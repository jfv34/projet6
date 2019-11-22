package com.vincler.jf.projet6.ui.list;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;

import java.util.ArrayList;

public class ListFragmentPresenter implements ListFragmentContract.Presenter {
    private ListFragmentContract.View view;

    public ListFragmentPresenter(ListFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void setWorkmatesByRestaurant(ArrayList<NearbyRestaurant> restaurants) {
        Task<QuerySnapshot> users = UserFirebase.getUsers();
        users.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                int usersSize = users.getResult().size();
                for (int rest = 0; rest < restaurants.size(); rest++) {

                    String restaurantId = restaurants.get(rest).getPlaceid();

                    int workmatesNumbers = 0;
                    for (int u = 0; u < usersSize; u++) {
                        String userFavoriteRestau = users.getResult().getDocuments().get(u).getData().get("restaurantFavoriteId").toString();
                        if (userFavoriteRestau.equals(restaurantId)) {
                            workmatesNumbers++;
                        }
                    }

                    restaurants.get(rest).setWorkmatesNumber(workmatesNumbers);
                    view.displayRestaurants(restaurants);
                }
            }
        });
    }
}