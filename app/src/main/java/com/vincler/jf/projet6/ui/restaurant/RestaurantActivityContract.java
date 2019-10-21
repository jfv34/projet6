package com.vincler.jf.projet6.ui.restaurant;



public interface RestaurantActivityContract {

    interface View {

    }

    interface Presenter  {

        void likeRestaurant(String uid, String latLongRestaurant);

        String getUidFirebase();

        byte rating();
    }
}