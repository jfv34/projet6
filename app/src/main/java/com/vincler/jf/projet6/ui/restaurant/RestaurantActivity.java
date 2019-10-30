package com.vincler.jf.projet6.ui.restaurant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.models.Details;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.utils.IntentUtils;

import java.util.ArrayList;

public class RestaurantActivity extends FragmentActivity implements RestaurantActivityContract.View {

    private RestaurantActivityContract.Presenter presenter;

    RecyclerView recyclerView;
    TextView like_tv;
    FloatingActionButton activity_restaurant_fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        recyclerView = findViewById(R.id.fragment_workmatesInRestaurant_recyclerView);

        Intent intent = getIntent();
        Restaurant restaurant = intent.getParcelableExtra("restaurant");

        presenter = new RestaurantActivityPresenter(this, restaurant);

        ImageView photo_iv = findViewById(R.id.activity_restaurant_photo_iv);
        TextView name_tv = findViewById(R.id.activity_restaurant_name_tv);
        TextView address_tv = findViewById(R.id.activity_restaurant_address_tv);
        like_tv = findViewById(R.id.activity_restaurant_like_tv);
        ImageView like_iv = findViewById(R.id.activity_restaurant_like_iv);
        TextView call_tv = findViewById(R.id.activity_restaurant_call_tv);
        ImageView call_iv = findViewById(R.id.activity_restaurant_call_iv);
        TextView webSite_tv = findViewById(R.id.activity_restaurant_website_tv);
        ImageView webSite_iv = findViewById(R.id.activity_restaurant_website_iv);

        activity_restaurant_fab = findViewById(R.id.activity_restaurant_fab);

        Glide.with(this).
                load(restaurant.getMapsPhotoUrl()).
                into(photo_iv);

        name_tv.setText(restaurant.getName());
        address_tv.setText(restaurant.getAddress());

        webSite_iv.setOnClickListener(v -> clickWebSite());
        webSite_tv.setOnClickListener(v -> clickWebSite());

        call_tv.setOnClickListener(v -> clickPhone());
        call_tv.setOnClickListener(v -> clickPhone());


        loadUsers();

        //floatingButton_listener(placeId, restaurantChoice_visible_fab, restaurantChoice_invisible_fab);

        presenter.loadRestaurant();
    }

    //TODO move to presenter - inside the details
    private void loadUsers() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList users = new ArrayList();
        for (int test = 1; test < 30; test++) {
            String testName = "TestName" + test;
            User user = new User("testUid", testName, "testMail", "testPhoneNumber", "", "");
            users.add(user);
        }

        recyclerView.setAdapter(new RestaurantAdapter(users, this));
    }

    private void clickWebSite() {
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(presenter.getWebSite()));
        startActivity(intent);
    }

    @Override
    public void displayDetails(Details details) {
        if (details.isLiked()) {
            like_tv.setText(getApplicationContext().getString(R.string.dislike));
        } else {
            like_tv.setText(getApplicationContext().getString(R.string.like));
        }

        activity_restaurant_fab.setOnClickListener(v -> presenter.likeOrNot(details));

        if(details.isFavorited()){
            activity_restaurant_fab.setImageResource(R.drawable.ic_check_circle_24px);
        }else{
            activity_restaurant_fab.setImageResource(R.drawable.ic_add_24px);
        }

        //TODO display users
    }
}