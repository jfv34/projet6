package com.vincler.jf.projet6.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantResponse {

    public RestaurantResponse(String restaurantName, double latitude, double longitude, String address,
                              String photo) {
        this.restaurantName = restaurantName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.photo = photo;
    }

    @SerializedName("name")
    public String restaurantName;

    @SerializedName("lat")
    public double latitude;

    @SerializedName("lng")
    public double longitude;

    @SerializedName("html_attributions")
    public String photo;

    @SerializedName("geometry")
    public GeometryResponse geometryResponse;

    @SerializedName("photos")
    public List<PhotosResponse> photosResponse;

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

    public String getPhoto() {
        return photosResponse.get(0).photo_html_response.get(0).toString();
    }
}