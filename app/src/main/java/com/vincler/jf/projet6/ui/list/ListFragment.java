package com.vincler.jf.projet6.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;
import com.vincler.jf.projet6.ui.main.MainActivity;

import java.util.ArrayList;

public class ListFragment extends Fragment implements ListFragmentContract.View {

    private RecyclerView recyclerView;
    public ListFragmentContract.Presenter presenter = new ListFragmentPresenter(this);

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_restaurants, container, false);
        recyclerView = rootView.findViewById(R.id.fragment_restaurant_recyclerView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).presenter.getLiveData().observe(this, restaurants
                -> presenter.setWorkmatesByRestaurant(restaurants));
    }

    public void displayRestaurants(ArrayList<NearbyRestaurant> restaurants) {

        if (!restaurants.isEmpty()) {

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new ListRestaurantsAdapter(restaurants);
            recyclerView.setAdapter(adapter);
        }

    }
}