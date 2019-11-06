package com.vincler.jf.projet6.ui.restaurant;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.api.LikesFirebase;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.models.Details;
import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.models.googleMapResponse.DetailsResponse;
import com.vincler.jf.projet6.utils.UnsafeOkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantActivityPresenter implements RestaurantActivityContract.Presenter {

    private RestaurantActivityContract.View view;

    private String restaurantChoiceId;
    private Details details;
    private String phoneNumber;
    private String webSite;
    private String name;
    private String address;
    private String photo;

    private boolean isFavorited = false;
    private boolean isLiked = false;

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

    public RestaurantActivityPresenter(RestaurantActivityContract.View view, String restaurantChoiceId) {
        this.view = view;
        this.restaurantChoiceId = restaurantChoiceId;
    }

    @Override
    public void loadRestaurant() {

        Log.i("tag_loadRestaurant", "ok");
        loadDetails();


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
    public void toggleLike() {
        if (isLiked) {
            LikesFirebase.deleteLike(getUserID(), restaurantChoiceId);
        } else {
            LikesFirebase.createLike(getUserID(), restaurantChoiceId);
        }

        isLiked = !isLiked;
        view.displayLike(isLiked);
        loadUsers();
    }

    @Override
    public void toggleFavorite() {
        if (isFavorited) {
            UserFirebase.updateRestaurantChoiceId("", getUserID());
            UserFirebase.updateRestaurantChoiceName("", getUserID());
        } else {
            UserFirebase.updateRestaurantChoiceId(restaurantChoiceId, getUserID());
            UserFirebase.updateRestaurantChoiceName(details.getName(), getUserID());
        }
        isFavorited = !isFavorited;
        view.displayFavorite(isFavorited);
        loadUsers();
    }

    public String getUserID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : "";
    }

    @Override
    public void loadDetails() {
        LikesFirebase.getLikeForRestaurant(
                getUserID(),
                restaurantChoiceId
        ).addOnCompleteListener(task1 -> {
            isLiked = task1.getResult() != null && !task1.getResult().isEmpty();
            UserFirebase.getUser(getUserID())
                    .addOnCompleteListener(task2 -> {
                        if (task2.getResult().get("restaurantChoice") != null) {
                            String restaurantChoice = task2.getResult().get("restaurantChoice").toString();
                            isFavorited = restaurantChoice.equals(restaurantChoice);
                        } else isFavorited = false;

                        service.listDetails(restaurantChoiceId).enqueue(new Callback<DetailsResponse>() {
                            @Override
                            public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {
                                phoneNumber = response.body().getPhoneNumber();
                                webSite = response.body().getWebSite();
                                name = response.body().getName();
                                address = response.body().getAddress();
                                photo = response.body().getPhoto();
                                details = new Details(
                                        name,
                                        address,
                                        photo,
                                        isLiked,
                                        isFavorited,
                                        phoneNumber,
                                        webSite);
                                view.displayDetails(
                                        new Details(
                                                name,
                                                address,
                                                photo,
                                                isLiked,
                                                isFavorited,
                                                phoneNumber,
                                                webSite)
                                );
                                view.displayRestaurant(details);
                            }

                            @Override
                            public void onFailure(Call<DetailsResponse> call, Throwable t) {
                                Log.i("tag_onResponse", "failure");
                            }
                        });
                    });

        });
    }

    @Override
    public void loadUsers() {
        List<HashMap> result = new ArrayList<>();
        Task<QuerySnapshot> data = UserFirebase.getUsersByRestaurantChoice(restaurantChoiceId);
        data.addOnCompleteListener(task -> {
            if (data.getResult() != null) {

                for (int i = 0; i < data.getResult().size(); i++) {
                    HashMap h = (HashMap) data.getResult().getDocuments().get(i).getData();
                    result.add(h);
                }

                ArrayList<User> users = new ArrayList<>();

                for (int i = 0; i < result.size(); i++) {
                    HashMap hm = result.get(i);
                    User user = new User(
                            hm.get("uid").toString(),
                            hm.get("username").toString(),
                            hm.get("email").toString(),
                            hm.get("phoneNumber").toString(),
                            hm.get("restaurantChoice").toString(),
                            hm.get("restaurantName").toString(),
                            hm.get("photoUserUrl").toString());

                    users.add(user);

                }

                view.displayUsers(users);
            }
        });
    }
}