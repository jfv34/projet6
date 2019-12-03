package com.vincler.jf.projet6.ui.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.vincler.jf.projet6.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_preferences);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.preferences_setting,new SettingsFragment())
                .commit();

        Toolbar toolbar = findViewById(R.id.preferences_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.settings);
    }
}
