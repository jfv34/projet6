package com.vincler.jf.projet6.models;

public class Restaurant {

    private String name;
    private String type;
    private double latitude;
    private double longitude;
    private String address;
    private String photo;

    public Restaurant(String name, double latitude, double longitude, String address, String photo) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.photo = photo;
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

    public String getPhoto() {
        return photo;
    }


}
