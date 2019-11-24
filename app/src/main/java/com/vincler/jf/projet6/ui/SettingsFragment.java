package com.vincler.jf.projet6.ui;

import android.os.Bundle;

import androidx.core.view.LayoutInflaterCompat;
import androidx.preference.PreferenceFragmentCompat;

import com.vincler.jf.projet6.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
