package com.vincler.jf.projet6.models.search;

import com.google.gson.annotations.SerializedName;
import com.vincler.jf.projet6.models.googleMapResponse.GeometryResponse;
import com.vincler.jf.projet6.models.googleMapResponse.LocationResponse;

public class SearchResponse {

    public SearchResponse(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @SerializedName("lat")
    public double latitude;

    @SerializedName("lng")
    public double longitude;

    @SerializedName("geometry")
    public GeometryResponse geometryResponse;

    @SerializedName("location")
    public LocationResponse locationResponse;

    @SerializedName("result")
    public ResultSearchResponse resultSearchResponse;

    public double getLatitude() {

        return resultSearchResponse.geometryResponse.locationResponse.latitude;
    }

    public double getLongitude() {

        return resultSearchResponse.geometryResponse.locationResponse.longitude;
    }
}