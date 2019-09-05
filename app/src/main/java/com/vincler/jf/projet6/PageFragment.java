package com.vincler.jf.projet6;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        int position = getArguments().getInt(KEY_POSITION);
        int fragmentLayout = 0;

        switch (position) {
            case 0:
                fragmentLayout = R.layout.fragment_map;
                break;
            case 1:
                fragmentLayout = R.layout.fragment_list;
                break;
            case 2:
                fragmentLayout = R.layout.fragment_work;
        }
        return inflater.inflate(fragmentLayout, container, false);
    }
}