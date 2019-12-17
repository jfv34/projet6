package com.vincler.jf.projet6.models.restaurants.details;

import com.vincler.jf.projet6.KeyAPI;
import com.vincler.jf.projet6.utils.ConstantsUtils;

public class Details {
    private String name;
    private String address;
    private String photo;
    private boolean isLiked;
    private boolean isFavorited;
    private String phoneNumber;
    private String webSite;
    private int stars;

    public Details(String name, String address, String photo, boolean isLiked, boolean isFavorited,
                   String phoneNumber, String webSite, int stars) {
        this.name = name;
        this.address = address;
        this.photo = photo;
        this.isLiked = isLiked;
        this.isFavorited = isFavorited;
        this.phoneNumber = phoneNumber;
        this.webSite = webSite;
        this.stars = stars;
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
                + "&key=" + KeyAPI.API_KEY;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebSite() {
        return webSite;
    }

    public int getStars() {
        return stars;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public boolean isLiked() {
        return isLiked;
    }

}