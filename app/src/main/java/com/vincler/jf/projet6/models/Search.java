package com.vincler.jf.projet6.models;

public class Search {
    private String placeId;
    private String restaurantName;

    public Search(String placeId, String restaurantName) {
        this.placeId = placeId;
        this.restaurantName = restaurantName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
}
