package com.vincler.jf.projet6.models;

public class Restaurant {

    private String name;
    private double latitude;
    private double longitude;
    private String address;

    public Restaurant(String name, double latitude, double longitude, String address) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }


}
