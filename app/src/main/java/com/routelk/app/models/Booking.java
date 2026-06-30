package com.routelk.app.models;

public class Booking {

    private String id;
    private String userName;
    private String from;
    private String to;
    private String busName;
    private String seatNo;
    private String date;

    public Booking() {
    }

    public Booking(String id,
                   String userName,
                   String from,
                   String to,
                   String busName,
                   String seatNo,
                   String date) {

        this.id = id;
        this.userName = userName;
        this.from = from;
        this.to = to;
        this.busName = busName;
        this.seatNo = seatNo;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getBusName() {
        return busName;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public String getDate() {
        return date;
    }
}