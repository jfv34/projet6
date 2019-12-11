package com.vincler.jf.projet6.models.search;

import com.google.gson.annotations.SerializedName;
import com.vincler.jf.projet6.models.googleMapResponse.GeometryResponse;
import com.vincler.jf.projet6.models.googleMapResponse.LocationResponse;

class ResultSearchResponse {
    @SerializedName("lat")
    public double latitude;

    @SerializedName("lng")
    public double longitude;

    @SerializedName("location")
    public LocationResponse locationResponse;

    @SerializedName("geometry")
    public GeometryResponse geometryResponse;
}
