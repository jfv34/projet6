package com.vincler.jf.projet6.ui.main;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.models.Restaurant;

import java.util.ArrayList;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.View view;

    public MutableLiveData<ArrayList<Restaurant>> restaurantsData = new MutableLiveData<>(new ArrayList<>());

    @Override
    public MutableLiveData<ArrayList<Restaurant>> getLiveData() {
        return restaurantsData;
    }
    public MutableLiveData<ArrayList<Restaurant>> getRestaurantsData() {
        return restaurantsData;
    }

    public MainActivityPresenter(MainActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void filterRestaurants(String query) {
        ArrayList<Restaurant> result = new ArrayList<>();
        ArrayList<Restaurant> restaurants = restaurantsData.getValue();

        if (restaurants != null) {
            for (int i = 0; i < restaurants.size(); i++) {
                String r = restaurants.get(i).getName();
                Restaurant data = restaurants.get(i);

                result.add(new Restaurant(
                        data.getName(),
                        data.getLatitude(),
                        data.getLongitude(),
                        data.getAddress(),
                        data.getPhoto(),
                        data.getRating(),
                        r.toLowerCase().contains(query.toLowerCase()),
                        restaurants.get(i).getIsOpenNow(),
                        data.getPlaceid()
                ));
            }
        }

        restaurantsData.setValue(result);
    }

    @Override
    public void loadUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null) {
            view.startLogin();
        } else {
            view.displayUserInformation(firebaseUser);

            //TODO CHECK IF USER DOES NOT EXIST ALREADY
            Task<DocumentSnapshot> u = UserFirebase.getUser(firebaseUser.getUid());
            u.addOnCompleteListener(task -> {
                if (!u.isSuccessful() || u.getResult().get("doc") == null) {

                    UserFirebase.createUser(firebaseUser.getUid(), firebaseUser.getDisplayName(),
                            firebaseUser.getEmail(),
                            firebaseUser.getPhoneNumber(),
                            firebaseUser.getPhotoUrl().toString()
                    );
                }
            });
        }
    }

    public String getRestaurantChoice(String uid) {
        Object restaurantChoice = UserFirebase.getUser(uid).getResult().get("restaurantChoice");
        return restaurantChoice != null? restaurantChoice.toString() : "";
    }

    public String getUidFirebase() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : "";
    }
}
