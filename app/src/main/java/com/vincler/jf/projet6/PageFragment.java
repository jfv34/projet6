package com.vincler.jf.projet6;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PageFragment extends Fragment {

    private static final String KEY_POSITION = "position";

    public PageFragment() {
    }

    public static PageFragment newInstance(int position) {

        PageFragment fragment = new PageFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        fragment.setArguments(args);
        return (fragment);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentview = inflater.inflate(R.layout.fragment_page, container, false);
        TextView textView = fragmentview.findViewById(R.id.fragment_page_textview_test);
        int position = getArguments().getInt(KEY_POSITION);
        textView.setText("Page numéro " + position);


        return fragmentview;
    }

}