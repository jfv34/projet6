package com.vincler.jf.projet6.models;

public class LikeRestaurant {

    private String uid;
    private String latLongRestaurant;

    public LikeRestaurant(String uid, String latLongRestaurant) {
        this.uid = uid;
        this.latLongRestaurant = latLongRestaurant;
    }

    // --- GETTERS ---
    public String getUid() {
        return uid;
    }

    public String getLatLongRestaurant() {
        return latLongRestaurant;
    }


    // --- SETTERS ---
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setLatLongRestaurant(String latLongRestaurant) {
        this.latLongRestaurant = latLongRestaurant;
    }
}



