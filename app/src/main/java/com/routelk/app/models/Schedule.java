package com.routelk.app.models;


public class Schedule {


    private String id;

    private String busId;

    private String routeId;

    private String departureTime;

    private String arrivalTime;

    private String travelDate;

    private int price;



    // Required empty constructor for Firebase

    public Schedule(){

    }





    public String getId(){

        return id;

    }


    public void setId(String id){

        this.id = id;

    }






    public String getBusId(){

        return busId;

    }


    public void setBusId(String busId){

        this.busId = busId;

    }






    public String getRouteId(){

        return routeId;

    }


    public void setRouteId(String routeId){

        this.routeId = routeId;

    }






    public String getDepartureTime(){

        return departureTime;

    }


    public void setDepartureTime(String departureTime){

        this.departureTime = departureTime;

    }






    public String getArrivalTime(){

        return arrivalTime;

    }


    public void setArrivalTime(String arrivalTime){

        this.arrivalTime = arrivalTime;

    }






    public String getTravelDate(){

        return travelDate;

    }

    public void setTravelDate(String travelDate) {

    public void setTravelDate(String travelDate){

        this.travelDate = travelDate;

    }






    public int getPrice(){

        return price;

    }


    public void setPrice(int price){

        this.price = price;

    }


}