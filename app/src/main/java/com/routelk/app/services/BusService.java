package com.routelk.app.services;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.routelk.app.models.Bus;

public class BusService {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String COLLECTION_NAME = "buses";

    public void getAllBuses(OnCompleteListener<QuerySnapshot> callback) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(callback);
    }

    public void addBus(Bus bus, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(bus.getBusID())
                .set(bus)
                .addOnCompleteListener(callback);
    }

    public void updateBus(Bus bus, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(bus.getBusID())
                .set(bus)
                .addOnCompleteListener(callback);
    }

    public void deleteBus(String busId, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(busId)
                .delete()
                .addOnCompleteListener(callback);
    }
}