package com.vincler.jf.projet6.ui.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.models.googleMapResponse.ListRestaurantResponse;
import com.vincler.jf.projet6.models.googleMapResponse.RestaurantResponse;
import com.vincler.jf.projet6.utils.UnsafeOkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragmentPresenter implements MapFragmentContract.Presenter {

    private MapFragmentContract.View view;

    public MapFragmentPresenter(MapFragmentContract.View view) {
        this.view = view;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = UnsafeOkHttpClient.getUnsafeOkHttpClient().addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RestaurantsService.class);
    }

    private LocationManager locationManager;
    public static boolean locationFocusedOnUser = true;
    RestaurantsService service;

    @SuppressLint("MissingPermission")
    @Override
    public void startLocate(Activity activity) {
        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }

        if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }
    }

    @Override
    public void stopLocate() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void searchRestaurants(double latitude, double longitude) {

        String locationRequest = latitude + "," + longitude;
        service.listRestaurants(locationRequest, "1000").enqueue(new Callback<ListRestaurantResponse>() {

            @Override
            public void onResponse(Call<ListRestaurantResponse> call, Response<ListRestaurantResponse> response) {
                if (!response.body().getResults().isEmpty()) {
                    ArrayList newRestaurants = new ArrayList();
                    int sizeRestaurantsData = response.body().results.size();
                    for (int i = 0; i < sizeRestaurantsData; i++) {
                        RestaurantResponse res = response.body().getResults().get(i);
                        Restaurant restaurant = new Restaurant(res.getName(), res.getLatitude(), res.getLongitude(), res.getAddress(),
                                res.getPhoto(), res.getRating(), true, res.getIsOpenNow(), res.getPlaceid());
                        newRestaurants.add(i, restaurant);
                    }
                    view.getLiveData().setValue(newRestaurants);
                }
            }

            @Override
            public void onFailure(Call<ListRestaurantResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (locationFocusedOnUser) {
            view.updatesMapDisplay(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    @Override
    public void stopFollowUser() {
        locationFocusedOnUser = false;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Restaurant restaurantChosenByClickOnMarker(Marker marker, ArrayList<Restaurant> data) {

        LatLng latLng = marker.getPosition();
        int restauId = -1;

        for (int i = 0; i < data.size(); i++) {
            double lat = data.get(i).getLatitude();
            double lg = data.get(i).getLongitude();
            LatLng latlongTest = new LatLng(lat, lg);
            if (latlongTest.equals(latLng)) {
                restauId = i;
            }
        }
        if (restauId != -1) {
            return data.get(restauId);
        } else return null;
    }

    public BitmapDescriptor bitmapDescriptorFromVector(Context context, int drawable) {
        Drawable background = ContextCompat.getDrawable(context, drawable);
        assert background != null;
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}