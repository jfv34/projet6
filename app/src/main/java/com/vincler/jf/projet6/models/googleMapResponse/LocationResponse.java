package com.vincler.jf.projet6.models.googleMapResponse;

import com.google.gson.annotations.SerializedName;

public class LocationResponse {
    @SerializedName("lat")
    public double latitude;

    @SerializedName("lng")
    public double longitude;
}
