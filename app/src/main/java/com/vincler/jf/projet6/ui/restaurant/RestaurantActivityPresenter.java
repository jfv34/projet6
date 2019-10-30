package com.vincler.jf.projet6.ui.restaurant;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vincler.jf.projet6.api.LikesFirebase;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.models.Details;
import com.vincler.jf.projet6.models.Restaurant;
import com.vincler.jf.projet6.models.googleMapResponse.DetailsResponse;
import com.vincler.jf.projet6.utils.UnsafeOkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantActivityPresenter implements RestaurantActivityContract.Presenter {

    private Restaurant restaurant;
    private RestaurantActivityContract.View view;
    private String phoneNumber;
    private String webSite;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .client(UnsafeOkHttpClient
                    .getUnsafeOkHttpClient()
                    .addInterceptor(
                            new HttpLoggingInterceptor()
                                    .setLevel(HttpLoggingInterceptor.Level.BODY)).build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    RestaurantsService service = retrofit.create(RestaurantsService.class);

    public RestaurantActivityPresenter(RestaurantActivityContract.View view, Restaurant restaurant) {
        this.view = view;
        this.restaurant = restaurant;
    }

    @Override
    public String getPhoneNumber() {

        return phoneNumber;
    }

    @Override
    public String getWebSite() {

        return webSite;
    }

    @Override
    public void likeRestaurant() {
        LikesFirebase.createLike(getUserID(), restaurant.getPlaceid());
    }

    @Override
    public void dislikeRestaurant() {
        LikesFirebase.deleteLike(getUserID(), restaurant.getPlaceid());
    }

    @Override
    public void notFavoritedRestaurant() {
        UserFirebase.updateRestaurantChoice("", getUserID());
    }

    @Override
    public void favoritedRestaurant() {
        UserFirebase.updateRestaurantChoice(restaurant.getPlaceid(), getUserID());
    }

    public String getUserID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : "";
    }

    @Override
    public void loadRestaurant() {
        LikesFirebase.getLikeForRestaurant(
                getUserID(),
                restaurant.getPlaceid()
        ).addOnCompleteListener(task1 -> {

            boolean isLiked = task1.getResult() != null && !task1.getResult().isEmpty();

            UserFirebase.getUser(getUserID())
                    .addOnCompleteListener(task2 -> {
                        String restaurantChoice = task2.getResult().get("restaurantChoice").toString();
                        boolean isFavorited = restaurantChoice.equals(restaurant.getPlaceid());

                        service.listDetails(restaurant.getPlaceid()).enqueue(new Callback<DetailsResponse>() {
                            @Override
                            public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {
                                phoneNumber=response.body().getPhoneNumber();
                                webSite = response.body().getWebSite();
                                view.displayDetails(
                                        new Details(
                                                isLiked,
                                                isFavorited,
                                                phoneNumber,
                                                webSite)
                                );
                            }

                            @Override
                            public void onFailure(Call<DetailsResponse> call, Throwable t) {

                            }
                        });
                    });
        });
    }
}