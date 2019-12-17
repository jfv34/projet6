package com.vincler.jf.projet6.ui.restaurant;


import com.vincler.jf.projet6.models.restaurants.details.Details;
import com.vincler.jf.projet6.models.User;

import java.util.ArrayList;

public interface RestaurantActivityContract {

    interface View {
        void displayDetails(Details details);

        void displayFavorite(boolean isFavorited);

        void displayLike(boolean isLiked);

        void displayUsers(ArrayList<User> users);

        void displayRestaurant(Details details);

        void displayLoader(Boolean isLoading);
    }

    interface Presenter  {

        void loadDetails();

        void loadUsers();

        void toggleLike();

        String getPhoneNumber();

        String getWebSite();

        void toggleFavorite();
    }

}