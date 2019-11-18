package com.vincler.jf.projet6.notifications;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.vincler.jf.projet6.api.UserFirebase;

public class NotificationsActivity extends AppCompatActivity {

    String restaurantFavoriteName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Task<DocumentSnapshot> user = UserFirebase.getUser(uid);
        user.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                restaurantFavoriteName = task.getResult().get("RestaurantFavoriteName").toString();
            }
        });

        Log.i("tag_restaurant_","notif: "+restaurantFavoriteName);
    }
}
