package com.vincler.jf.projet6.models;

import com.google.gson.annotations.SerializedName;

public class Restaurants {

    public Restaurants(String restaurantName, double latitude, double longitude) {
        this.restaurantName = restaurantName;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    @SerializedName("name")
    public String restaurantName;

    @SerializedName("lat")
    public double latitude;

    @SerializedName("lng")
    public double longitude;

    @SerializedName("geometry")
    public GeometryResponse geometryResponse;


    public String getRestaurant() {
        return restaurantName;
    }

    public double getLatitude() {

        return geometryResponse.locationResponse.latitude;
    }

    public double getLongitude() {

        return geometryResponse.locationResponse.longitude;
    }
}
