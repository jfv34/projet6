package com.vincler.jf.projet6.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.ListRestaurantsAdapter;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.activities.MainActivity;
import com.vincler.jf.projet6.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    RecyclerView recyclerView;

    public static ListFragment newInstance() {

        return new ListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_restaurants, container, false);
        recyclerView = rootView.findViewById(R.id.fragment_restaurant_recyclerView);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).restaurantsData.observe(this, restaurants -> {
            displayRestaurants(restaurants);
        });
    }

    private void displayRestaurants(ArrayList<Restaurant> restaurants) {

        List<String> name = new ArrayList<>();
        List<String> address = new ArrayList<>();
        List<String> photo = new ArrayList<>();
        List<Double> rating = new ArrayList<>();
        List<Double> latitude = new ArrayList<>();
        List<Double> longitude = new ArrayList<>();

        if (!restaurants.isEmpty()) {

            for (int i = 0; i < restaurants.size(); i++) {
                name.add(restaurants.get(i).getName());
                address.add(restaurants.get(i).getAddress());
                photo.add(restaurants.get(i).getPhoto());
                rating.add(restaurants.get(i).getRating());
                latitude.add(restaurants.get(i).getLatitude());
                longitude.add(restaurants.get(i).getLongitude());
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);


            RecyclerView.Adapter adapter = new ListRestaurantsAdapter(name, address, photo,
                    rating, latitude, longitude);
            recyclerView.setAdapter(adapter);
        }
    }

    /*private void getDataDetails(Response<ListDetailsResponse> response) {

        ArrayList newDetails = new ArrayList();
        Log.i("tag_newDetails", "getDataDetail");

        int sizeDetailsData = response.body().results.size();

        ArrayList<DetailRestaurant> detailRestaurantList = new ArrayList<DetailRestaurant>();
        for (int restaurantId = 0; restaurantId < sizeDetailsData; restaurantId++) {

            ArrayList<String> daysList = new ArrayList<>();
            for (int day = 0; day < 6; day++) {
                String opening_of_this_day = response.body().getResults().get(restaurantId)
                        .getOpening_hours().get(day).toString();
                daysList.set(day, opening_of_this_day);
            }
            detailRestaurantList.set(restaurantId,daysList);

                Log.i("tag_response_name", restaurant.getName());
                Log.i("tag_response_lat", String.valueOf(restaurant.getLatitude()));
                Log.i("tag_response_long", String.valueOf(restaurant.getLongitude()));
                Log.i("tag_response_addres", String.valueOf(restaurant.getAddress()));
                Log.i("tag_response_photo", String.valueOf(restaurant.getPhoto()));
                Log.i("tag_response_search", String.valueOf(restaurant.getSearchStatus()));
            }


            restaurantsData.setValue(newRestaurants);


        }*/
}
