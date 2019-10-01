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

        String[] name = new String[6];
        String[] address = new String[6];

        if (!restaurants.isEmpty()) {

            for (int i = 0; i < name.length; i++) {
                name[i] = restaurants.get(i).getName();
                address[i] = restaurants.get(i).getAddress();
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new ListRestaurantsAdapter(name, address);
            recyclerView.setAdapter(adapter);
        }
    }
}
