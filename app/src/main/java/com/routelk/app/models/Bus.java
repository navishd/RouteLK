package com.routelk.app.models;

public class Bus {

    private String id;
    private String busName;
    private String busNumber;
    private String busType; // Matches your Firestore screenshot
    private String totalSeats;

    public Bus() {
    }

    public Bus(String id, String busName, String busNumber, String busType, String totalSeats) {
        this.id = id;
        this.busName = busName;
        this.busNumber = busNumber;
        this.busType = busType;
        this.totalSeats = totalSeats;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBusName() { return busName; }
    public void setBusName(String busName) { this.busName = busName; }

    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }

    public String getBusType() { return busType; }
    public void setBusType(String busType) { this.busType = busType; }

    public String getTotalSeats() { return totalSeats; }
    public void setTotalSeats(String totalSeats) { this.totalSeats = totalSeats; }
}