package com.routelk.app.services;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.routelk.app.models.Schedule;

public class ScheduleService {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String COLLECTION_NAME = "Schedule";

    public void getAllSchedules(OnCompleteListener<QuerySnapshot> callback) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(callback);
    }

    public void searchSchedules(String from, String to, OnCompleteListener<QuerySnapshot> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("from", from)
                .whereEqualTo("to", to)
                .get()
                .addOnCompleteListener(callback);
    }

    public void addSchedule(Schedule schedule, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(schedule.getId())
                .set(schedule)
                .addOnCompleteListener(callback);
    }

    public void updateSchedule(Schedule schedule, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(schedule.getId())
                .set(schedule)
                .addOnCompleteListener(callback);
    }

    public void deleteSchedule(String scheduleId, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(scheduleId)
                .delete()
                .addOnCompleteListener(callback);
    }
}