package com.vincler.jf.projet6.ui.restaurant;

import android.content.Context;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.api.LunchesFirebase;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.models.googleMapResponse.DetailsResponse;
import com.vincler.jf.projet6.utils.IntentUtils;
import com.vincler.jf.projet6.utils.UnsafeOkHttpClient;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantActivity extends FragmentActivity implements RestaurantActivityContract.View {

    private RestaurantActivityContract.Presenter presenter = new RestaurantActivityPresenter(this);
    String phoneNumber;
    String webSite;
    RecyclerView recyclerView;
    boolean visibleIconInFAB = false;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid = firebaseUser != null ? firebaseUser.getUid() : null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        recyclerView=findViewById(R.id.fragment_workmatesInRestaurant_recyclerView);

        Intent intent = getIntent();
        Restaurant restaurant = intent.getParcelableExtra("restaurant");

        ImageView photo_iv = findViewById(R.id.activity_restaurant_photo_iv);
        TextView name_tv = findViewById(R.id.activity_restaurant_name_tv);
        TextView address_tv = findViewById(R.id.activity_restaurant_address_tv);
        TextView like_tv = findViewById(R.id.activity_restaurant_like_tv);
        ImageView like_iv = findViewById(R.id.activity_restaurant_like_iv);
        TextView call_tv = findViewById(R.id.activity_restaurant_call_tv);
        ImageView call_iv = findViewById(R.id.activity_restaurant_call_iv);
        TextView webSite_tv = findViewById(R.id.activity_restaurant_website_tv);
        ImageView webSite_iv = findViewById(R.id.activity_restaurant_website_iv);
        FloatingActionButton restaurantChoice_visible_fab = findViewById(R.id.activity_restaurant_visibleIcon_fab);
        FloatingActionButton restaurantChoice_invisible_fab = findViewById(R.id.activity_restaurant_invisibleIcon_fab);

        Glide.with(this).
                load(restaurant.getMapsPhotoUrl()).
                into(photo_iv);

        name_tv.setText(restaurant.getName());
        address_tv.setText(restaurant.getAddress());

        String placeId = restaurant.getPlaceid();
        retrofit(placeId);

        like_iv.setOnClickListener(v -> clickLike(restaurant));
        like_tv.setOnClickListener(v -> clickLike(restaurant));
        webSite_iv.setOnClickListener(v -> clickWebSite());
        webSite_tv.setOnClickListener(v -> clickWebSite());
        call_iv.setOnClickListener(v -> IntentUtils.callNumber(this,phoneNumber));
        call_tv.setOnClickListener(v -> IntentUtils.callNumber(this, phoneNumber));

        byte rating = presenter.rating();

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList users = new ArrayList();
        for (int test = 1; test < 30; test++) {
            String testName = "TestName" + test;
            User user = new User("testUid", testName, "testMail",
                    "testPhoneNumber", "", "");

            users.add(user);
        }

        Context context = getApplicationContext();
        RecyclerView.Adapter adapter = new RestaurantAdapter(users, context);
        recyclerView.setAdapter(adapter);

        Task<DocumentSnapshot> t = UserFirebase.getUser(uid);
        t.addOnCompleteListener(task -> {
            String restaurantChoice = t.getResult().get("restaurantChoice").toString();
            if (restaurantChoice.equals(placeId)) {
                visibleIconInFAB = true;
                restaurantChoice_visible_fab.show();
                restaurantChoice_invisible_fab.hide();
            }
        });

        floatingButton_listener(placeId, restaurantChoice_visible_fab, restaurantChoice_invisible_fab,
                restaurant);
    }


    private void floatingButton_listener(String placeId, FloatingActionButton restaurantChoice_visible_fab,
                                         FloatingActionButton restaurantChoice_invisible_fab,
                                         Restaurant restaurant) {
        restaurantChoice_visible_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (visibleIconInFAB) {
                    visibleIconInFAB = false;
                    UserFirebase.updateRestaurantChoice("", uid);
                    LunchesFirebase.deleteLunch(uid);

                    restaurantChoice_visible_fab.hide();
                    restaurantChoice_invisible_fab.show();

                }
            }
        });

        restaurantChoice_invisible_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!visibleIconInFAB) {
                    visibleIconInFAB = true;
                    UserFirebase.updateRestaurantChoice(placeId, uid);
                    LunchesFirebase.createLunches(uid, placeId);
                    restaurantChoice_visible_fab.show();
                    restaurantChoice_invisible_fab.hide();
                }
            }
        });
    }

    private void retrofit(String placeid) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = UnsafeOkHttpClient.getUnsafeOkHttpClient().addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestaurantsService service = retrofit.create(RestaurantsService.class);

        service.listDetails(placeid).enqueue(new Callback<DetailsResponse>() {


            @Override
            public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {

                phoneNumber = response.body().getPhoneNumber();
                webSite = response.body().getWebSite();

            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {

            }
        });
    }

    private void clickWebSite() {
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(webSite));
        startActivity(intent);
    }

    private void clickLike(Restaurant restaurant) {
        presenter.likeRestaurant(FirebaseAuth.getInstance().getUid(), restaurant.getPlaceid());
    }
}
