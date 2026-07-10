package com.routelk.app.models;

public class User {

    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String documentId;


    // Firestore empty constructor
    public User() {

    }


    public User(String id,
                String fullName,
                String email,
                String phone) {

        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getFullName() {
        return fullName;
    }


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getDocumentId() {
        return documentId;
    }


    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}