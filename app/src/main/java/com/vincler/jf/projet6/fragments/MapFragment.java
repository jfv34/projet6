package com.vincler.jf.projet6.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.activities.MainActivity;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.models.SearchStatus;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_CODE = 123;
    private static final int ZOOM_MAP = 16;
    private LocationManager locationManager;
    private double previousLatitude;
    private double previousLongitude;
    private GoogleMap googleMap;
    public static boolean locationFocusedOnUser = true;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).restaurantsData.observe(this, restaurants -> {
            displayRestaurants(restaurants, "");
        });

        ((MainActivity) getActivity()).customEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                displayRestaurants((((MainActivity) getActivity()).restaurantsData.getValue()), s.toString());

            }
        });
    }

    void displayRestaurants(ArrayList<Restaurant> restaurants, String textSearched) {
        ((MainActivity) getActivity()).hideRestaurantsNotSearched(textSearched);
        Log.i("tag_display_restau", "ok");

        if (googleMap != null) {
            googleMap.clear();

            for (int i = 0; i < restaurants.size(); i++) {
                if (restaurants.get(i).getSearchStatus() == SearchStatus.DEFAULT) {
                    double latitude = restaurants.get(i).getLatitude();
                    double longitude = restaurants.get(i).getLongitude();
                    markers(latitude, longitude, R.drawable.icon_marker_red);
                }

            }
        }
    }

    private void markers(double latitude, double longitude, int drawable) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(bitmapDescriptorFromVector(requireContext(), drawable)));
    }

    @SuppressLint("MissingPermission")
    private void loadMap() {

        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (map != null) {
            map.getMapAsync(googleMap -> {

                this.googleMap = googleMap;
                googleMap.setMyLocationEnabled(true);
                updatesMapDisplay(previousLatitude, previousLongitude);

                googleMap.setOnMarkerClickListener(marker -> {
                    String id = marker.getId();
                    Log.i("tag_marckerClick", "CLICK " + id);
                    return false;
                });

                googleMap.setOnCameraIdleListener(() -> {
                    LatLng target = googleMap.getCameraPosition().target;
                    Double latitude = target.latitude;
                    Double longitude = target.longitude;

                    if (latitude != previousLatitude && longitude != previousLongitude) {
                        previousLatitude = latitude;
                        previousLongitude = longitude;
                        ((MainActivity) getActivity()).findRestaurantsNearCoordinates(latitude, longitude);

                        ArrayList<Restaurant> restaurants = ((MainActivity) getActivity()).restaurantsData.getValue();

                        if (restaurants != null) {
                            displayRestaurants(restaurants, "");
                        }
                    }
                });
                onUserMoveMap();
            });
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int drawable) {
        Drawable background = ContextCompat.getDrawable(context, drawable);
        assert background != null;
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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
        checkPermissions();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(Location location) {
        if (locationFocusedOnUser) {
            updatesMapDisplay(location.getLatitude(), location.getLongitude());
        }
    }

    private void updatesMapDisplay(double latitude, double longitude) {
        if (googleMap != null) {
            LatLng latlng = new LatLng(latitude, longitude);
            CameraPosition cameraPosition = new CameraPosition.Builder().zoom(ZOOM_MAP).target(latlng).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void onUserMoveMap() {
        googleMap.setOnCameraMoveListener(() -> locationFocusedOnUser = false);
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

    private void checkPermissions() {

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
            checkPermissions();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }
}