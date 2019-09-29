package com.vincler.jf.projet6.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.ListRestaurantsAdapter;
import com.vincler.jf.projet6.R;

public class RestaurantsFragment extends Fragment {

    public static RestaurantsFragment newInstance() {

        return new RestaurantsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] myData = new String[6];
        myData[0] = "text one";
        myData[1] = "text two";
        myData[2] = "text three";
        myData[3] = "text four";
        myData[4] = "text five";
        myData[5] = "text six";

        View rootView = inflater.inflate(R.layout.fragment_restaurants, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_restaurant_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new ListRestaurantsAdapter(myData);
        recyclerView.setAdapter(adapter);

        return rootView;

    }
}


