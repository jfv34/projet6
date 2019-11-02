package com.vincler.jf.projet6.ui.restaurant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.models.Details;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.utils.IntentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantActivity extends FragmentActivity implements RestaurantActivityContract.View {

    private RestaurantActivityContract.Presenter presenter;

    RecyclerView recyclerView;
    TextView address_tv;
    TextView name_tv;
    ImageView photo_iv;
    TextView like_tv;
    ImageView like_iv;
    TextView webSite_tv;
    ImageView webSite_iv;
    TextView call_tv;
    ImageView call_iv;
    FloatingActionButton favorite_fab;
    boolean isFavorited = false;
    boolean isLiked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        recyclerView = findViewById(R.id.fragment_workmatesInRestaurant_recyclerView);

        Intent intent = getIntent();
        Restaurant restaurant = intent.getParcelableExtra("restaurant");

        presenter = new RestaurantActivityPresenter(this, restaurant);

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

        favorite_fab.setImageResource(R.drawable.ic_update_24px);

        Glide.with(this).
                load(restaurant.getMapsPhotoUrl()).
                into(photo_iv);

        name_tv.setText(restaurant.getName());
        address_tv.setText(restaurant.getAddress());

        loadUsers(restaurant);
        presenter.loadRestaurant();
    }

    //TODO move to presenter - inside the details
    private void loadUsers(Restaurant restaurant) {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<User> users = new ArrayList<>();
        List<HashMap> result = new ArrayList<>();
        Task<QuerySnapshot> data = UserFirebase.getUsersByRestaurantChoice(restaurant.getPlaceid());
        data.addOnCompleteListener(task -> {
            if (data.getResult() != null) {

                for (int i = 0; i < data.getResult().size(); i++) {
                    HashMap ii = (HashMap) data.getResult().getDocuments().get(i).getData();
                    Log.i("restaurantChoice P ", ii.toString()+"");
                    result.add(ii);
                }
                Log.i("restaurantChoice T ", result.get(0).get("uid").toString());


                for (int test = 0; test < result.size(); test++) {
                    HashMap tt = result.get(test);
                    User user = new User(
                            tt.get("uid").toString(),
                            tt.get("username").toString(),
                            tt.get("email").toString(),
                            tt.get("phoneNumber").toString(),
                            tt.get("restaurantChoice").toString(),
                            tt.get("photoUserUrl").toString());

                    users.add(user);
                    recyclerView.setAdapter(new RestaurantAdapter(users, this));
                }



            }
        });







    }

    private void clickWebSite() {
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(presenter.getWebSite()));
        startActivity(intent);
    }

    @Override
    public void displayDetails(Details details) {

        listener_webSiteButton();
        listener_callButton();
        diplay_likeOrNot(details);
        listener_likeOrNot();
        display_isfavoritedOrNot_fab(details);
        listener_isfavoritedOrNot_fab();

        //TODO display users
    }

    private void listener_isfavoritedOrNot_fab() {
        favorite_fab.setOnClickListener(v -> {
            clickFavoriteOrNot();
        });
    }

    private void display_isfavoritedOrNot_fab(Details details) {
        if (details.isFavorited()) {
            favorite_fab.setImageResource(R.drawable.ic_check_circle_24px);
            isFavorited = true;
        } else {
            favorite_fab.setImageResource(R.drawable.ic_add_24px);
            isFavorited = false;
        }

    }

    private void listener_likeOrNot() {
        like_tv.setOnClickListener(v -> {
            clickLikeOrDislike();
        });

        like_iv.setOnClickListener(v -> {
            clickLikeOrDislike();
        });

    }

    private void diplay_likeOrNot(Details details) {
        if (details.isLiked()) {
            like_tv.setText(getApplicationContext().getString(R.string.dislike));
            isLiked=false;
        } else {
            like_tv.setText(getApplicationContext().getString(R.string.like));
            isLiked=true;
        }
    }

    private void listener_callButton() {
        call_iv.setOnClickListener(v -> IntentUtils.callNumber(this, presenter.getPhoneNumber()));
        call_tv.setOnClickListener(v -> IntentUtils.callNumber(this, presenter.getPhoneNumber()));

    }

    private void listener_webSiteButton() {
        webSite_iv.setOnClickListener(v -> clickWebSite());
        webSite_tv.setOnClickListener(v -> clickWebSite());
    }


    private void clickFavoriteOrNot() {
        if (isFavorited) {
            isFavorited = false;
            favorite_fab.setImageResource(R.drawable.ic_add_24px);
            presenter.notFavoritedRestaurant();
        } else {
            isFavorited = true;
            favorite_fab.setImageResource(R.drawable.ic_check_circle_24px);
            presenter.favoritedRestaurant();
        }
    }


    private void clickLikeOrDislike() {
        if (isLiked) {
            isLiked = false;
            like_tv.setText(getApplicationContext().getString(R.string.dislike));
            presenter.likeRestaurant();
            Log.i("tag_like", "dislike");
        } else {
            isLiked = true;
            like_tv.setText(getApplicationContext().getString(R.string.like));
            presenter.dislikeRestaurant();
            Log.i("tag_like", "like");
        }
    }
}