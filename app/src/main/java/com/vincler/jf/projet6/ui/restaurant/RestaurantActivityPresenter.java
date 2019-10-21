package com.vincler.jf.projet6.ui.restaurant;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vincler.jf.projet6.api.RestaurantLikeFirebase;

public class RestaurantActivityPresenter implements RestaurantActivityContract.Presenter {

    private RestaurantActivityContract.View view;

    public RestaurantActivityPresenter(RestaurantActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void likeRestaurant(String uid, String latLongRestaurant) {
        Log.i("uid: ", uid);
        Log.i("latlong: ", latLongRestaurant);
        RestaurantLikeFirebase.createRestaurant(uid, latLongRestaurant);


    }

    @Override
    public String getUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : "";
    }
}