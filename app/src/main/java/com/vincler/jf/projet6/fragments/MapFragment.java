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
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.vincler.jf.projet6.R;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

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

    @Override
    public void onLocationChanged(Location location) {

        //latitude = location.getLatitude();
        //longitude = location.getLongitude();

        latitude = 48.8584;
        longitude = 2.2945;
        if(googleMap!=null){
            LatLng latlng = new LatLng(latitude,longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
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
        (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
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


