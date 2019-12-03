package com.vincler.jf.projet6.ui.search;

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

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<String> search;

    public SearchFragment(ArrayList<String> search) {
        this.search = search;
    }

    public static SearchFragment newInstance(ArrayList<String> searchList) {

        SearchFragment searchFragment = new SearchFragment(searchList);
        Bundle args = new Bundle();
        args.putStringArrayList("search", searchList);
        searchFragment.setArguments(args);

        return searchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            search = getArguments().getStringArrayList("search");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = rootView.findViewById(R.id.fragment_search_recyclerView);



        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    displaySearch();

    }

    public void displaySearch() {

        if (search != null && !search.isEmpty()) {

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter adapter = new ListSearchAdapter(search);
            recyclerView.setAdapter(adapter);
        }

    }


}
