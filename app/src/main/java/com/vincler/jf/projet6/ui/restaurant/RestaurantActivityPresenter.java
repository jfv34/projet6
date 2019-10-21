package com.vincler.jf.projet6.ui.restaurant;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vincler.jf.projet6.api.LikeFirebase;

import java.util.Objects;

public class RestaurantActivityPresenter implements RestaurantActivityContract.Presenter {

    private RestaurantActivityContract.View view;

    public RestaurantActivityPresenter(RestaurantActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void likeRestaurant(String uid, String latLongRestaurant) {

        LikeFirebase.createLike(uid, latLongRestaurant);
        Log.i("tag_uid", uid);
        Log.i("tag_latlongRestaurant", latLongRestaurant);
    }

    @Override
    public String getUidFirebase() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : "";
    }

    @Override
    public byte rating() {

        //String t = Objects.requireNonNull(LikeFirebase.getLike());

        //Log.i("tag_document",t);

        return 0;
    }
}