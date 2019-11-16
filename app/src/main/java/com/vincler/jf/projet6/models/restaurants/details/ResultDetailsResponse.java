package com.vincler.jf.projet6.models.restaurants.details;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultDetailsResponse {
    @SerializedName("international_phone_number")
    public String phoneNumber;

    @SerializedName("website")
    public String website;

    @SerializedName("name")
    public String name;

    @SerializedName("photos")
    public List<PhotosResponse> photosResponse;

    @SerializedName("formatted_address")
    public String address;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoto() {
        return photosResponse.get(0).reference;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebSite() {
        return website;
    }
}