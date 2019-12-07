package com.vincler.jf.projet6.api;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.models.Like;
import com.vincler.jf.projet6.utils.GetStringUtils;

import java.util.Objects;

public class LikesFirebase {

    private static final String COLLECTION_NAME = "likes";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getLikeCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createLike(String user_uid, String restaurant_uid) {
        Like userToCreate = new Like(user_uid, restaurant_uid);
        String alea = "LIKE_" + GetStringUtils.random(16);
        return LikesFirebase.getLikeCollection().document(alea).set(userToCreate);
    }

    // --- GET LIKES ---

    public static Task<QuerySnapshot> getLikes(){
        return LikesFirebase.getLikeCollection()
                .get();
    }

    // --- GET LIKES FOR RESTAURANT ---

    public static Task<QuerySnapshot> getLikeForRestaurant(String user_uid, String restaurant_uid) {
        return LikesFirebase.getLikeCollection()
                .whereEqualTo("user_uid", user_uid)
                .whereEqualTo("restaurant_uid", restaurant_uid)
                .get();
    }

    public static void deleteLike(String user_uid, String restaurant_uid) {
        getLikeCollection()
                .whereEqualTo("user_uid", user_uid)
                .whereEqualTo("restaurant_uid", restaurant_uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String idDelete = document.getId();
                            getLikeCollection().document(idDelete)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {

                                    })
                                    .addOnFailureListener(e -> Log.w("tag_LikesFirebase", "Error deleting document", e));
                        }
                    } else {
                        Log.d("tag_LikesFirebase", "Error getting documents: ", task.getException());
                    }
                });
    }
}