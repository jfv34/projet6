package com.vincler.jf.projet6.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;
import com.vincler.jf.projet6.ui.list.ListRestaurantsAdapter;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = rootView.findViewById(R.id.fragment_search_recyclerView);

        displaySearch(null);

        return rootView;
    }

    public void displaySearch(ArrayList<String> search) {

        if (!search.isEmpty()) {

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new ListSearchAdapter(search);
            recyclerView.setAdapter(adapter);
        }

    }


}
