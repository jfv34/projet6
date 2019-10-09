package com.vincler.jf.projet6.ui.map;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.models.googleMapResponse.ListRestaurantResponse;
import com.vincler.jf.projet6.models.googleMapResponse.RestaurantResponse;
import com.vincler.jf.projet6.utils.UnsafeOkHttpClient;

import java.util.ArrayList;

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

    private static final int ZOOM_MAP = 16;
    private LocationManager locationManager;
    public static boolean locationFocusedOnUser = true;
    private double previousLatitude;
    private double previousLongitude;
    RestaurantsService service;

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
        locationManager.removeUpdates(this);
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
                        ArrayList<String> opening_hours_List = null;

                        Restaurant restaurant = new Restaurant(res.getName(), res.getLatitude(), res.getLongitude(), res.getAddress(), res.getPhoto(), res.getRating(), true, opening_hours_List, res.getPlaceid());
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

        /*if (latitude != previousLatitude && longitude != previousLongitude) {
                        previousLatitude = latitude;
                        previousLongitude = longitude;
                        ((MainActivity) getActivity()).findRestaurantsNearCoordinates(latitude, longitude);

                        ArrayList<Restaurant> restaurants = ((MainActivity) getActivity()).restaurantsData.getValue();

                        if (restaurants != null) {
                            displayRestaurants(restaurants, "");
                        }
                    }*/
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
}
