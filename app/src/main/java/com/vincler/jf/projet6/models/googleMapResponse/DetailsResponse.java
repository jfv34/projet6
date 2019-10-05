package com.vincler.jf.projet6.models.googleMapResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DetailsResponse {

    public DetailsResponse(ArrayList weekday_textList) {
        this.weekday_textList = weekday_textList;
    }

    @SerializedName("weekday_text")
    public ArrayList weekday_textList;

    @SerializedName("opening_hours")
    public Open_hoursResponse opening_hoursResponse;

    public ArrayList getOpening_hours() {

        return opening_hoursResponse.weekday_textList;
    }
}