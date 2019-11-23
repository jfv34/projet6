package com.vincler.jf.projet6.ui.restaurant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.models.restaurants.details.Details;
import com.vincler.jf.projet6.utils.IntentUtils;

import java.util.ArrayList;

public class RestaurantActivity extends FragmentActivity implements RestaurantActivityContract.View {

    private RestaurantActivityContract.Presenter presenter;

    private RecyclerView recyclerView;
    private TextView address_tv;
    private TextView name_tv;
    private ImageView photo_iv;
    private TextView like_tv;
    private ImageView like_iv;
    private TextView webSite_tv;
    private ImageView webSite_iv;
    private TextView call_tv;
    private ImageView call_iv;
    private FloatingActionButton favorite_fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        recyclerView = findViewById(R.id.fragment_workmatesInRestaurant_recyclerView);
        photo_iv = findViewById(R.id.activity_restaurant_photo_iv);
        name_tv = findViewById(R.id.activity_restaurant_name_tv);
        address_tv = findViewById(R.id.activity_restaurant_address_tv);
        like_tv = findViewById(R.id.activity_restaurant_like_tv);
        like_iv = findViewById(R.id.activity_restaurant_like_iv);
        favorite_fab = findViewById(R.id.activity_restaurant_fab);
        call_tv = findViewById(R.id.activity_restaurant_call_tv);
        call_iv = findViewById(R.id.activity_restaurant_call_iv);
        webSite_tv = findViewById(R.id.activity_restaurant_website_tv);
        webSite_iv = findViewById(R.id.activity_restaurant_website_iv);

        //NestedScrollView nestedScrollView = findViewById(R.id.activity_restaurant_nestedScrollView);

     /*   nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.i("tag_scroll x ", scrollX + "");
                Log.i("tag_scroll old x ", oldScrollX + "");
                Log.i("tag_scroll y ", scrollY + "");
                Log.i("tag_scroll old y ", oldScrollY + "");
            }});
*/
        presenter = new RestaurantActivityPresenter(
                this,this,
                getIntent().getStringExtra("restaurantDisplayedId"),
                getIntent().getIntExtra("restaurantStars", 0)
        );
        presenter.loadDetails();
        presenter.loadUsers();
    }


    private void clickWebSite() {
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(presenter.getWebSite()));
        startActivity(intent);
    }

    @Override
    public void displayRestaurant(Details details) {

        if (details != null) {
            name_tv.setText(details.getName());
            address_tv.setText(details.getAddress());

            Glide.with(this)
                    .load(details.getMapsPhotoUrl())
                    .into(photo_iv);
        }
    }

    @Override
    public void displayDetails(Details details) {
        webSite_iv.setOnClickListener(v -> clickWebSite());
        webSite_tv.setOnClickListener(v -> clickWebSite());

        call_iv.setOnClickListener(v -> IntentUtils.callNumber(this, presenter.getPhoneNumber()));
        call_tv.setOnClickListener(v -> IntentUtils.callNumber(this, presenter.getPhoneNumber()));

        like_tv.setOnClickListener(v -> presenter.toggleLike());
        like_iv.setOnClickListener(v -> presenter.toggleLike());

        favorite_fab.setOnClickListener(v -> presenter.toggleFavorite());

        displayLike(details.isLiked());
        displayFavorite(details.isFavorited());
        displayStars(details.getStars());
    }

    private void displayStars(int stars) {
        ImageView star1_iv = findViewById(R.id.activity_restaurant_star1_iv);
        ImageView star2_iv = findViewById(R.id.activity_restaurant_star2_iv);
        ImageView star3_iv = findViewById(R.id.activity_restaurant_star3_iv);

        star1_iv.setVisibility(View.INVISIBLE);
        star2_iv.setVisibility(View.INVISIBLE);
        star3_iv.setVisibility(View.INVISIBLE);

        switch (stars) {
            case 3:
                star3_iv.setVisibility(View.VISIBLE);
            case 2:
                star2_iv.setVisibility(View.VISIBLE);
            case 1:
                star1_iv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayFavorite(boolean isFavorited) {
        if (isFavorited) {
            favorite_fab.setImageResource(R.drawable.ic_check_circle_24px);
        } else {
            favorite_fab.setImageResource(R.drawable.ic_add_24px);
        }
    }

    @Override
    public void displayLike(boolean isLiked) {
        if (isLiked) {
            like_tv.setText(getString(R.string.dislike));
        } else {
            like_tv.setText(getString(R.string.like));
        }
    }

    @Override
    public void displayUsers(ArrayList<User> users) {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RestaurantAdapter(users, this));
    }
}