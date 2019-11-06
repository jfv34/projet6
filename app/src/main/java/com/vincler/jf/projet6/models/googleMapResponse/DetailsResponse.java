package com.vincler.jf.projet6.models.googleMapResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailsResponse {

    public DetailsResponse(String name, String address, String photo, String phoneNumber, String webSite) {

        this.name = name;
        this.address = address;
        this.photo = photo;
        this.phoneNumber = phoneNumber;
        this.webSite = webSite;

    }

    @SerializedName("result")
    public ResultDetailsResponse resultDetailsResponse;

    @SerializedName("international_phone_number")
    public String phoneNumber;

    @SerializedName("name")
    public String name;

    @SerializedName("photo_reference")
    public String photo;

    @SerializedName("photos")
    public List<PhotosResponse> PhotosResponse;

    @SerializedName("formatted_address")
    public String address;

    @SerializedName("website")
    public String webSite;

    public String getPhoneNumber() {
        if(resultDetailsResponse!=null){
            return resultDetailsResponse.phoneNumber;
        } else return "";
    }

    public String getWebSite() {
        if (resultDetailsResponse != null) {
            return resultDetailsResponse.website;
        }else return "";
    }

    public String getName() {
        if (resultDetailsResponse != null) {
            return resultDetailsResponse.name;
        } else return "";
    }

    public String getAddress() {
        if (resultDetailsResponse != null) {
            return resultDetailsResponse.address;
        } else return "";
    }

    public String getPhoto() {
        if ((PhotosResponse) != null) {
            return PhotosResponse.get(0).photo_reference_response;
        } else return "";
    }
}

