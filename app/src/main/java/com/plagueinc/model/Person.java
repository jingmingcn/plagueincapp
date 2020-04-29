package com.plagueinc.model;

import android.util.Log;

import java.util.*;

class Person {

    private static String TAG = Person.class.getSimpleName();

    enum HealthState {
        Health, Infected, Dead, Immunity
    }

    enum HumanRight {
        Free, Quarantined
    }

    private String id;
    private HealthState healthState;
    private HumanRight humanRight;
    private int infectiousPeriod = 0;
    private Position position; // Position of Destination
    private Position _position; // Position of Departure
    private Position currentPosition;
    private long lastLoopTime; // Time for Calculating Position of Movement
    private long lastBubbleTime; // Time for Bubble Effecting
    private long lastInfectTime; // Time for Infecting Tick
    private long lastStepTime; // Time for Moving a Step
    private City city;
    private Population population;
    Random r = new Random();
    public float infect_r = Preferences.r;
    public float dead_r = Preferences.r;
    public byte infect_r_frame;

    public Person(City city, Population population) {
        this(city,population,HealthState.Health);
    }

    public Person(City city, Population population, HealthState healthState) {
        this(city, population,healthState,city.randomPosition());
    }

    public Person(City city, Population population, HealthState healthState, Position position) {
        this.city = city;
        this.population = population;
        this.healthState = healthState;
        this.humanRight = HumanRight.Free;
        this.position = position;
        this._position = position;
        this.currentPosition = position;
        this.id = UUID.randomUUID().toString();
        lastLoopTime = lastBubbleTime = lastInfectTime = lastStepTime = System.currentTimeMillis();
    }

    public void live() {

        long now = System.currentTimeMillis();

        // Move Step Logic
        if(now - lastStepTime >= Preferences.step_period) {
            if(this.healthState == HealthState.Infected) {
                infectiousPeriod++;
            }
            randomWalk();
            lastStepTime = now;
            lastLoopTime = now;
        }


        // Quarantined Logic

        if(this.healthState == HealthState.Infected && r.nextDouble() <= Preferences.quarantined_rate) {
            this.humanRight = HumanRight.Quarantined;
            this.position = this.getCurrentPosition();
            this._position = this.getCurrentPosition();

        }

        if (this.healthState == HealthState.Infected && infectiousPeriod >= Preferences.incubation_period) {
            if (r.nextFloat() < Preferences.death_rate) {
                this.healthState = HealthState.Dead;
                this.position = this.getCurrentPosition();
                this._position = this.getCurrentPosition();
            } else {
                this.healthState = HealthState.Immunity;
                this.humanRight = HumanRight.Free;
            }
        }

//        switch(this.healthState) {
//            case Health: color = Color.green;break;
//            case Dead: color = Color.gray;break;
//            case Infected: color = Color.red;break;
//            case Immunity: color = Color.blue;break;
//        }



        infect();

        // Update Infection Circle Radius Logic
        if(this.healthState==HealthState.Infected && this.humanRight == Person.HumanRight.Free) {
            float r = (now-lastBubbleTime)/1000f;
            if(infect_r>Preferences.infectious_distance || infect_r<Preferences.r){
                infect_r = Preferences.r;
            }

            infect_r += (Preferences.infectious_distance-Preferences.r)*r;
        }
        lastBubbleTime = now;

        // Update Current Position Logic
        float r = ((float)(now - lastStepTime))/Preferences.step_period;
        Position p = _position;
        Position p_ = position;
        int x = (int)(p.x + r*(p_.x-p.x));
        int y = (int)(p.y + r*(p_.y-p.y));
        currentPosition = new Position(x,y);
        lastLoopTime = now;
        //Log.d(TAG,String.format("r:%1$s,p(x,y)=(%2$s,%3$s),p_(x,y)=(%4$s,%5$s),(x,y)=(%6$s,%7$s)",r,p.x,p.y,p_.x,p_.y,x,y));




    }

    public Position getPosition() {
        return this.position;
    }
    public Position getCurrentPosition() {
        return currentPosition;
    }
    public float getInfectR(){return infect_r;}
    public float getDeadR(){return dead_r;}

    public boolean isAlive() {
        return this.healthState != HealthState.Dead;
    }

    public boolean isHealth() {
        return this.healthState == HealthState.Health;
    }

    public boolean isDead(){
        return this.healthState == HealthState.Dead;
    }

    public boolean isImmunity(){
        return this.healthState == HealthState.Immunity;
    }

    public boolean isInfected(){
        return this.healthState == HealthState.Infected;
    }

    public boolean isFree(){return this.humanRight == HumanRight.Free;}

    public void infected() {
        if (this.healthState == HealthState.Health && r.nextFloat() < Preferences.infectious_rate) {
            this.healthState = HealthState.Infected;
        }
    }

    private void randomWalk() {
        if (healthState != HealthState.Dead && humanRight != HumanRight.Quarantined) {
            this._position = this.position;
            Position p_;
            do{
                int walk_distance = Preferences.walk_distance;
                int x_ = r.nextInt(walk_distance*2)-walk_distance;
                int y_ = r.nextInt(walk_distance*2)-walk_distance;
                p_ = new Position(position.x + x_, position.y + y_);
            }while(!city.isInside(p_));
            this.position = p_;
        }
    }

    private void infect() {
        if (healthState == HealthState.Infected && humanRight == HumanRight.Free) {
            List<Person> neighbors = population.getNeighbors(this);
            for (Person p : neighbors) {
                p.infected();
            }
        }

    }

}
