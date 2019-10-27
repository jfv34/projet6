package com.vincler.jf.projet6.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vincler.jf.projet6.models.User;

public class UserFirebase {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String email,
                                        String phoneNumber, String restaurantChoice,
                                        String photoUserUrl) {
        User userToCreate = new User(uid, username, email, phoneNumber, restaurantChoice, photoUserUrl);

        return UserFirebase.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserFirebase.getUsersCollection().document(uid).get();
    }

    // --- GET BY RESTAURANTCHOICE ---

   /* public static Task<DocumentSnapshot> getUsersByRestaurantChoice(String restaurantChoice){
        getUsersCollection().document().collection("cities")
                .whereEqualTo("capital", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }*/

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserFirebase.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateRestaurantChoice(String restaurantChoice, String uid) {
        return UserFirebase.getUsersCollection().document(uid).update("restaurantChoice", restaurantChoice);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserFirebase.getUsersCollection().document(uid).delete();
    }
}
