package com.plagueinc.model;

import java.util.*;

public class Population {
    List<Person> personList = new ArrayList<Person>();

    public Population(City map, int number) {
        int i = 0;
        while(i++<number) {
            Person person = new Person(map, this);
            personList.add(person);
        }
    }

    public List<Person> getNeighbors(Person person) {
        List<Person> list = new ArrayList<Person>();
        for (Person p : personList) {
            if (p.isAlive() && City.distance(p.getPosition(), person.getPosition()) <= Preferences.infectious_distance) {
                list.add(p);
            }
        }
        return list;
    }

    public List<Person> getLivePerson() {
        List<Person> list = new ArrayList<Person>();
        for(Person p:personList){
            if(p.isAlive()){
                list.add(p);
            }
        }
        return list;
    }

    public List<Person> getHealthPersonList(){
        List<Person> list = new ArrayList<Person>();
        for(Person p:personList){
            if(p.isHealth()){
                list.add(p);
            }
        }
        return list;
    }

    public List<Person> getInfectedPersonList(){
        List<Person> list = new ArrayList<Person>();
        for(Person p:personList){
            if(p.isInfected()){
                list.add(p);
            }
        }
        return list;
    }

    public List<Person> getImmunityPersonList(){
        List<Person> list = new ArrayList<Person>();
        for(Person p:personList){
            if(p.isImmunity()){
                list.add(p);
            }
        }
        return list;
    }

    public List<Person> getDeadPersonList(){
        List<Person> list = new ArrayList<Person>();
        for(Person p:personList){
            if(p.isDead()){
                list.add(p);
            }
        }
        return list;
    }

    public void addPerson(Person person){
        this.personList.add(person);
    }
}
