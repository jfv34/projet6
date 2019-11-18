package com.vincler.jf.projet6.ui.restaurant;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vincler.jf.projet6.utils.ConstantsUtils.NOTIFICATION_HOUR;
import static com.vincler.jf.projet6.utils.ConstantsUtils.NOTIFICATION_MINUTES;

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
            stopNotification();
        } else {
            UserFirebase.updateRestaurantFavoriteId(restaurantDisplayedId, getUserID());
            UserFirebase.updateRestaurantFavoriteName(details.getName(), getUserID());
            scheduleNotification();
        }
        isFavorited = !isFavorited;
        view.displayFavorite(isFavorited);
        loadUsers();
    }

    private String getUserID() {
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

    private void scheduleNotification() {


        UserFirebase.getUsersByRestaurantFavorite(restaurantDisplayedId).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                int duration = getDuration();
                String titleNotification = "Rappel: déjeuner";
                String message = getNotificationText(task);

                Data data = new Data.Builder()
                        .putString(NotificationsWorker.EXTRA_TITLE, titleNotification)
                        .putString(NotificationsWorker.EXTRA_TEXT, message)
                        .build();

                OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationsWorker.class)
                        .setInitialDelay(duration, TimeUnit.MINUTES)
                        .setInputData(data)
                        .build();

                WorkManager.getInstance().cancelAllWork();
                WorkManager.getInstance().enqueue(oneTimeWorkRequest);
            }
        });
    }

    private String getNotificationText(Task<QuerySnapshot> task) {
        int size = task.getResult().size();
        String workmatesListText = getWorkmatesListText(task);
        StringBuilder message = new StringBuilder();
        message.append("Vous avez choisi de déjeuner aujourd'hui au restaurant");
        message.append(" ");
        message.append(details.getName());
        message.append(" ");
        message.append("situé");
        message.append(" ");
        message.append(details.getAddress());
        message.append(". ");
        if (size == 1) {
            message.append("Aucun autre oollègue n'a prévu d'y déjeuner");
        }
        if (size == 2) {
            message.append("Une autre personne a prévu d'y déjeuner:");
        }
        if (size > 2) {
            message.append("Les autres personnes qui ont prévu d'y déjeuner sont:");
        }
        if (size > 1) {
            message.append(" ");
            message.append(workmatesListText);
        }
        message.append(".");

        return message.toString();
    }

    private String getWorkmatesListText(Task<QuerySnapshot> task) {
        int size = task.getResult().size();
        String text = "";
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        for (int i = 0; i < size; i++) {
            String workmate = task.getResult().getDocuments().get(i).get("username").toString();
            if (!workmate.equals(userName)) {
                if (size == 2) {
                    text = workmate;
                }
                if (i < size && size > 2) {
                    text = String.format("%s%s, ", text, workmate);
                    if (i == size - 1) {
                        text = text
                                + "et "
                                + workmate;
                    }
                }
            }
        }
        return text;
    }

    private int getDuration() {
        SimpleDateFormat formatCurrentHour = new SimpleDateFormat("HH");
        SimpleDateFormat formatCurrentMinutes = new SimpleDateFormat("mm");
        Date date = new Date();

        int currentHour = Integer.parseInt(formatCurrentHour.format(date));
        int currentMinutes = Integer.parseInt(formatCurrentMinutes.format(date));

        int notificationTime = NOTIFICATION_HOUR * 60 + NOTIFICATION_MINUTES;
        int currentTime = currentHour * 60 + currentMinutes;

        int duration = notificationTime - currentTime;
        if (currentTime > notificationTime) {
            duration = duration + 1440;
        }
        return duration;
    }

    private void stopNotification() {
        WorkManager.getInstance().cancelAllWork();
    }
}