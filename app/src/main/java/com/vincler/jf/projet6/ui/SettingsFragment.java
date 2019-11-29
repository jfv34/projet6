package com.vincler.jf.projet6.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.vincler.jf.projet6.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    private SwitchPreference switchPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        switchPreference = getPreferenceScreen().findPreference("notifications");
        if (switchPreference != null) {
            Log.i("tag_settings1", switchPreference.toString());

            switchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                        Log.i("tag_settings2", preference.toString());
                Log.i("tag_settings3", newValue.toString());
                        return true;
                    }

            )
            ;
        }
    }
}