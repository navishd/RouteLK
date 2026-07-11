package com.routelk.app.models;


public class Bus {


    private String busID;

    private String busName;

    private String busNumber;

    private String busType;


    private int seatCount;
    private String totalSeats;


    private String departureTime;

    private String arrivalTime;


    private String from;

    private String to;


    private double price;


    private String routeId;

    private String scheduleId;



    // Empty constructor required for Firebase

    public Bus(){

    }




    public String getBusID() {

        return busID;

    }


    public void setBusID(String busID) {

        this.busID = busID;

    }





    public String getBusName() {

        return busName;

    }


    public void setBusName(String busName) {

        this.busName = busName;

    }





    public String getBusNumber() {

        return busNumber;

    }


    public void setBusNumber(String busNumber) {

        this.busNumber = busNumber;

    }





    public String getBusType() {

        return busType;

    }


    public void setBusType(String busType) {

        this.busType = busType;

    }





    public int getSeatCount() {

        return seatCount;

    }


    public void setSeatCount(int seatCount) {

        this.seatCount = seatCount;

    }





    // Old code compatibility

    public int getTotalSeats() {

        return seatCount;

    }


    public void setTotalSeats(int totalSeats) {

        this.seatCount = totalSeats;

    }





    public String getDepartureTime() {

        return departureTime;

    }


    public void setDepartureTime(String departureTime) {

        this.departureTime = departureTime;

    }





    public String getArrivalTime() {

        return arrivalTime;

    }


    public void setArrivalTime(String arrivalTime) {

        this.arrivalTime = arrivalTime;

    }





    public String getFrom() {

        return from;

    }


    public void setFrom(String from) {

        this.from = from;

    }





    public String getTo() {

        return to;

    }


    public void setTo(String to) {

        this.to = to;

    }





    public double getPrice() {

        return price;

    }


    public void setPrice(double price) {

        this.price = price;

    }





    public String getRouteId() {

        return routeId;

    }


    public void setRouteId(String routeId) {

        this.routeId = routeId;

    }





    public String getScheduleId() {

        return scheduleId;

    }


    public void setScheduleId(String scheduleId) {

        this.scheduleId = scheduleId;

    }



}