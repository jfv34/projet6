package com.vincler.jf.projet6;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class WorkFragment extends Fragment {

    TextView textView;

    public static WorkFragment newInstance() {

        return new WorkFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_work, container, false);
        textView = rootView.findViewById((R.id.fragment_word_textView));
        textView.setText("This is the Workmates");


        return rootView;


    }

}


