package com.routelk.app.models;


public class Route {


    private String id;

    private String routeID;

    private String from;

    private String to;

    private double distance;

    private double price;

    private String routeName;



    public Route(){

    }




    public String getId(){

        return id;

    }


    public void setId(String id){

        this.id = id;
        this.routeID = id;

    }





    // Firebase field routeID

    public String getRouteID(){

        return routeID;

    }


    public void setRouteID(String routeID){

        this.routeID = routeID;
        this.id = routeID;

    }





    // Support old naming

    public String getRouteId(){

        return routeID;

    }


    public void setRouteId(String routeId){

        this.routeID = routeId;
        this.id = routeId;

    }





    public String getFrom(){

        return from;

    }


    public void setFrom(String from){

        this.from = from;

    }





    public String getTo(){

        return to;

    }


    public void setTo(String to){

        this.to = to;

    }





    public String getRouteName(){

        return routeName;

    }


    public void setRouteName(String routeName){

        this.routeName = routeName;

    }





    public double getDistance(){

        return distance;

    }


    public void setDistance(double distance){

        this.distance = distance;

    }





    public double getPrice(){

        return price;

    }


    public void setPrice(double price){

        this.price = price;

    }

}