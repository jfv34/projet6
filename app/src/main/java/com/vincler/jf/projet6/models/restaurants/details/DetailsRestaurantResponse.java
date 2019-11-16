package com.vincler.jf.projet6.models.restaurants.details;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailsRestaurantResponse {

    public DetailsRestaurantResponse(String name, String address, List<PhotosResponse> photos, String phoneNumber, String webSite) {

        this.name = name;
        this.address = address;
        this.photos = photos;
        this.phoneNumber = phoneNumber;
        this.webSite = webSite;
    }

    @SerializedName("result")
    public ResultDetailsResponse resultDetailsResponse;

    @SerializedName("international_phone_number")
    public String phoneNumber;

    @SerializedName("name")
    public String name;

    @SerializedName("photos")
    public List<PhotosResponse> photos;

    @SerializedName("formatted_address")
    public String address;

    @SerializedName("website")
    public String webSite;


    public String getName() {
        if (resultDetailsResponse != null) {
            return resultDetailsResponse.name;
        } else return "";
    }
}

