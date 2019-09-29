package com.vincler.jf.projet6.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.ListRestaurantsAdapter;
import com.vincler.jf.projet6.ListWorkmatesAdapter;
import com.vincler.jf.projet6.R;


public class WorkmatesFragment extends Fragment {

    TextView textView;

    public static WorkmatesFragment newInstance() {

        return new WorkmatesFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_workmates, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_workmates_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new ListWorkmatesAdapter(myData);
        recyclerView.setAdapter(adapter);

        return rootView;


    }

}


