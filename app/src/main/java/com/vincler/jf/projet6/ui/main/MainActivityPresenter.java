package com.vincler.jf.projet6.ui.main;

import android.text.Editable;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.models.Search;
import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.models.googleMapResponse.GeometryResponse;
import com.vincler.jf.projet6.models.restaurants.nearby.NearbyRestaurant;
import com.vincler.jf.projet6.models.search.SearchResponse;
import com.vincler.jf.projet6.ui.SharedData;
import com.vincler.jf.projet6.utils.UnsafeOkHttpClient;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.View view;
    public MutableLiveData<ArrayList<NearbyRestaurant>> restaurantsData = new MutableLiveData<>(new ArrayList<>());

    @Override
    public MutableLiveData<ArrayList<NearbyRestaurant>> getLiveData() {
        return restaurantsData;
    }
    public MainActivityPresenter(MainActivityContract.View view) {
        this.view = view;
    }


    @Override
    public void filterRestaurants(String query) {
        ArrayList<NearbyRestaurant> result = new ArrayList<>();
        ArrayList<NearbyRestaurant> restaurants = restaurantsData.getValue();

        if (restaurants != null) {
            for (int i = 0; i < restaurants.size(); i++) {
                String r = restaurants.get(i).getName();
                NearbyRestaurant data = restaurants.get(i);

                result.add(new NearbyRestaurant(
                        data.getName(),
                        data.getLatitude(),
                        data.getLongitude(),
                        data.getAddress(),
                        data.getPhoto(),
                        data.getStars(),
                        r.toLowerCase().contains(query.toLowerCase()),
                        restaurants.get(i).getIsOpenNow(),
                        data.getWorkmatesNumber(),
                        data.getPlaceid()
                ));
            }
        }

        restaurantsData.setValue(result);
    }

    @Override
    public void loadUser() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null) {
            view.startLogin();
        } else {
            Task<DocumentSnapshot> u = UserFirebase.getUser(firebaseUser.getUid());
            u.addOnCompleteListener(task -> {
                DocumentSnapshot result = task.getResult();

                if (result.getData()== null) {

                    UserFirebase.createUser(
                            firebaseUser.getUid(),
                            firebaseUser.getDisplayName(),
                            firebaseUser.getEmail(),
                            firebaseUser.getPhoneNumber(),
                            firebaseUser.getPhotoUrl().toString()
                    );
                }else {
                    User user = new User(
                            result.getString("uid"),
                            result.getString("username"),
                            result.getString("email"),
                            result.getString("phoneNumber"),
                            result.getString("restaurantFavoriteId"),
                            result.getString("restaurantFavoriteName"),
                            result.getString("photoUserUrl"));

                    view.displayUserInformation(user);
                    SharedData.favoritedRestaurant.setValue(user.getRestaurantFavoriteId());
                }
            });
        }
    }

    public String getUidFirebase() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : "";
    }

    @Override
    public void autocompleteRequest(Editable s, PlacesClient placesClient) {

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest
                .builder()
                .setQuery(s.toString())
                .build();
        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            ArrayList<Search> searchList = new ArrayList<>();
            int i=0;
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {

                Search search = new Search(prediction.getPlaceId(),prediction.getPrimaryText(null).toString());
                searchList.add(i,search);
                i++;
            }
            view.updateSearch(searchList);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e("tag_places", "Place not found: " + apiException.getStatusCode());
            }
        });
    }

    @Override
    public void search(Search search) {
Log.i("test_click",search.getPlaceId()+"/"+search.getName());



        RestaurantsService service;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = UnsafeOkHttpClient.getUnsafeOkHttpClient().addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RestaurantsService.class);

        service.geometry(search.getPlaceId()).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                double latitude = response.body().getLatitude();
                double longitude = response.body().getLongitude();
                Log.i("tag_result_latitude",String.valueOf(latitude));
                Log.i("tag_result_longitude", String.valueOf(longitude));

            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {

            }
        });
    }
}