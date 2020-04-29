package com.plagueinc.model;

import java.util.Random;

public class City {

    private int width;
    private int height;
    private Random r = new Random();

    public City(int width, int height){
        this.width = width;
        this.height = height;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public boolean isInside(Position p){
        return p.x >= Preferences.r
                && p.x <= width-Preferences.r
                && p.y >= Preferences.r
                && p.y <= height-Preferences.r;
    }

    public static int distance(Position p1, Position p2) {
        return (int) Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    public Position randomPosition(){
        return new Position(r.nextInt(this.width+Preferences.r)-Preferences.r,
                r.nextInt(this.height+Preferences.r)-Preferences.r);
    }
}
