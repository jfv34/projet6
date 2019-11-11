package com.vincler.jf.projet6.notifications;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.vincler.jf.projet6.ui.main.MainActivity;

public class NotificationsActivity extends MainActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    NotificationsActivityPresenter presenter = new NotificationsActivityPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String KEY_PREF_NOTIFICATIONS = "key_pref_notifications";
        sharedPreferences = getApplicationContext().getSharedPreferences(KEY_PREF_NOTIFICATIONS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}
