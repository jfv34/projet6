package com.vincler.jf.projet6.models;

public class User {

    private String userId;
    private String userName;
    private String email;
    private String photoUrl;
    private String phoneNumber;
    private String restaurantChoice;

    public User() {
    }

    public User(String uid, String username) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.phoneNumber = phoneNumber;
        this.restaurantChoice = restaurantChoice;
    }

    // --- GETTERS ---
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return userName;
    }


    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRestaurantChoicer() {
        return restaurantChoice;
    }

    // --- SETTERS ---
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRestaurantChoice(String restaurantChoice) {
        this.restaurantChoice = restaurantChoice;
    }
}



