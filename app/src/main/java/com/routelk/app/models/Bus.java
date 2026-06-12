package com.routelk.app.models;

public class Bus {
    private String name;
    private String time;
    private String type;
    private String price;

    public Bus(String name, String time, String type, String price) {
        this.name = name;
        this.time = time;
        this.type = type;
        this.price = price;
    }

    public String getName() { return name; }
    public String getTime() { return time; }
    public String getType() { return type; }
    public String getPrice() { return price; }
}