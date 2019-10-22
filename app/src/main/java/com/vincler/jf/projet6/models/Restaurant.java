package com.vincler.jf.projet6.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.vincler.jf.projet6.utils.KeysUtils;

public class Restaurant implements Parcelable {

    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private String photo;
    private double rating;
    private boolean isVisible;
    private String  isOpenNowList;
    private String placeid;

    public Restaurant(String name, double latitude, double longitude, String address, String photo,
                      Double rating, boolean isVisible, String isOpenNowList, String placeid) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.photo = photo;
        this.rating = rating;
        this.isVisible = isVisible;
        this.isOpenNowList = isOpenNowList;
        this.placeid = placeid;
    }

    protected Restaurant(Parcel in) {
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
        photo = in.readString();
        rating = in.readDouble();
        isVisible = in.readByte() != 0;
        isOpenNowList = in.readString();
        placeid = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoto() {
        return photo;
    }

    public double getRating() {
        return rating;
    }

    public String getPlaceid() {
        return placeid;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public String getIsOpenNow() {
        return isOpenNowList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(address);
        dest.writeString(photo);
        dest.writeDouble(rating);
        dest.writeByte((byte) (isVisible ? 1 : 0));
        dest.writeString(isOpenNowList);
        dest.writeString(placeid);
    }

    public String getMapsPhotoUrl(){
        return  "https://maps.googleapis.com/maps/api/place/photo?"
                + "maxwidth=" + KeysUtils.WIDTH_PHOTO
                + "&photoreference=" + getPhoto()
                + "&key=" + KeysUtils.API_KEY;
    }
}
