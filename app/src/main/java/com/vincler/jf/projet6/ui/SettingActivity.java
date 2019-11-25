package com.vincler.jf.projet6.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.vincler.jf.projet6.R;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        getSupportFragmentManager()
                .beginTransaction()
                .attach(new SettingsFragment())
                .commit();
    }
}
