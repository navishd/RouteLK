package com.routelk.app.models;


public class Bus {


    private String busID;
    private String busName;
    private String busNumber;
    private String busType;

    private String routeId;
    private String scheduleId;

    private String from;
    private String to;

    private String departureTime;
    private String arrivalTime;

    private String price;
    private int seatCount;



    public Bus(){

    }



    public Bus(
            String busID,
            String busName,
            String busNumber,
            String busType,
            String routeId,
            String scheduleId,
            String from,
            String to,
            String departureTime,
            String arrivalTime,
            String price,
            int seatCount
    ){

        this.busID = busID;
        this.busName = busName;
        this.busNumber = busNumber;
        this.busType = busType;

        this.routeId = routeId;
        this.scheduleId = scheduleId;

        this.from = from;
        this.to = to;

        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;

        this.price = price;
        this.seatCount = seatCount;

    }



    public String getBusID() {
        return busID;
    }


    public String getBusName() {
        return busName;
    }


    public String getBusNumber() {
        return busNumber;
    }


    public String getBusType() {
        return busType;
    }


    public String getRouteId() {
        return routeId;
    }


    public String getScheduleId() {
        return scheduleId;
    }


    public String getFrom() {
        return from;
    }


    public String getTo() {
        return to;
    }


    public String getDepartureTime() {
        return departureTime;
    }


    public String getArrivalTime() {
        return arrivalTime;
    }


    public String getPrice() {
        return price;
    }


    public int getSeatCount() {
        return seatCount;
    }


    public void setBusID(String busID) {
        this.busID = busID;
    }


    public void setBusName(String busName) {
        this.busName = busName;
    }


    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }


    public void setBusType(String busType) {
        this.busType = busType;
    }


    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }


    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }


    public void setFrom(String from) {
        this.from = from;
    }


    public void setTo(String to) {
        this.to = to;
    }


    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }


    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }


    public void setPrice(String price) {
        this.price = price;
    }


    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

}