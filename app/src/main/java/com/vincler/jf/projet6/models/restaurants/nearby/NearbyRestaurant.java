package com.vincler.jf.projet6.models.restaurants.nearby;

import android.os.Parcel;
import android.os.Parcelable;

import com.vincler.jf.projet6.utils.ConstantsUtils;

public class NearbyRestaurant implements Parcelable {

    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private String photo;
    private int stars;
    private boolean isVisible;
    private String  isOpenNowList;
    private int workmatesNumber;
    private String placeid;

    public NearbyRestaurant(String name, double latitude, double longitude, String address,
                            String photo, int stars, boolean isVisible, String isOpenNowList,
                            int workmatesNumber, String placeid) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.photo = photo;
        this.stars = stars;
        this.isVisible = isVisible;
        this.isOpenNowList = isOpenNowList;
        this.workmatesNumber = workmatesNumber;
        this.placeid = placeid;
    }

    protected NearbyRestaurant(Parcel in) {
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
        photo = in.readString();
        stars = in.readInt();
        isVisible = in.readByte() != 0;
        isOpenNowList = in.readString();
        workmatesNumber = in.readInt();
        placeid = in.readString();
    }

    public static final Creator<NearbyRestaurant> CREATOR = new Creator<NearbyRestaurant>() {
        @Override
        public NearbyRestaurant createFromParcel(Parcel in) {
            return new NearbyRestaurant(in);
        }

        @Override
        public NearbyRestaurant[] newArray(int size) {
            return new NearbyRestaurant[size];
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

    public int getStars() {
        return stars;
    }

    public int getWorkmatesNumber() {
        return workmatesNumber;
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

    public void setWorkmatesNumber(int workmatesNumber) {
        this.workmatesNumber = workmatesNumber;
    }

    public void setStarsNumber(int stars) {
        this.stars = stars;
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
        dest.writeInt(stars);
        dest.writeByte((byte) (isVisible ? 1 : 0));
        dest.writeString(isOpenNowList);
        dest.writeInt(workmatesNumber);
        dest.writeString(placeid);
    }

    public String getMapsPhotoUrl(){
        return  "https://maps.googleapis.com/maps/api/place/photo?"
                + "maxwidth=" + ConstantsUtils.WIDTH_PHOTO
                + "&photoreference=" + getPhoto()
                + "&key=" + ConstantsUtils.API_KEY;
    }
}
