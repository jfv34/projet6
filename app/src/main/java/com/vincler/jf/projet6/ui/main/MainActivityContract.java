package com.vincler.jf.projet6.ui.main;


import android.text.Editable;

import androidx.lifecycle.MutableLiveData;

import com.google.android.libraries.places.api.net.PlacesClient;
import com.vincler.jf.projet6.models.Search;
import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;

import java.util.ArrayList;
import java.util.List;

public interface MainActivityContract {

    interface View {
        void displayUserInformation(User user);

        void startLogin();

        void updateSearch(ArrayList<Search> searchList);

        void closeKeyboard();

        void displayToolbar();

        void eraseEditText();
    }

    interface Presenter {
        MutableLiveData<ArrayList<NearbyRestaurant>> getLiveData();

        void filterRestaurants(String query);

        void loadUser();

        String getUidFirebase();

        void autocompleteRequest(Editable s, PlacesClient placesClient);

        void search(Search search);

        void clearSearchList();
    }
}