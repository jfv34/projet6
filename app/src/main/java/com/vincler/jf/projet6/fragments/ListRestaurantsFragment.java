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

public class ListRestaurantsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    public static ListRestaurantsFragment newInstance() {

        return new ListRestaurantsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] myData = new String[3];
        myData[0] = "premier texte";
        myData[1] = "deuxième texte";
        myData[2] = "troisième texte";


        View rootView = inflater.inflate(R.layout.fragment_listrestaurants, container, false);
        recyclerView = rootView.findViewById(R.id.listRestaurants_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ListRestaurantsAdapter(myData);
        recyclerView.setAdapter(adapter);


        return rootView;


    }
}


