package com.vincler.jf.projet6.ui.main;


import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;
import com.vincler.jf.projet6.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public interface MainActivityContract {

    interface View {

    }

    interface Presenter {
        MutableLiveData<ArrayList<Restaurant>> getLiveData();

        void filterRestaurants(String query);

        List<AuthUI.IdpConfig> firebase(FirebaseUser firebaseUser);

        void createUserInFirestore(FirebaseUser firebaseUser);
    }
}