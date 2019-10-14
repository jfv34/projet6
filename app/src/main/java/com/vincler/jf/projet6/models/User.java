package com.vincler.jf.projet6.models;

import android.net.Uri;

public class User {

    private String uid;
    private String userName;
    private String email;
    private String phoneNumber;
    private String restaurantChoice;

    public User(String uid, String userName, String email, String phoneNumber, String restaurantChoice) {
        this.uid = uid;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.restaurantChoice = restaurantChoice;
    }

    // --- GETTERS ---
    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return userName;
    }


    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRestaurantChoice() {
        return restaurantChoice;
    }

    // --- SETTERS ---
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRestaurantChoice(String restaurantChoice) {
        this.restaurantChoice = restaurantChoice;
    }
}



