package com.vincler.jf.projet6.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.models.Like;
import com.vincler.jf.projet6.models.Lunch;
import com.vincler.jf.projet6.utils.RandomString;

public class LunchesFirebase {

    private static final String COLLECTION_NAME = "lunches";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getLunchesCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createLunches(String user_uid, String restaurant_uid) {
        Lunch userToCreate = new Lunch(user_uid, restaurant_uid);
        return LunchesFirebase.getLunchesCollection().document().set(userToCreate);
    }

   // --- DELETE ---

    public static Task<Void> deleteLunch (String uid) {
        return LunchesFirebase.getLunchesCollection().document(uid).delete();
    }

    // --- GET ---

 /*   public static Task<QuerySnapshot> getLikeForRestaurant(String uid) {
        return LunchesFirebase.getLikeCollection().whereEqualTo("restaurant_uid", uid).get();
    }*/
}
