package com.vincler.jf.projet6.models;

import java.util.ArrayList;

public class Restaurant {

    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private String photo;
    private SearchStatus searchStatus;
    private ArrayList<String> opening_hours_List;
    private String placeid;

    public Restaurant(String name, double latitude, double longitude, String address, String photo,
                      SearchStatus searchStatus, ArrayList<String> opening_hours_List, String placeid) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.photo = photo;
        this.searchStatus = searchStatus;
        this.opening_hours_List = opening_hours_List;
        this.placeid = placeid;
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

    public String getPlaceid() {
        return placeid;
    }

    public SearchStatus getSearchStatus() {
        return searchStatus;
    }

    public ArrayList<String> getOpening_hours_List() {
        return opening_hours_List;
    }


}
