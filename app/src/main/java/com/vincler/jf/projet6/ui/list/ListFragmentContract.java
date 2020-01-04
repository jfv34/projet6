package com.vincler.jf.projet6.ui.list;


import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;

import java.util.ArrayList;

public interface ListFragmentContract {

    interface View {
        void displayRestaurants(ArrayList<NearbyRestaurant> restaurants);
    }

    interface Presenter {

        void setWorkmatesNumbers(ArrayList<NearbyRestaurant> restaurants);

        void setRating(ArrayList<NearbyRestaurant> restaurants);
    }
}