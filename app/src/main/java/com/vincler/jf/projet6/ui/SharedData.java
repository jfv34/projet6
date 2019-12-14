package com.vincler.jf.projet6.ui;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;

public class SharedData {
    public static MutableLiveData<String> favoritedRestaurant = new MutableLiveData<>();
    public static MutableLiveData<LatLng> latlngMap = new MutableLiveData<>();
}

