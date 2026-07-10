package com.routelk.app.models;

import java.util.List;

public class Schedule {

    private String scheduleID;
    private String busID;
    private String routeID;
    private String departureTime;
    private String arrivalTime;
    private int price;
    private List<String> operatingDays;

    // Empty Constructor (Firestore requires this)
    public Schedule() {
    }

    // Constructor
    public Schedule(String scheduleID,
                    String busID,
                    String routeID,
                    String departureTime,
                    String arrivalTime,
                    int price,
                    List<String> operatingDays) {

        this.scheduleID = scheduleID;
        this.busID = busID;
        this.routeID = routeID;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.operatingDays = operatingDays;
    }

    // Getters

    public String getScheduleID() {
        return scheduleID;
    }

    public String getBusID() {
        return busID;
    }

    public String getRouteID() {
        return routeID;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public int getPrice() {
        return price;
    }

    public List<String> getOperatingDays() {
        return operatingDays;
    }

    // Setters

    public void setScheduleID(String scheduleID) {
        this.scheduleID = scheduleID;
    }

    public void setBusID(String busID) {
        this.busID = busID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setOperatingDays(List<String> operatingDays) {
        this.operatingDays = operatingDays;
    }

}