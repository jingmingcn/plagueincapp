package com.plagueinc.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class ScenceFrame {

    private static final String TAG = ScenceFrame.class.getSimpleName();

    Population population;
    City city;
    Paint paint_health;
    Paint paint_infected;
    Paint paint_dead;
    Paint paint_immunity;
    Paint paint_infected_circle;
    Paint paint_quarantined;


    public ScenceFrame(City city,Population population){
        this.population = population;
        this.city = city;
        this.paint_health = new Paint();
        this.paint_health.setColor(Color.GREEN);

        this.paint_infected = new Paint();
        this.paint_infected.setColor(Color.RED);

        this.paint_infected_circle = new Paint();
        this.paint_infected_circle.setColor(Color.RED);
        this.paint_infected_circle.setStyle(Paint.Style.STROKE);
        this.paint_infected_circle.setStrokeWidth(2f);

        this.paint_dead = new Paint();
        this.paint_dead.setColor(Color.BLACK);

        this.paint_immunity = new Paint();
        this.paint_immunity.setColor(Color.BLUE);

        this.paint_quarantined = new Paint();
        this.paint_quarantined.setColor(Color.BLACK);
        this.paint_quarantined.setStyle(Paint.Style.STROKE);
        this.paint_quarantined.setStrokeWidth(2f);

    }

    public void drawOn(Canvas canvas) {

        List<Person> healthList = population.getHealthPersonList();
        for(Person p:healthList){
            canvas.drawCircle(p.getCurrentPosition().x,
                    p.getCurrentPosition().y,
                    Preferences.r,
                    paint_health);
        }

        List<Person> infectedList = population.getInfectedPersonList();
        for(Person person:infectedList){
            Position p = person.getCurrentPosition();
            canvas.drawCircle(p.x,
                    p.y,
                    Preferences.r,
                    paint_infected);
            if(person.isFree()){
                canvas.drawCircle(p.x,p.y,person.getInfectR(),paint_infected_circle);
            }else{
                canvas.drawRect(p.x-16,p.y-16,p.x+16,p.y+16,paint_quarantined);
            }

        }

        List<Person> immunityList = population.getImmunityPersonList();
        for(Person person:immunityList){
            Position p = person.getCurrentPosition();
            canvas.drawCircle(p.x,
                    p.y,
                    Preferences.r,
                    paint_immunity);
        }

//        List<Person> deadList = population.getDeadPersonList();
//        for(Person person:deadList){
//            Position p = person.getCurrentPosition();
//            canvas.drawCircle(p.x,
//                    p.y,
//                    person.getDeadR(),
//                    paint_dead);
//        }


    }

    public void doLogic(){
        List<Person> personList = population.getLivePerson();
        for(Person p : personList){
            p.live();
        }
    }

    public void clear() {

    }

    public void addInfectedPerson(int x, int y){
        Person person = new Person(city,population, Person.HealthState.Infected,new Position(x,y));

        population.addPerson(person);
    }
}
