package com.example.carpool;

import java.util.ArrayList;
import java.util.Random;

public class Vehicle {
    private String type;
    private Integer capacity;
    private String model;
    private String email;
    private Integer id;
    private String open;

    public Vehicle(){}

    public Vehicle(String t, Integer c, String m, String e)
    {
        Random r = new Random();
        this.id = r.nextInt(99999);;
        this.type = t;
        this.capacity = c;
        this.model = m;
        this.email = e;
        this.open = "Open";
    }


    public String getType() {return this.type;}

    public String getModel() {return this.model;}

    public Integer getCapacity() {return this.capacity;}
    public String getEmail() {return this.email;}

    public Integer getID()
    {
        return this.id;
    }

    public String getOpen()
    {
        if(this.open.equals("Open"))
            return "Open";
        else
            return "Closed";
    }

    public void setOpen() {if(this.open.equals("Open")) {this.open = "Closed";} else {this.open = "Open";}}

    public void decrementCapacity()
    {
        this.capacity = this.capacity-1;
    }

}
