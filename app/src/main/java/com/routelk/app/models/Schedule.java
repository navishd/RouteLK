package com.routelk.app.models;

public class Schedule {

    private String id;
    private String busId;
    private String busName;

    private String routeId;
    private String from;
    private String to;

    private String departureTime;
    private String arrivalTime;

    private String date;
    private String operatingDays;

    private int price;


    // Firestore empty constructor
    public Schedule() {

    }


    public Schedule(String id,
                    String busId,
                    String busName,
                    String routeId,
                    String from,
                    String to,
                    String departureTime,
                    String arrivalTime,
                    String date,
                    String operatingDays,
                    int price) {

        this.id = id;
        this.busId = busId;
        this.busName = busName;
        this.routeId = routeId;
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.date = date;
        this.operatingDays = operatingDays;
        this.price = price;

    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getBusId() {
        return busId;
    }


    public void setBusId(String busId) {
        this.busId = busId;
    }


    public String getBusName() {
        return busName;
    }


    public void setBusName(String busName) {
        this.busName = busName;
    }


    public String getRouteId() {
        return routeId;
    }


    public void setRouteId(String routeId) {
        this.routeId = routeId;
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


    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }


    public String getOperatingDays() {
        return operatingDays;
    }


    public void setOperatingDays(String operatingDays) {
        this.operatingDays = operatingDays;
    }


    public int getPrice() {
        return price;
    }


    public void setPrice(int price) {
        this.price = price;
    }

}