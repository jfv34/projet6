package com.vincler.jf.projet6.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.models.Like;
import com.vincler.jf.projet6.utils.RandomString;

public class LikesFirebase {

    private static final String COLLECTION_NAME = "likes";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getLikeCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createLike(String user_uid, String restaurant_uid) {
        Like userToCreate = new Like(user_uid, restaurant_uid);
        String alea = "LIKE_"+RandomString.random(16);
        return LikesFirebase.getLikeCollection().document(alea).set(userToCreate);
    }

    // --- GET ---

    public static Task<QuerySnapshot> getLikeForRestaurant(String uid) {
        return LikesFirebase.getLikeCollection().whereEqualTo("restaurant_uid", uid).get();
    }
}
