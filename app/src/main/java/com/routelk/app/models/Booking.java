package com.routelk.app.models;

public class Booking {

    private String id;
    private String userId;
    private String userName; // The user who made the booking
    private String passengerName;
    private String passengerPhone;
    private String from;
    private String to;
    private String busName;
    private String seatNo;
    private String date;

    public Booking() {
    }

    public Booking(String id,
                   String userId,
                   String userName,
                   String passengerName,
                   String passengerPhone,
                   String from,
                   String to,
                   String busName,
                   String seatNo,
                   String date) {

        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.passengerName = passengerName;
        this.passengerPhone = passengerPhone;
        this.from = from;
        this.to = to;
        this.busName = busName;
        this.seatNo = seatNo;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerPhone() {
        return passengerPhone;
    }

    public void setPassengerPhone(String passengerPhone) {
        this.passengerPhone = passengerPhone;
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

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}