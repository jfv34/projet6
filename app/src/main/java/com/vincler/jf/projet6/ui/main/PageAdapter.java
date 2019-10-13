package com.vincler.jf.projet6.ui.main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vincler.jf.projet6.ui.list.ListFragment;
import com.vincler.jf.projet6.ui.map.MapFragment;
import com.vincler.jf.projet6.ui.workmates.WorkmatesFragment;

public class PageAdapter extends FragmentPagerAdapter {

    private static final int NUMBER_OF_PAGES = 3;

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return NUMBER_OF_PAGES;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MapFragment.newInstance();
            case 1:
                return ListFragment.newInstance();
            case 2:
                return WorkmatesFragment.newInstance();
            default:
                return null;
        }
    }
}