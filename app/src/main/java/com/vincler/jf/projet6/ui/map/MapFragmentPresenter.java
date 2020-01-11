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
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.models.restaurants.nearby.ListNearbyRestaurantResponse;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurantResponse;
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
    private LocationManager locationManager;
    private static boolean locationFocusedOnUser = true;
    private RestaurantsService service;
    private ArrayList newRestaurants;
    private ArrayList oldRestaurants;


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
        service.listRestaurants(locationRequest, "1000").enqueue(new Callback<ListNearbyRestaurantResponse>() {
            @Override
            public void onResponse(Call<ListNearbyRestaurantResponse> call, Response<ListNearbyRestaurantResponse> response) {
                if (!response.body().getResults().isEmpty()) {

                    oldRestaurants = newRestaurants;
                    newRestaurants = new ArrayList();
                    int sizeRestaurantsData = response.body().results.size();
                    for (int i = 0; i < sizeRestaurantsData; i++) {
                        int r=0;
                        NearbyRestaurantResponse res = response.body().getResults().get(i);
                        NearbyRestaurant restaurant = new NearbyRestaurant(res.getName(), res.getLatitude(),
                                res.getLongitude(), res.getAddress(), res.getPhoto(), 0,
                                true, res.getIsOpenNow(), 0,
                                res.getPlaceid());
                        boolean alreadyDisplay = false;
                        if (oldRestaurants != null) {
                            for (int j = 0; j < oldRestaurants.size(); j++) {

                                if ( ((NearbyRestaurant)oldRestaurants.get(j)).getPlaceid().equals(restaurant.getPlaceid())) {
                                    Log.i("tag_oldRestaurant","restau identical !");
                                    alreadyDisplay = true;
                                }
                                else {Log.i("tag_oldRestaurant","restau NOT identical");}
                            }
                        }
                        if (!alreadyDisplay) {
                            newRestaurants.add(r, restaurant);
                            r++;
                        }
                    }
                    view.getLiveData().setValue(newRestaurants);
                }
            }

            @Override
            public void onFailure(Call<ListNearbyRestaurantResponse> call, Throwable t) {
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

    public NearbyRestaurant restaurantChosenByClickOnMarker(Marker marker, ArrayList<NearbyRestaurant> data) {
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