package com.vincler.jf.projet6.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.vincler.jf.projet6.R;


public class WorkmatesFragment extends Fragment {

    TextView textView;

    public static WorkmatesFragment newInstance() {

        return new WorkmatesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_workmates, container, false);


        return rootView;


    }

}


