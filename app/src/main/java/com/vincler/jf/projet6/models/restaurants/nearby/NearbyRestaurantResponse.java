package com.vincler.jf.projet6.models.restaurants.nearby;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.vincler.jf.projet6.models.googleMapResponse.GeometryResponse;
import com.vincler.jf.projet6.models.googleMapResponse.OpeningHoursResponse;

import java.util.ArrayList;

public class NearbyRestaurantResponse {

    public NearbyRestaurantResponse(String restaurantName, double latitude, double longitude, String address,
                                    String photo_reference, Double rating, ArrayList typesListResponse, String placeid,
                                    ArrayList isOpenNowList) {
        this.restaurantName = restaurantName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.photo_reference = photo_reference;
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
    public String photo_reference;

    @SerializedName("photos")
    public ArrayList<NearbyPhotosResponse> photos;

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
  public String getPhoto(){

        if ((photos) != null) {
            return photos.get(0).reference;

        }
        return "";
    }

    public Double getRating() {
        return rating;
    }
}