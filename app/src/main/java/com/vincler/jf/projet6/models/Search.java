package com.vincler.jf.projet6.models;

public class Search {
    private String placeId;
    private String name;

    public Search(String placeId, String name) {
        this.placeId = placeId;
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }
}
