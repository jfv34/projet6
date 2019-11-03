package com.vincler.jf.projet6.ui.restaurant;


import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.models.Details;
import com.vincler.jf.projet6.models.Restaurant;

public interface RestaurantActivityContract {

    interface View {
        void displayDetails(Details details);
    }

    interface Presenter  {

        void loadRestaurant();

        void loadUsers(Restaurant restaurant, Context context, RecyclerView recyclerView);

        void likeRestaurant();

        void dislikeRestaurant();

        String getPhoneNumber();

        String getWebSite();

        void notFavoritedRestaurant();

        void favoritedRestaurant();
    }

}