package com.vincler.jf.projet6.ui.restaurant;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vincler.jf.projet6.api.LikesFirebase;
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.models.Details;
import com.vincler.jf.projet6.models.googleMapResponse.DetailsResponse;
import com.vincler.jf.projet6.utils.UnsafeOkHttpClient;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantActivityPresenter implements RestaurantActivityContract.Presenter {

    private RestaurantActivityContract.View view;
    private String phoneNumber;
    private String webSite;

    public RestaurantActivityPresenter(RestaurantActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void likeRestaurant(String user_uid, String restaurant_uid) {

        LikesFirebase.createLike(user_uid, restaurant_uid);
        Log.i("tag_uid", user_uid);
        Log.i("tag_latlongRestaurant", restaurant_uid);
    }

    @Override
    public String getUidFirebase() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : "";
    }

    @Override
    public byte rating() {

        return 0;
    }

    @Override
    public Details retrofit(String placeid) {

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
                Log.i("phoneNumber1",phoneNumber+"***");
                webSite = response.body().getWebSite();

            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {

            }
        });
        Log.i("phoneNumber2",phoneNumber+"***");

        return new Details(phoneNumber,webSite);

    }
}