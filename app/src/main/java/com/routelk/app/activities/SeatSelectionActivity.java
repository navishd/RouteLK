package com.routelk.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatSelectionActivity extends AppCompatActivity {

    private final List<String> selectedSeats = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String bookingFor;
    private String friendName;
    private String friendPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        
        // Get extras from BusDetails
        bookingFor = getIntent().getStringExtra("BOOKING_FOR");
        friendName = getIntent().getStringExtra("FRIEND_NAME");
        friendPhone = getIntent().getStringExtra("FRIEND_PHONE");

        Button continueButton = findViewById(R.id.continueButton);

        // Find all seat buttons and set click listeners
        setupSeatButtons(findViewById(android.R.id.content));

        continueButton.setOnClickListener(v -> {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "Please select at least one seat", Toast.LENGTH_SHORT).show();
                return;
            }
            saveSelectionToFirestore();
        });
    }

    private void setupSeatButtons(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup layout = (ViewGroup) view;
            for (int i = 0; i < layout.getChildCount(); i++) {
                setupSeatButtons(layout.getChildAt(i));
            }
        } else if (view instanceof Button && !(view.getId() == R.id.continueButton)) {
            Button seat = (Button) view;
            String seatNum = seat.getText().toString();
            
            // Only treat numeric buttons as seats
            if (seatNum.matches("\\d+")) {
                // Initialize visual state
                seat.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.WHITE));
                seat.setTextColor(Color.BLACK);

                seat.setOnClickListener(v -> {
                    if (selectedSeats.contains(seatNum)) {
                        selectedSeats.remove(seatNum);
                        seat.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.WHITE));
                        seat.setTextColor(Color.BLACK);
                    } else {
                        selectedSeats.add(seatNum);
                        seat.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#2563EB")));
                        seat.setTextColor(Color.WHITE);
                    }
                });
            }
        }
    }

    private void saveSelectionToFirestore() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please login to book seats", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> booking = new HashMap<>();
        booking.put("userId", userId);
        booking.put("seats", selectedSeats);
        booking.put("timestamp", com.google.firebase.Timestamp.now());
        booking.put("status", "pending");
        booking.put("bookingFor", bookingFor);
        
        if ("FRIEND".equals(bookingFor)) {
            Map<String, Object> friend = new HashMap<>();
            friend.put("name", friendName);
            friend.put("phone", friendPhone);
            booking.put("passengerDetails", friend);
        }

        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(SeatSelectionActivity.this, "Seats selected: " + selectedSeats, Toast.LENGTH_SHORT).show();
                    // Go straight to Payment, skipping PassengerDetailsScreen
                    Intent intent = new Intent(SeatSelectionActivity.this, Payment.class);
                    intent.putExtra("BOOKING_ID", documentReference.getId());
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(SeatSelectionActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}