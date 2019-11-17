package com.vincler.jf.projet6.ui.restaurant;

import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.api.LikesFirebase;
import com.vincler.jf.projet6.api.UserFirebase;
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.models.restaurants.details.Details;
import com.vincler.jf.projet6.models.restaurants.details.DetailsRestaurantResponse;
import com.vincler.jf.projet6.models.restaurants.details.ResultDetailsResponse;
import com.vincler.jf.projet6.notifications.NotificationsWorker;
import com.vincler.jf.projet6.utils.UnsafeOkHttpClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantActivityPresenter implements RestaurantActivityContract.Presenter {

    private RestaurantActivityContract.View view;
    private String restaurantDisplayedId;
    private Details details;
    private boolean isFavorited = false;
    private boolean isLiked = false;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .client(UnsafeOkHttpClient
                    .getUnsafeOkHttpClient()
                    .addInterceptor(
                            new HttpLoggingInterceptor()
                                    .setLevel(HttpLoggingInterceptor.Level.BODY)).build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private RestaurantsService service = retrofit.create(RestaurantsService.class);


    public RestaurantActivityPresenter(RestaurantActivityContract.View view, String restaurantDisplayedId) {
        this.view = view;
        this.restaurantDisplayedId = restaurantDisplayedId;
    }

    @Override
    public void loadRestaurant() {

        loadDetails();
    }

    @Override
    public String getPhoneNumber() {
        return details.getPhoneNumber();
    }

    @Override
    public String getWebSite() {
        return details.getWebSite();
    }

    @Override
    public void toggleLike() {
        if (isLiked) {
            LikesFirebase.deleteLike(getUserID(), restaurantDisplayedId);
        } else {
            LikesFirebase.createLike(getUserID(), restaurantDisplayedId);
        }

        isLiked = !isLiked;
        view.displayLike(isLiked);
        loadUsers();
    }

    @Override
    public void toggleFavorite() {
        if (isFavorited) {
            UserFirebase.updateRestaurantFavoriteId("", getUserID());
            UserFirebase.updateRestaurantFavoriteName("", getUserID());
        } else {
            UserFirebase.updateRestaurantFavoriteId(restaurantDisplayedId, getUserID());
            UserFirebase.updateRestaurantFavoriteName(details.getName(), getUserID());
            sendPeriodicsNotifications();

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
                restaurantDisplayedId
        ).addOnCompleteListener(task1 -> {
            isLiked = task1.getResult() != null && !task1.getResult().isEmpty();
            UserFirebase.getUser(getUserID())
                    .addOnCompleteListener(task2 -> {
                        if (task2.getResult().get("restaurantFavoriteId") != null) {
                            String restaurantFavoriteId = task2.getResult().get("restaurantFavoriteId").toString();
                            isFavorited = restaurantDisplayedId.equals(restaurantFavoriteId);
                        } else isFavorited = false;

                        service.listDetails(restaurantDisplayedId).enqueue(new Callback<DetailsRestaurantResponse>() {
                            @Override
                            public void onResponse(Call<DetailsRestaurantResponse> call, Response<DetailsRestaurantResponse> response) {

                                ResultDetailsResponse result = response.body().resultDetailsResponse;

                                details = new Details(
                                        result.getName(),
                                        result.getAddress(),
                                        result.getPhoto(),
                                        isLiked,
                                        isFavorited,
                                        result.getPhoneNumber(),
                                        result.getWebSite());
                                view.displayDetails(details);
                                view.displayRestaurant(details);
                            }

                            @Override
                            public void onFailure(Call<DetailsRestaurantResponse> call, Throwable t) {
                                Log.i("tag_onResponse", "failure");
                            }
                        });
                    });
        });
    }

    @Override
    public void loadUsers() {
        List<HashMap> result = new ArrayList<>();
        Task<QuerySnapshot> data = UserFirebase.getUsersByRestaurantFavorite(restaurantDisplayedId);
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
                            hm.get("restaurantFavoriteId").toString(),
                            hm.get("restaurantFavoriteName").toString(),
                            hm.get("photoUserUrl").toString());

                    users.add(user);
                }

                view.displayUsers(users);
            }
        });
    }

    private void sendPeriodicsNotifications() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String currentTime = simpleDateFormat.format(new Date());
        Date firstDate = null;
        try {
            firstDate = simpleDateFormat.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date secondDate = null;
        try {
            secondDate = simpleDateFormat.parse("12:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diffInMillies;
        if (secondDate.after(firstDate)) {
            diffInMillies = secondDate.getTime() - firstDate.getTime();
        } else {
            diffInMillies = firstDate.getTime() - secondDate.getTime();
        }

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationsWorker.class)
                .setInitialDelay(diffInMillies, TimeUnit.MILLISECONDS).build();
        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
        Log.i("tag_time ", String.valueOf(diffInMillies));
    }
}