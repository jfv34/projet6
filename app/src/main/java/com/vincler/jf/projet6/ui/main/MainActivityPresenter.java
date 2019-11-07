package com.vincler.jf.projet6.ui.main;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.View view;

    public MutableLiveData<ArrayList<Restaurant>> restaurantsData = new MutableLiveData<>(new ArrayList<>());

    @Override
    public MutableLiveData<ArrayList<Restaurant>> getLiveData() {
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

                if (u.getResult().getData()== null) {

                    UserFirebase.createUser(firebaseUser.getUid(), firebaseUser.getDisplayName(),
                            firebaseUser.getEmail(),
                            firebaseUser.getPhoneNumber(),
                            firebaseUser.getPhotoUrl().toString()
                    );
                }else Log.i("tag_connect",u.getResult().getData().toString()+"");
            });
        }
    }

    public String getRestaurantChoice() {

        String uid = getUidFirebase();

        final String[] restaurantChoiceId = new String[1];
        restaurantChoiceId[0] ="";
        Task<DocumentSnapshot> data = UserFirebase.getUser(uid);

        data.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (data.getResult() != null) {
                    restaurantChoiceId[0] = data.getResult().getData().get("restaurantChoice").toString();
                    Log.i("tag_restaurantchoice1", restaurantChoiceId[0]);
                }
            }
        });
        Log.i("tag_restaurantchoice2", restaurantChoiceId[0]);
        return restaurantChoiceId[0];
    }

    public String getUidFirebase() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : "";
    }
}
