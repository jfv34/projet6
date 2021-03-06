package com.vincler.jf.projet6.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;
import com.vincler.jf.projet6.ui.SharedData;
import com.vincler.jf.projet6.ui.main.MainActivity;
import com.vincler.jf.projet6.ui.restaurant.RestaurantActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MapFragment extends Fragment implements MapFragmentContract.View {

    private static final int PERMISSIONS_REQUEST_CODE = 123;
    private GoogleMap googleMap;
    private boolean isFavorited = false;
    private MapFragmentContract.Presenter presenter = new MapFragmentPresenter(this);

    @Override
    public MutableLiveData<ArrayList<NearbyRestaurant>> getLiveData() {
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
        loadMap();
        getLiveData().observe(this, it -> {
            if (googleMap != null) {
                for (int i = 0; i < it.size(); i++) {
                        double latitude = it.get(i).getLatitude();
                        double longitude = it.get(i).getLongitude();
                        String restaurantId = it.get(i).getPlaceid();
                        markers(latitude, longitude, restaurantId);

                }
            }
        });

        SharedData.latlngMap.observe(this,latLng ->changeMap(latLng));
    }

    private void changeMap(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void markers(double latitude, double longitude, String restaurantId) {
        Task<QuerySnapshot> data = UserFirebase.getUsers();
        data.addOnCompleteListener(task -> {
            isFavorited = false;
            if (data.getResult() != null) {
                for (int i = 0; i < data.getResult().size(); i++) {
                    HashMap h = (HashMap) data.getResult().getDocuments().get(i).getData();

                    if (restaurantId.equals(h.get("restaurantFavoriteId").toString())) {
                        isFavorited = true;
                    }
                }
            }
            int drawable;
            if (isFavorited) {
                drawable = R.drawable.icon_marker_green;
            } else {
                drawable = R.drawable.icon_marker_red;
            }
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(presenter.bitmapDescriptorFromVector(requireContext(), drawable)));
        });
    }

    @SuppressLint("MissingPermission")
    private void loadMap() {
        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (map != null) {
            map.getMapAsync(googleMap -> {
                this.googleMap = googleMap;

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Location Permission already granted
                        googleMap.setMyLocationEnabled(true);
                    } else {
                        //Request Location Permission
                        checkLocationPermission();
                    }
                } else {
                    googleMap.setMyLocationEnabled(true);
                }

                googleMap.setOnMarkerClickListener(marker -> {
                    ArrayList<NearbyRestaurant> data = getLiveData().getValue();
                    NearbyRestaurant restaurant = presenter.restaurantChosenByClickOnMarker(marker, data);
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

    private void checkLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopLocate();
    }

    @Override
    public void updatesMapDisplay(LatLng position) {
        if (googleMap != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(position).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        googleMap.setMyLocationEnabled(true);
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}