package com.vincler.jf.projet6.ui.restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vincler.jf.projet6.R;

import java.util.ArrayList;

public class RestaurantActivity extends Activity {

    private static final int WIDTH_PHOTO = 200;
    private static final String API_KEY = "AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc";
    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Intent intent = getIntent();
        context = getApplicationContext();

        ArrayList<String> restaurant = intent.getStringArrayListExtra("restaurant");
        String name = restaurant.get(0);
        String address = restaurant.get(1);
        String photoRef = restaurant.get(2);
        String url = "https://maps.googleapis.com/maps/api/place/photo?"
                + "maxwidth=" + WIDTH_PHOTO
                + "&photoreference=" + photoRef
                + "&key=" + API_KEY;

        ImageView photo_iv = findViewById(R.id.activity_restaurant_photo_iv);
        TextView name_tv = findViewById(R.id.activity_restaurant_name_tv);
        TextView address_tv = findViewById(R.id.activity_restaurant_address_tv);


        Glide.with(context).load(url).into(photo_iv);
        name_tv.setText(name);
        address_tv.setText(address);
    }
}
