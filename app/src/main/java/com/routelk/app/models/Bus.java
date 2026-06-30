package com.routelk.app.models;

public class Bus {

    private String id;
    private String busName;
    private String busNumber;
    private String busType;
    private String totalSeats;

    public Bus() {
    }

    public Bus(String id,
               String busName,
               String busNumber,
               String busType,
               String totalSeats) {

        this.id = id;
        this.busName = busName;
        this.busNumber = busNumber;
        this.busType = busType;
        this.totalSeats = totalSeats;
    }

    // Getters

    public String getId() {
        return id;
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

    public String getTotalSeats() {
        return totalSeats;
    }

    // Setters

    public void setId(String id) {
        this.id = id;
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

    public void setTotalSeats(String totalSeats) {
        this.totalSeats = totalSeats;
    }
}