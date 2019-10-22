package com.vincler.jf.projet6.models;

public class Details {
    private String phoneNumber;
    private String webSite;

    public Details(String phoneNumber, String webSite) {
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
}
