package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;
import com.routelk.app.models.Booking;

import java.util.ArrayList;
import java.util.UUID;

public class Payment extends AppCompatActivity {

    private MaterialCardView cardCredit;
    private RadioButton rbCredit;
    private MaterialButton btnPayNow;
    private ArrayList<String> selectedSeats;
    private String busId, busName, from, to, date;
    private String passengerName, passengerPhone;
    private boolean isForOthers;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        selectedSeats = intent.getStringArrayListExtra("SELECTED_SEATS");
        busId = intent.getStringExtra("BUS_ID");
        busName = intent.getStringExtra("BUS_NAME");
        from = intent.getStringExtra("FROM");
        to = intent.getStringExtra("TO");
        date = intent.getStringExtra("DATE");
        isForOthers = intent.getBooleanExtra("IS_FOR_OTHERS", false);
        passengerName = intent.getStringExtra("PASSENGER_NAME");
        passengerPhone = intent.getStringExtra("PASSENGER_PHONE");

        ImageView btnBack = findViewById(R.id.btnBack);
        cardCredit = findViewById(R.id.cardCredit);
        rbCredit = findViewById(R.id.rbCredit);
        btnPayNow = findViewById(R.id.btnPayNow);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (cardCredit != null && rbCredit != null) {
            cardCredit.setStrokeColor(ContextCompat.getColor(this, R.color.primary));
            cardCredit.setStrokeWidth(4);
            cardCredit.setCardElevation(8);
            rbCredit.setChecked(true);
        }

        if (btnPayNow != null) {
            btnPayNow.setOnClickListener(v -> processPayment());
        }
    }

    private void processPayment() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login to confirm booking", Toast.LENGTH_SHORT).show();
            return;
        }

        btnPayNow.setEnabled(false);
        Toast.makeText(this, "Processing Payment...", Toast.LENGTH_SHORT).show();

        String userId = currentUser.getUid();

        // Fetch user name first
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    String userName = documentSnapshot.getString("fullName");
                    String userPhone = documentSnapshot.getString("phone");
                    if (userName == null) userName = "Guest";
                    
                    // If not booking for others, passenger is the user themselves
                    if (!isForOthers) {
                        passengerName = userName;
                        passengerPhone = userPhone != null ? userPhone : "";
                    }
                    
                    saveBookings(userId, userName, passengerName, passengerPhone);
                })
                .addOnFailureListener(e -> {
                    if (!isForOthers && passengerName == null) {
                        passengerName = "User";
                        passengerPhone = "";
                    }
                    saveBookings(userId, "User", passengerName, passengerPhone);
                });
    }

    private void saveBookings(String userId, String userName, String pName, String pPhone) {
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            finish();
            return;
        }

        int totalSeats = selectedSeats.size();
        final int[] savedCount = {0};

        for (String seat : selectedSeats) {
            String bookingId = UUID.randomUUID().toString();
            Booking booking = new Booking(bookingId, userId, userName, pName, pPhone, from, to, busName, seat, date);

            db.collection("bookings").document(bookingId).set(booking)
                    .addOnSuccessListener(unused -> {
                        savedCount[0]++;
                        if (savedCount[0] == totalSeats) {
                            completeBooking();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Payment.this, "Failed to save booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        btnPayNow.setEnabled(true);
                    });
        }
    }

    private void completeBooking() {
        // Confirm booking by adding to local reserved seats (simulating real-time update)
        if (selectedSeats != null) {
            SeatSelectionActivity.reservedSeats.addAll(selectedSeats);
        }

        Intent intent = new Intent(Payment.this, BookingSuccess.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}