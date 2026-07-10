package com.routelk.app.services;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.routelk.app.models.Route;

public class RouteService {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String COLLECTION_NAME = "routes";

    public void getAllRoutes(OnCompleteListener<QuerySnapshot> callback) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(callback);
    }

    public void addRoute(Route route, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(route.getId())
                .set(route)
                .addOnCompleteListener(callback);
    }

    public void updateRoute(Route route, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(route.getId())
                .set(route)
                .addOnCompleteListener(callback);
    }

    public void deleteRoute(String routeId, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(routeId)
                .delete()
                .addOnCompleteListener(callback);
    }
}