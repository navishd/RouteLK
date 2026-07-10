package com.routelk.app.models;

public class Bus {

    private String busID;
    private String busName;
    private String busNumber;
    private String busType;
    private int seatCount;


    // Firestore empty constructor
    public Bus(){

    }


    public Bus(String busID,
               String busName,
               String busNumber,
               String busType,
               int seatCount){

        this.busID = busID;
        this.busName = busName;
        this.busNumber = busNumber;
        this.busType = busType;
        this.seatCount = seatCount;

    }


    public String getBusID(){

        return busID;
    }


    public void setBusID(String busID){

        this.busID = busID;
    }


    public String getBusName(){

        return busName;
    }


    public void setBusName(String busName){

        this.busName = busName;
    }


    public String getBusNumber(){

        return busNumber;
    }


    public void setBusNumber(String busNumber){

        this.busNumber = busNumber;
    }


    public String getBusType(){

        return busType;
    }


    public void setBusType(String busType){

        this.busType = busType;
    }


    public int getSeatCount(){

        return seatCount;
    }


    public void setSeatCount(int seatCount){

        this.seatCount = seatCount;
    }

}