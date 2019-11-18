package com.vincler.jf.projet6.ui.main;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;

import java.util.ArrayList;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.View view;

    public MutableLiveData<ArrayList<NearbyRestaurant>> restaurantsData = new MutableLiveData<>(new ArrayList<>());

    @Override
    public MutableLiveData<ArrayList<NearbyRestaurant>> getLiveData() {
        return restaurantsData;
    }
    public MainActivityPresenter(MainActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void filterRestaurants(String query) {
        ArrayList<NearbyRestaurant> result = new ArrayList<>();
        ArrayList<NearbyRestaurant> restaurants = restaurantsData.getValue();

        if (restaurants != null) {
            for (int i = 0; i < restaurants.size(); i++) {
                String r = restaurants.get(i).getName();
                NearbyRestaurant data = restaurants.get(i);

                result.add(new NearbyRestaurant(
                        data.getName(),
                        data.getLatitude(),
                        data.getLongitude(),
                        data.getAddress(),
                        data.getPhoto(),
                        data.getRating(),
                        r.toLowerCase().contains(query.toLowerCase()),
                        restaurants.get(i).getIsOpenNow(),
                        data.getWorkmatesNumber(),
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

            Task<DocumentSnapshot> u = UserFirebase.getUser(firebaseUser.getUid());
            u.addOnCompleteListener(task -> {

                if (u.getResult().getData()== null) {

                    UserFirebase.createUser(firebaseUser.getUid(), firebaseUser.getDisplayName(),
                            firebaseUser.getEmail(),
                            firebaseUser.getPhoneNumber(),
                            firebaseUser.getPhotoUrl().toString()
                    );
                }
            });
        }
    }

    public String getUidFirebase() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : "";
    }
}
