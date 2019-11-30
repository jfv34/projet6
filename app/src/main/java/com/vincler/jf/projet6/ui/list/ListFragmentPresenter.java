package com.vincler.jf.projet6.ui.list;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.api.LikesFirebase;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;

import java.util.ArrayList;

import static com.vincler.jf.projet6.utils.ConstantsUtils.RATE_FOR_ONE_STARS;
import static com.vincler.jf.projet6.utils.ConstantsUtils.RATE_FOR_THREE_STARS;
import static com.vincler.jf.projet6.utils.ConstantsUtils.RATE_FOR_TWO_STARS;

public class ListFragmentPresenter implements ListFragmentContract.Presenter {
    private ListFragmentContract.View view;

    public ListFragmentPresenter(ListFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void setWorkmatesByRestaurant(ArrayList<NearbyRestaurant> restaurants) {
        Task<QuerySnapshot> users = UserFirebase.getUsers();
        users.addOnCompleteListener(task -> {

            int usersSize = users.getResult().size();
            for (int rest = 0; rest < restaurants.size(); rest++) {

                String restaurantId = restaurants.get(rest).getPlaceid();

                int workmatesNumber = 0;
                for (int u = 0; u < usersSize; u++) {
                    String userFavoriteRestau = users.getResult().getDocuments().get(u).getString("restaurantFavoriteId");
                    if (userFavoriteRestau.equals(restaurantId)) {
                        workmatesNumber++;
                    }
                }

                restaurants.get(rest).setWorkmatesNumber(workmatesNumber);

            }
            setRating(restaurants);
        });
    }

    @Override
    public void setRating(ArrayList<NearbyRestaurant> restaurants) {
        Task<QuerySnapshot> likes = LikesFirebase.getLikes();
        likes.addOnCompleteListener(task -> {
            int likeSize = likes.getResult().size();
            for (int rest = 0; rest < restaurants.size(); rest++) {
                String restaurantId = restaurants.get(rest).getPlaceid();
                int likesNumber = 0;
                for (int i = 0; i < likeSize; i++) {
                    String userLikeRestau = likes.getResult().getDocuments().get(i).getString(("restaurant_uid"));
                    if (userLikeRestau.equals(restaurantId)) {
                        likesNumber++;
                    }
                }

                int stars = 0;
                double likeRate = (double) likesNumber / (double) likeSize;

                if (likeRate > RATE_FOR_THREE_STARS) {
                    stars++;
                }
                if (likeRate > RATE_FOR_TWO_STARS) {
                    stars++;
                }
                if (likeRate > RATE_FOR_ONE_STARS) {
                    stars++;
                }

                restaurants.get(rest).setStarsNumber(stars);
            }
            view.displayRestaurants(restaurants);
        }

        );
    }
}