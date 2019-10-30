package com.vincler.jf.projet6.ui.restaurant;


import com.vincler.jf.projet6.models.Details;

public interface RestaurantActivityContract {

    interface View {
        void displayDetails(Details details);
    }

    interface Presenter  {

        void loadRestaurant();

        void likeRestaurant();

        void dislikeRestaurant();

        String getPhoneNumber();

        String getWebSite();

        boolean likeOrNot(Details details);
    }

}