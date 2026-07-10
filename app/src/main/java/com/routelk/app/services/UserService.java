package com.routelk.app.services;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.routelk.app.models.User;

public class UserService {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String COLLECTION_NAME = "users";

    public void getAllUsers(OnCompleteListener<QuerySnapshot> callback) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(callback);
    }

    public void getUser(String userId, OnCompleteListener<DocumentSnapshot> callback) {
        db.collection(COLLECTION_NAME)
                .document(userId)
                .get()
                .addOnCompleteListener(callback);
    }

    public void createUser(User user, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(user.getId())
                .set(user)
                .addOnCompleteListener(callback);
    }

    public void updateUser(User user, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(user.getId())
                .set(user)
                .addOnCompleteListener(callback);
    }

    public void deleteUser(String userId, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(userId)
                .delete()
                .addOnCompleteListener(callback);
    }
}