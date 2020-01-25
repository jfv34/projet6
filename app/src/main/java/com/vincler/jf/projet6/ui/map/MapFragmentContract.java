package com.vincler.jf.projet6.ui.map;


import android.app.Activity;
import android.content.Context;
import android.location.LocationListener;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;

import java.util.ArrayList;

public interface MapFragmentContract {

    interface View {
        MutableLiveData<ArrayList<NearbyRestaurant>> getLiveData();

        void updatesMapDisplay(LatLng position);
    }

    interface Presenter extends LocationListener {
        void startLocate(Activity activity);

        void stopLocate();

        void stopFollowUser();

        void searchRestaurants(double latitude, double longitude);

        NearbyRestaurant restaurantChosenByClickOnMarker(Marker marker, ArrayList<NearbyRestaurant> data);

        BitmapDescriptor bitmapDescriptorFromVector(Context context, int drawable);
    }
}