package com.vincler.jf.projet6.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.UnsafeOkHttpClient;
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.models.ListRestaurantsResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends DrawerFragment implements LocationListener, OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_CODE = 123;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private GoogleMap googleMap;
    private DrawerLayout drawerLayout;
    private final String RADIUS = "1500";


    public static MapFragment newInstance() {

        return new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        drawerLayout = rootView.findViewById(R.id.fragment_map_drawer_layout);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void loadMap() {

        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(new OnMapReadyCallback() {

            @Override
            @SuppressLint("MissingPermission")
            public void onMapReady(GoogleMap googleMap) {

                googleMap.setMyLocationEnabled(true);
                requestByRetrofit();

            }
        });
    }


    private void requestByRetrofit() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = UnsafeOkHttpClient.getUnsafeOkHttpClient().addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RestaurantsService service = retrofit.create(RestaurantsService.class);

        String locationUser = latitude + "," + longitude;

        service.listRestaurants(locationUser, RADIUS).enqueue(new Callback<ListRestaurantsResponse>() {
            @Override
            public void onResponse(Call<ListRestaurantsResponse> call, Response<ListRestaurantsResponse> response) {

                if (!response.body().getResults().isEmpty()) {
                    Log.i("tag_response", response.body().getResults().get(0).getRestaurant());
                }


            }

            @Override
            public void onFailure(Call<ListRestaurantsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();

        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkpermissions();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if (googleMap != null) {
            LatLng latlng = new LatLng(latitude, longitude);

            CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(latlng).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

        requestByRetrofit();
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

    private void checkpermissions() {

        if
        (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMISSIONS_REQUEST_CODE);
            return;
        }

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);

        }
        if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);


        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);

        }
        loadMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            checkpermissions();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


}






