package com.vincler.jf.projet6.models.restaurants.nearby;

import com.google.gson.annotations.SerializedName;
import com.vincler.jf.projet6.models.googleMapResponse.GeometryResponse;
import com.vincler.jf.projet6.models.googleMapResponse.OpeningHoursResponse;
import com.vincler.jf.projet6.models.restaurants.details.PhotosResponse;

import java.util.ArrayList;
import java.util.List;

public class NearbyRestaurantResponse {

    public NearbyRestaurantResponse(String restaurantName, double latitude, double longitude, String address,
                                    String photo, Double rating, ArrayList typesListResponse, String placeid,
                                    ArrayList isOpenNowList) {
        this.restaurantName = restaurantName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.photo = photo;
        this.rating = rating;
        this.typesListResponse = typesListResponse;
        this.placeid = placeid;
        this.isOpenNowList = isOpenNowList;

    }

    @SerializedName("name")
    public String restaurantName;

    @SerializedName("lat")
    public double latitude;

    @SerializedName("lng")
    public double longitude;

    @SerializedName("photo_reference")
    public String photo;

    @SerializedName("rating")
    public Double rating;

    @SerializedName("geometry")
    public GeometryResponse geometryResponse;

    @SerializedName("types")
    public ArrayList typesListResponse;

    @SerializedName("vicinity")
    public String address;

    @SerializedName("place_id")
    public String placeid;

    @SerializedName("opening_hours")
    public OpeningHoursResponse openingHoursResponse;

    @SerializedName("open_now")
    public ArrayList isOpenNowList;

    public String getName() {
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

    public String getPlaceid() {
        return placeid;
    }

    public String getIsOpenNow(){

        if ((openingHoursResponse) != null) {
            return openingHoursResponse.isOpenNow.toString();
        }
       return "";
    }


    public Double getRating() {
        return rating;
    }
}