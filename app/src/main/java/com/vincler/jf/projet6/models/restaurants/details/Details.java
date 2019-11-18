package com.vincler.jf.projet6.models.restaurants.details;

import com.vincler.jf.projet6.utils.ConstantsUtils;

public class Details {
    private String name;
    private String address;
    private String photo;
    private boolean isLiked;
    private boolean isFavorited;
    private String phoneNumber;
    private String webSite;

    public Details(String name, String address, String photo, boolean isLiked, boolean isFavorited, String phoneNumber, String webSite) {
        this.name = name;
        this.address = address;
        this.photo = photo;
        this.isLiked = isLiked;
        this.isFavorited = isFavorited;
        this.phoneNumber = phoneNumber;
        this.webSite = webSite;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoto() {
        return photo;
    }

    public String getMapsPhotoUrl(){
        return  "https://maps.googleapis.com/maps/api/place/photo?"
                + "maxwidth=" + ConstantsUtils.WIDTH_PHOTO
                + "&photoreference=" + getPhoto()
                + "&key=" + ConstantsUtils.API_KEY;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
