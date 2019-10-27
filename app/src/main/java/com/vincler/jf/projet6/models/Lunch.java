package com.vincler.jf.projet6.models;

public class Lunch {

    private String user_uid;
    private String restaurant_uid;

    public Lunch(String user_uid, String restaurant_uid){
        this.restaurant_uid = restaurant_uid;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getRestaurant_uid() {
        return restaurant_uid;
    }

    public void setRestaurant_uid(String restaurant_uid) {
        this.restaurant_uid = restaurant_uid;
    }
}



