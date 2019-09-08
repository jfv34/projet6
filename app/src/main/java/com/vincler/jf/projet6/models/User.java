package com.vincler.jf.projet6.models;

import android.net.Uri;

public class User {

    public String name;
    public String mail;
    public Uri photo;

    public User(String name, String mail, Uri photo) {
        this.name = name;
        this.mail = mail;
        this.photo = photo;

    }
}
