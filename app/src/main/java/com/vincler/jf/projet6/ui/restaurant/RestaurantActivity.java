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
import com.google.firebase.auth.FirebaseAuth;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.models.googleMapResponse.DetailsResponse;
import com.vincler.jf.projet6.utils.IntentUtils;
import com.vincler.jf.projet6.utils.UnsafeOkHttpClient;

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

        Glide.with(this).
                load(restaurant.getMapsPhotoUrl()).
                into(photo_iv);

        name_tv.setText(restaurant.getName());
        address_tv.setText(restaurant.getAddress());

        String placeid = restaurant.getPlaceid();
        retrofit(placeid);

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
        String[] data = new String[40];
        for(int i=0;i<40;i++){
            data[i]= "text number "+ i;
        }

        RecyclerView.Adapter adapter = new RestaurantAdapter(data);
        recyclerView.setAdapter(adapter);

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
