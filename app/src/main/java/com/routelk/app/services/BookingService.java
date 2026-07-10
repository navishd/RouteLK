package com.routelk.app.services;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.routelk.app.models.Booking;

public class BookingService {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String COLLECTION_NAME = "bookings";

    public void createBooking(Booking booking, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(booking.getId())
                .set(booking)
                .addOnCompleteListener(callback);
    }

    public void cancelBooking(String bookingId, OnCompleteListener<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(bookingId)
                .delete()
                .addOnCompleteListener(callback);
    }

    public void getBookingsByUser(String userId, OnCompleteListener<QuerySnapshot> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(callback);
    }

    public void isSeatBooked(String busName, String date, String seatNo, OnCompleteListener<QuerySnapshot> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("busName", busName)
                .whereEqualTo("date", date)
                .whereEqualTo("seatNo", seatNo)
                .get()
                .addOnCompleteListener(callback);
    }

    public void getBookingsByBusAndDate(String busName, String date, OnCompleteListener<QuerySnapshot> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("busName", busName)
                .whereEqualTo("date", date)
                .get()
                .addOnCompleteListener(callback);
    }
}