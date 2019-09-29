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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.UnsafeOkHttpClient;
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.models.googleMapResponse.ListRestaurantResponse;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int PERMISSIONS_REQUEST_CODE = 123;
    private static final int ZOOM_MAP = 16;
    private static final String RADIUS = "1000";
    private LocationManager locationManager;
    private double latitudeUser;
    private double longitudeUser;
    private double previousLatitudeUser;
    private double previousLongitudeUser;
    private static GoogleMap googleMap;
    private static ArrayList<Restaurant> restaurantsData = new ArrayList<>();
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
    }

    @SuppressLint("MissingPermission")
    private void loadMap() {

        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (map != null) {
            map.getMapAsync(googleMap -> {

                MapFragment.this.googleMap = googleMap;
                googleMap.setMyLocationEnabled(true);
                updatesMapDisplay(latitudeUser, longitudeUser);
                retrofit();
                googleMap.setOnMarkerClickListener(this);
                onUserMoveMap();
            });
        }
    }

    private void retrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = UnsafeOkHttpClient.getUnsafeOkHttpClient().addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RestaurantsService service = retrofit.create(RestaurantsService.class);

        requestForListRestaurant(service, latitudeUser, longitudeUser);
    }

    private void requestForListRestaurant(RestaurantsService service, double latitudeRequest,
                                          double longitudeRequest) {

        String locationRequest = latitudeRequest + "," + longitudeRequest;

        service.listRestaurants(locationRequest, RADIUS).enqueue(new Callback<ListRestaurantResponse>() {
            @Override
            public void onResponse(Call<ListRestaurantResponse> call, Response<ListRestaurantResponse> response) {
                Log.i("tag_response", "ok");

                if (!response.body().getResults().isEmpty()) {
                    getDataRestaurants(response);
                }
            }

            @Override
            public void onFailure(Call<ListRestaurantResponse> call, Throwable t) {
                Log.i("tag_response", "onFailure");
                t.printStackTrace();
            }
        });
    }

    private void getDataRestaurants(Response<ListRestaurantResponse> response) {

        int sizeRestaurantsData = response.body().results.size();

        for (int i = 0; i < sizeRestaurantsData; i++) {

            String name = response.body().getResults().get(i).getRestaurant();
            double latitude = response.body().getResults().get(i).getLatitude();
            double longitude = response.body().getResults().get(i).getLongitude();
            String address = response.body().getResults().get(i).getAddress();
            String photo = response.body().getResults().get(i).getPhoto();


            Restaurant restaurant = new Restaurant(name, latitude, longitude, address, photo);
            restaurantsData.add(i, restaurant);

            Log.i("tag_response_name", restaurantsData.get(i).getName());
            Log.i("tag_response_lat", String.valueOf(restaurantsData.get(i).getLatitude()));
            Log.i("tag_response_long", String.valueOf(restaurantsData.get(i).getLongitude()));
            Log.i("tag_response_addres", String.valueOf(restaurantsData.get(i).getAddress()));
            Log.i("tag_response_photo", String.valueOf(restaurantsData.get(i).getPhoto()));
        }

        markers(restaurantsData, sizeRestaurantsData);
    }

    private void markers(ArrayList<Restaurant> restaurantData, int sizeRestaurantsData) {

        for (int i = 0; i < sizeRestaurantsData; i++) {
            double latitude = restaurantData.get(i).getLatitude();
            double longitude = restaurantData.get(i).getLongitude();

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(bitmapDescriptorFromVector(getActivity())));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String id = marker.getId();
        Log.i("tag_marckerClick", "CLICK " + id);

        return false;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context) {

        Drawable background = ContextCompat.getDrawable(context, R.drawable.icon_marker_red);
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
            updatesGeolocationUser(location);
            updatesMapDisplay(latitudeUser, longitudeUser);
            if (latitudeUser != previousLatitudeUser && longitudeUser != previousLongitudeUser) {
                previousLatitudeUser = latitudeUser;
                previousLongitudeUser = longitudeUser;
                retrofit();
            }
        }
    }

    private void updatesGeolocationUser(Location location) {
        latitudeUser = location.getLatitude();
        longitudeUser = location.getLongitude();
    }

    private static void updatesMapDisplay(double latitude, double longitude) {
        if (googleMap != null) {
            LatLng latlng = new LatLng(latitude, longitude);
            CameraPosition cameraPosition = new CameraPosition.Builder().zoom(ZOOM_MAP).target(latlng).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void onUserMoveMap() {
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                locationFocusedOnUser = false;
                LatLng latLng = googleMap.getCameraPosition().target;
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;

                ///  requestForListRestaurant(...);    ???

            }
        });
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

    public static void searchRestaurant(String restaurant) {

        int restaurantFound_id = -1;
        for (int i = 0; i < restaurantsData.size(); i++) {
            String r = restaurantsData.get(i).getName();
            if (r.equals(restaurant)) {
                restaurantFound_id = i;
                break;
            }
        }
        if (restaurantFound_id != -1) {
            double restaurantLatitude = restaurantsData.get(restaurantFound_id).getLatitude();
            double restaurantLongitude = restaurantsData.get(restaurantFound_id).getLongitude();
            locationFocusedOnUser = false;
            updatesMapDisplay(restaurantLatitude, restaurantLongitude);
        }


    }

}