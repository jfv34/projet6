package com.vincler.jf.projet6.models;

import com.google.gson.annotations.SerializedName;

public class RestaurantResponse {

    public RestaurantResponse(String restaurantName, double latitude, double longitude, String address) {
        this.restaurantName = restaurantName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;

    }

    @SerializedName("name")
    public String restaurantName;

    @SerializedName("lat")
    public double latitude;

    @SerializedName("lng")
    public double longitude;

    @SerializedName("geometry")
    public GeometryResponse geometryResponse;

    @SerializedName("vicinity")
    public String address;


    public String getRestaurant() {
        return restaurantName;
    }

    public double getLatitude() {

        return geometryResponse.locationResponse.latitude;
    }

    public double getLongitude() {

        return geometryResponse.locationResponse.longitude;
    }

    public String getAddress() {
        return address;
    }
}
