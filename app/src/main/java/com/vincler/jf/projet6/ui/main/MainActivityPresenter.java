package com.vincler.jf.projet6.ui.main;

import androidx.lifecycle.MutableLiveData;

import com.vincler.jf.projet6.models.Restaurant;

import java.util.ArrayList;

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

        restaurantsData.setValue(result);
    }
}
