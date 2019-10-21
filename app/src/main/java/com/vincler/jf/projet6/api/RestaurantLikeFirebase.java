package com.vincler.jf.projet6.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vincler.jf.projet6.models.LikeRestaurant;

public class RestaurantLikeFirebase {

    private static final String COLLECTION_NAME = "restaurant";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createRestaurant(String uid, String latLongRestaurant) {
        LikeRestaurant userToCreate = new LikeRestaurant(uid, latLongRestaurant);

        return RestaurantLikeFirebase.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid) {
        return RestaurantLikeFirebase.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String userName, String uid) {
        return RestaurantLikeFirebase.getUsersCollection().document(uid).update("userName", userName);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return RestaurantLikeFirebase.getUsersCollection().document(uid).delete();
    }
}
