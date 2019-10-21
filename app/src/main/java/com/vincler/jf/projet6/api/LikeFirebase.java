package com.vincler.jf.projet6.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vincler.jf.projet6.models.Like;

public class LikeFirebase {

    private static final String COLLECTION_NAME = "like";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getLikeCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createLike(String uid, String latLongRestaurant) {
        Like userToCreate = new Like(uid, latLongRestaurant);

        return LikeFirebase.getLikeCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getLike(String uid) {
        return LikeFirebase.getLikeCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String userName, String uid) {
        return LikeFirebase.getLikeCollection().document(uid).update("userName", userName);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return LikeFirebase.getLikeCollection().document(uid).delete();
    }
}
