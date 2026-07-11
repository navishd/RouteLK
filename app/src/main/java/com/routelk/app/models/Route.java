package com.routelk.app.models;

public class Route {

    private String id;
    private String routeName;
    private String from;
    private String to;
    private double distance;
    private double price;
    private String routeID;


    public Route() {

    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getRouteName() {
        return routeName;
    }


    public String getFrom() {
        return from;
    }


    public String getTo() {
        return to;
    }



    public double getDistance() {
        return distance;
    }


    public double getPrice() {
        return price;
    }

    public String getRouteID() {
        return routeID;
    }
    public void setRouteID(String routeID){
        this.routeID = routeID;
    }
}
