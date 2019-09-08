package com.vincler.jf.projet6.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.vincler.jf.projet6.R;

public class ListFragment extends Fragment {

    TextView textView;

    public static ListFragment newInstance() {

        return new ListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        textView = rootView.findViewById((R.id.fragment_list_textView));
        textView.setText("This is the ListView");


        return rootView;


    }
}


