package com.vincler.jf.projet6.ui.map;


import android.app.Activity;
import android.location.LocationListener;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.vincler.jf.projet6.models.Restaurant;

import java.util.ArrayList;

public interface MapFragmentContract {

    interface View {
        MutableLiveData<ArrayList<Restaurant>> getLiveData();

        void updatesMapDisplay(LatLng position);
    }

    interface Presenter extends LocationListener {
        void startLocate(Activity activity);

        void stopLocate();

        void stopFollowUser();

        void searchRestaurants(double latitude, double longitude);

    }
}