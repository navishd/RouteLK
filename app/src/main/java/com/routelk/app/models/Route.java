package com.routelk.app.models;

public class Route {

    private String routeID; // Matches your Firestore screenshot
    private String routeName;
    private String from;
    private String to;
    private int distance;
    private int price;

    public Route() {
    }

    public Route(String routeID, String routeName, String from, String to, int distance, int price) {
        this.routeID = routeID;
        this.routeName = routeName;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.price = price;
    }

    public String getRouteID() { return routeID; }
    public void setRouteID(String routeID) { this.routeID = routeID; }

    // Alias methods for compatibility with Service and Activity logic
    public String getId() { return routeID; }
    public void setId(String id) { this.routeID = id; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public int getDistance() { return distance; }
    public void setDistance(int distance) { this.distance = distance; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}