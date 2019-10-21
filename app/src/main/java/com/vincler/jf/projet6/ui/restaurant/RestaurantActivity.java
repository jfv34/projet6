package com.vincler.jf.projet6.ui.restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vincler.jf.projet6.R;

import java.util.ArrayList;

public class RestaurantActivity extends Activity implements RestaurantActivityContract.View {

    private static final int WIDTH_PHOTO = 200;
    private static final String API_KEY = "AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc";
    private RestaurantActivityContract.Presenter presenter = new RestaurantActivityPresenter(this);
    Context context;
    private String uid;
    private String latLong;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Intent intent = getIntent();
        context = getApplicationContext();

        WorkmatesInRestaurantFragment.newInstance();

        ArrayList<String> restaurant = intent.getStringArrayListExtra("restaurant");

        uid = presenter.getUidFirebase();

        String name = restaurant.get(0);
        String address = restaurant.get(1);
        String photoRef = restaurant.get(2);
        latLong = restaurant.get(3);
        // String phoneNumber = restaurant.get(4);
        String phoneNumber = "0000000000"; //t test

        String url = "https://maps.googleapis.com/maps/api/place/photo?"
                + "maxwidth=" + WIDTH_PHOTO
                + "&photoreference=" + photoRef
                + "&key=" + API_KEY;

        ImageView photo_iv = findViewById(R.id.activity_restaurant_photo_iv);
        TextView name_tv = findViewById(R.id.activity_restaurant_name_tv);
        TextView address_tv = findViewById(R.id.activity_restaurant_address_tv);
        TextView like_tv = findViewById(R.id.activity_restaurant_like_tv);
        ImageView like_iv = findViewById(R.id.activity_restaurant_like_iv);
        TextView call_tv = findViewById(R.id.activity_restaurant_call_tv);
        ImageView call_iv = findViewById(R.id.activity_restaurant_call_iv);
        TextView webSite_tv = findViewById(R.id.activity_restaurant_website_tv);
        ImageView webSite_iv = findViewById(R.id.activity_restaurant_website_iv);


        Glide.with(context).
                load(url).
                into(photo_iv);

        name_tv.setText(name);
        address_tv.setText(address);

        like_iv.setOnClickListener(v -> clickLike());
        like_tv.setOnClickListener(v -> clickLike());
        webSite_iv.setOnClickListener(v -> clickWebSite());
        webSite_tv.setOnClickListener(v -> clickWebSite());
        call_iv.setOnClickListener(v -> clickCall(phoneNumber));
        call_tv.setOnClickListener(v -> clickCall(phoneNumber));

        byte rating = presenter.rating();
    }

    private void clickCall(String phoneNumber) {

        Intent callPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ phoneNumber));
        startActivity(callPhone);
    }

    private void clickWebSite() {

    }

    private void clickLike() {
        presenter.likeRestaurant(uid, latLong);
    }
}
