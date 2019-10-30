package com.vincler.jf.projet6.models;

public class Details {
    private boolean isLiked;
    private boolean isFavorited;
    private String phoneNumber;
    private String webSite;

    public Details(boolean isLiked, boolean isFavorited, String phoneNumber, String webSite) {
        this.isLiked = isLiked;
        this.isFavorited = isFavorited;
        this.phoneNumber = phoneNumber;
        this.webSite = webSite;
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
