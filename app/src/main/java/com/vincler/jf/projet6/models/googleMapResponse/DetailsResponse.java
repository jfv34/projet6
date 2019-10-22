package com.vincler.jf.projet6.models.googleMapResponse;

import com.google.gson.annotations.SerializedName;

public class DetailsResponse {

    public DetailsResponse(String phoneNumber, String webSite) {
        this.phoneNumber = phoneNumber;
        this.webSite = webSite;
    }

    @SerializedName("result")
    public ResultDetailsResponse resultDetailsResponse;

    @SerializedName("international_phone_number")
    public String phoneNumber;

    @SerializedName("website")
    public String webSite;

    public String getPhoneNumber() {
        if(resultDetailsResponse!=null){
        return resultDetailsResponse.phoneNumber;}else return "";
    }

    public String getWebSite() {
        if (resultDetailsResponse != null) {
            return resultDetailsResponse.website;
        }else return "";
    }

}

