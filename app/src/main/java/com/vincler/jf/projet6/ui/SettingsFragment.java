package com.vincler.jf.projet6.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.vincler.jf.projet6.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    private ListPreference listPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        listPreference = getPreferenceManager().findPreference("preference_key");
        if (listPreference != null) {
            listPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                Log.i("tag_preferences",listPreference.toString());
                Log.i("tag_preferences",listPreference.getValue().toString());
                Log.i("tag_preferences",listPreference.getEntry().toString());
                return false;
            });
        }

        return inflater.inflate(R.layout.preferences, container, false);
    }
}