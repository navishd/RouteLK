package com.routelk.app.models;

public class Route {

    private String id;
    private String routeName;
    private String from;
    private String to;
    private String distance;
    private String price;

    public Route() {
    }

    public Route(String id, String routeName,
                 String from,
                 String to,
                 String distance,
                 String price) {

        this.id = id;
        this.routeName = routeName;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.price = price;
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

    public String getDistance() {
        return distance;
    }

    public String getPrice() {
        return price;
    }
}