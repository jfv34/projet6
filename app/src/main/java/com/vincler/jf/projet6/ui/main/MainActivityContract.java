package com.vincler.jf.projet6.ui.main;


import androidx.lifecycle.MutableLiveData;

import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;

import java.util.ArrayList;

public interface MainActivityContract {

    interface View {
        void displayUserInformation(User user);

        void startLogin();

    }

    interface Presenter {
        MutableLiveData<ArrayList<NearbyRestaurant>> getLiveData();

        void filterRestaurants(String query);

        void loadUser();

        String getUidFirebase();
    }
}