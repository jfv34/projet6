package com.vincler.jf.projet6.models;

import java.util.ArrayList;

public class DetailRestaurant {

    private int restaurantId;
    private ArrayList<String> daysList;

    public DetailRestaurant(int restaurantId, ArrayList<String> daysList) {
        this.restaurantId = restaurantId;
        this.daysList = daysList;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public ArrayList getDaysList() {
        return daysList;
    }

}
