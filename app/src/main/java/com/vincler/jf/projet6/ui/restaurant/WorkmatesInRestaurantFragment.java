package com.vincler.jf.projet6.ui.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.R;


public class WorkmatesInRestaurantFragment extends Fragment {

    public static WorkmatesInRestaurantFragment newInstance() {

        return new WorkmatesInRestaurantFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] data = new String[6];
        data[0] = "text one";
        data[1] = "text two";
        data[2] = "text three";
        data[3] = "text four";
        data[4] = "text five";
        data[5] = "text six";

        View rootView = inflater.inflate(R.layout.activity_restaurant, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_workmatesInRestaurant_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new RestaurantAdapter(data);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        return rootView;

    }
}


