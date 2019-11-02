package com.vincler.jf.projet6.ui.main;

import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.models.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    public MutableLiveData<ArrayList<Restaurant>> restaurantsData = new MutableLiveData<>(new ArrayList<>());

    @Override
    public MutableLiveData<ArrayList<Restaurant>> getLiveData() {
        return restaurantsData;
    }

    private MainActivityContract.View view;

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
    public List<AuthUI.IdpConfig> firebase(FirebaseUser user) {

        return Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()
        );
    }

    @Override
    public void createUserInFirestore(FirebaseUser firebaseUser) {

        String restaurantChoice = "";
        String restaurantName = "";

        UserFirebase.createUser(firebaseUser.getUid(), firebaseUser.getDisplayName(),
                firebaseUser.getEmail(),
                firebaseUser.getPhoneNumber(),
                restaurantChoice,
                restaurantName,
                firebaseUser.getPhotoUrl().toString()
        );
    }



    public MutableLiveData<ArrayList<Restaurant>> getRestaurantsData() {
        return restaurantsData;
    }
}
