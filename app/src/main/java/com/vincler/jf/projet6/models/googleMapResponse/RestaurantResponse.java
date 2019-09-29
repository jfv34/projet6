package com.vincler.jf.projet6.models.googleMapResponse;

import com.google.gson.annotations.SerializedName;
import com.vincler.jf.projet6.models.googleMapResponse.GeometryResponse;
import com.vincler.jf.projet6.models.googleMapResponse.PhotosResponse;

import java.util.ArrayList;
import java.util.List;

public class RestaurantResponse {

    public RestaurantResponse(String restaurantName, double latitude, double longitude, String address,
                              String photo, ArrayList typesListResponse) {
        this.restaurantName = restaurantName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.photo = photo;
        this.typesListResponse = typesListResponse;

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

    @SerializedName("types")
    public ArrayList typesListResponse;


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

        if ((photosResponse) != null) {
            return photosResponse.get(0).photo_html_response.get(0).toString();
        }
        return "";
    }

    public String getType() {
        return typesListResponse.get(0).toString();
    }
}