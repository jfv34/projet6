package com.vincler.jf.projet6.models;

import com.google.gson.annotations.SerializedName;

class LocationResponse {
    @SerializedName("lat")
    public double latitude;

    @SerializedName("lng")
    public double longitude;

}
