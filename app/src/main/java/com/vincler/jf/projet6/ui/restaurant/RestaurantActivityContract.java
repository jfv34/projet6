package com.vincler.jf.projet6.ui.restaurant;


import com.vincler.jf.projet6.models.Details;

public interface RestaurantActivityContract {

    interface View {

    }

    interface Presenter  {

        void likeRestaurant(String uid, String latLongRestaurant);

        String getUidFirebase();

        byte rating();

        Details retrofit(String placeid);
    }
}