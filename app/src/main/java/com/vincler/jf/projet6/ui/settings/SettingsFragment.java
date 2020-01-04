package com.vincler.jf.projet6.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.utils.ConstantsUtils;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                ConstantsUtils.SHAREDPREFERENCES_SETTINGS, Context.MODE_PRIVATE);
        SwitchPreference switchPreference = getPreferenceScreen().findPreference("notifications");
        if (switchPreference != null) {
            switchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                sharedPref.edit().putBoolean("notifications", (Boolean) newValue).apply();
                Log.i("tag_setting_apply",newValue.toString());
                        return true;
                    }
            );
        }
    }
}