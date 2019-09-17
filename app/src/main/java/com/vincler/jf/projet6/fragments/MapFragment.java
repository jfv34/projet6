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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.vincler.jf.projet6.R;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends DrawerFragment implements LocationListener, OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_CODE = 123;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private GoogleMap googleMap;

    public static MapFragment newInstance() {

        return new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        return rootView;
    }

    private void loadMap() {

        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {

                MapFragment.this.googleMap = googleMap;
                googleMap.setMyLocationEnabled(true);

                //  Initialize the SDK

                Places.initialize(Objects.requireNonNull(getActivity()), "AIzaSyDxfJVIikFlDrFiDOQsfG7cFeQICbmZrtc");

                // Create a new Places client instance
                PlacesClient placesClient = Places.createClient(getActivity());

                // Use fields to define the data types to return.

                List<Place.Field> placeFieldsName = Collections.singletonList(Place.Field.NAME);

// Use the builder to create a FindCurrentPlaceRequest.
                FindCurrentPlaceRequest request =
                        FindCurrentPlaceRequest.newInstance(placeFieldsName);

// Call findCurrentPlace and handle the response (first check that the user has granted permission).
                if (ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
                    placeResponse.addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FindCurrentPlaceResponse response = task.getResult();
                            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {

                                String i = placeLikelihood.getPlace().toString();
                                Log.i("tag_place: ", i);

                            }
                        } else {
                            Exception exception = task.getException();

                            if (exception instanceof ApiException) {
                                ApiException apiException = (ApiException) exception;

                                Log.e("tag", "Place not found:" + apiException.getStatusCode());
                            }
                        }
                    });
                }
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






