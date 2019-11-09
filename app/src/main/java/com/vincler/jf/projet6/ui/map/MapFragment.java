package com.vincler.jf.projet6.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.ui.main.MainActivity;
import com.vincler.jf.projet6.ui.restaurant.RestaurantActivity;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapFragment extends Fragment implements MapFragmentContract.View, OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_CODE = 123;
    private GoogleMap googleMap;
    private MapFragmentContract.Presenter presenter = new MapFragmentPresenter(this);

    @Override
    public MutableLiveData<ArrayList<Restaurant>> getLiveData() {
        return ((MainActivity) getActivity()).presenter.getLiveData();
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLiveData().observe(this, it -> {
            if (googleMap != null) {
                googleMap.clear();
                for (int i = 0; i < it.size(); i++) {
                    if (it.get(i).isVisible()) {
                        double latitude = it.get(i).getLatitude();
                        double longitude = it.get(i).getLongitude();
                        markers(latitude, longitude, R.drawable.icon_marker_red);
                    }
                }
            }
        });
    }

    private void markers(double latitude, double longitude, int drawable) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(presenter.bitmapDescriptorFromVector(requireContext(), drawable)));
    }

    @SuppressLint("MissingPermission")
    private void loadMap() {
        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (map != null) {
            map.getMapAsync(googleMap -> {
                this.googleMap = googleMap;
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMarkerClickListener(marker -> {
                    ArrayList<Restaurant> data = getLiveData().getValue();
                    Restaurant restaurant = presenter.restaurantChosenByClickOnMarker(marker, data);
                    if (restaurant != null) {
                        Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                        intent.putExtra("restaurantDisplayedId", restaurant.getPlaceid());
                        startActivity(intent);
                    }
                    return false;
                });
                googleMap.setOnCameraIdleListener(() -> {
                    LatLng target = googleMap.getCameraPosition().target;
                    presenter.searchRestaurants(target.latitude, target.longitude);
                });
                googleMap.setOnCameraMoveListener(() -> presenter.stopFollowUser());
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopLocate();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermissions();
    }

    @Override
    public void updatesMapDisplay(LatLng position) {
        if (googleMap != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(position).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
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