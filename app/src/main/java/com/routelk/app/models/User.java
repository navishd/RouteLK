package com.routelk.app.models;

public class User {

    private String userId;
    private String fullName;
    private String email;
    private String phone;
    private String documentId;

    public User() {
    }

    public User(String userId,
                String fullName,
                String email,
                String phone,
                String role) {

        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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