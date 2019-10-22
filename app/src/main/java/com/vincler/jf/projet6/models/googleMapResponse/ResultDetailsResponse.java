package com.vincler.jf.projet6.models.googleMapResponse;

import com.google.gson.annotations.SerializedName;

class ResultDetailsResponse {
    @SerializedName("international_phone_number")
    public String phoneNumber;

    @SerializedName("website")
    public String website;
}