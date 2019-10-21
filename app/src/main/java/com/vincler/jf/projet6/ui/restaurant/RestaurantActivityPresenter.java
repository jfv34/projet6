package com.vincler.jf.projet6.ui.restaurant;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vincler.jf.projet6.api.LikesFirebase;

public class RestaurantActivityPresenter implements RestaurantActivityContract.Presenter {

    private RestaurantActivityContract.View view;

    public RestaurantActivityPresenter(RestaurantActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void likeRestaurant(String user_uid, String restaurant_uid) {

        LikesFirebase.createLike(user_uid, restaurant_uid);
        Log.i("tag_uid", user_uid);
        Log.i("tag_latlongRestaurant", restaurant_uid);
    }

    @Override
    public String getUidFirebase() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : "";
    }

    @Override
    public byte rating() {

        //String t = Objects.requireNonNull(LikesFirebase.getLike());

        //Log.i("tag_document",t);

        return 0;
    }
}