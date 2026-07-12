package com.routelk.app.models;

import com.google.firebase.Timestamp;

public class Booking {

    private String id;
    private String userId;
    private String userName;
    private String passengerName;
    private String passengerPhone;
    private String passengerEmail;
    private String from;
    private String to;
    private String busName;
    private String seatNo;
    private String date;
    private Timestamp timestamp;
    private String documentId;
    private String status;
    private double price;
    private String time;


    public Booking() {
    }

    public Booking(String id,
                   String userId,
                   String userName,
                   String passengerName,
                   String passengerPhone,
                   String passengerEmail,
                   String from,
                   String to,
                   String busName,
                   String seatNo,
                   String date,
                   Timestamp timestamp,
                   String status,
                   double price,
                   String time) {

        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.passengerName = passengerName;
        this.passengerPhone = passengerPhone;
        this.passengerEmail = passengerEmail;
        this.from = from;
        this.to = to;
        this.busName = busName;
        this.seatNo = seatNo;
        this.date = date;
        this.timestamp = timestamp;
        this.status = status;
        this.price = price;
        this.time = time;
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

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
