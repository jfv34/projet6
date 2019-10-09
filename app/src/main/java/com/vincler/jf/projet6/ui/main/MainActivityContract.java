package com.vincler.jf.projet6.ui.main;


import androidx.lifecycle.MutableLiveData;

import com.vincler.jf.projet6.models.Restaurant;

import java.util.ArrayList;

public interface MainActivityContract {

    interface View {

    }

    interface Presenter {
        MutableLiveData<ArrayList<Restaurant>> getLiveData();

        void filterRestaurants(String query);
    }
}