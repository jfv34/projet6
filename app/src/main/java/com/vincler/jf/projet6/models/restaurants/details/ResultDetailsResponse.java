package com.vincler.jf.projet6.models.restaurants.details;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ResultDetailsResponse {
    @SerializedName("international_phone_number")
    public String phoneNumber;

    @SerializedName("website")
    public String website;

    @SerializedName("name")
    public String name;

    @SerializedName("photo")
    public List<PhotosResponse> photosResponse;

    @SerializedName("formatted_address")
    public String address;






}