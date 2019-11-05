package com.vincler.jf.projet6.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vincler.jf.projet6.models.User;

public class UserFirebase {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    private static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String email, String phoneNumber, String photoUserUrl) {
        User userToCreate = new User(uid, username, email, phoneNumber, "", "", photoUserUrl);
        return getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid) {
        return getUsersCollection().document(uid).get();
    }


    // --- GET BY RESTAURANTCHOICE ---

    public static Task<QuerySnapshot> getUsersByRestaurantChoice(String restaurantChoice) {

        return getUsersCollection()
                .whereEqualTo("restaurantChoice", restaurantChoice)
                .get();
    }

    // --- GET ALL USERS ---

    public static Task<QuerySnapshot> getUsers() {

        return getUsersCollection()
                .get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserFirebase.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateRestaurantChoiceId(String restaurantChoice, String uid) {
        return UserFirebase.getUsersCollection().document(uid).update("restaurantChoice", restaurantChoice);
    }

    public static Task<Void> updateRestaurantChoiceName(String restaurantName, String uid) {
        return UserFirebase.getUsersCollection().document(uid).update("restaurantName", restaurantName);
    }


    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserFirebase.getUsersCollection().document(uid).delete();
    }
}