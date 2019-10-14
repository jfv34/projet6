package com.vincler.jf.projet6.ui.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.vincler.jf.projet6.R;

import java.util.ArrayList;

public class RestaurantActivity extends Activity {
    private final int INTENT_CALL_ID = 10012220;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Intent intent = getIntent();
        ArrayList<String> restaurant = intent.getStringArrayListExtra("restaurant");
        Log.i("tag_restaurant", restaurant.get(0));
        Log.i("tag_restaurant", restaurant.get(1));
        Log.i("tag_restaurant", restaurant.get(2));


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_CALL_ID) {
            if (resultCode == RESULT_OK) {

            }
        }
    }


}
