package com.vincler.jf.projet6.ui.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.R;


public class WorkmatesFragment extends Fragment {

    public static WorkmatesFragment newInstance() {

        return new WorkmatesFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_workmates, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_workmates_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new ListWorkmatesAdapter(data);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        return rootView;

    }
}


