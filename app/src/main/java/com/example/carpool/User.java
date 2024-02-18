package com.example.carpool;

import java.util.ArrayList;
public class User {
    private Integer id;
    private String email;
    private String type;
    private ArrayList<Vehicle> own = new ArrayList<>();
    private ArrayList<Vehicle> ride = new ArrayList<>();

    public User(){}

    public User(Integer id, String email, String type)
    {
        this.id = id;
        this.email = email;
        this.type = type;
    }



    public void addVehicle(Vehicle v) {own.add(v);}
    public ArrayList<Vehicle> getVehicle() {return this.own;}
    public void addVehicleRode(Vehicle v) {ride.add(v);}

    public String getEmail(){
        return this.email;
    }
    public String getType(){
        return this.type;
    }

}
