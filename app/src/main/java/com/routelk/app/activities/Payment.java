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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;
import com.routelk.app.models.Booking;

import android.text.TextUtils;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.UUID;

public class Payment extends AppCompatActivity {

    private MaterialCardView cardCredit;
    private RadioButton rbCredit;
    private MaterialButton btnPayNow;
    private ArrayList<String> selectedSeats;
    private String busId, busName, from, to, date, time;
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
        time = intent.getStringExtra("TIME");
        isForOthers = intent.getBooleanExtra("IS_FOR_OTHERS", false);
        passengerName = intent.getStringExtra("PASSENGER_NAME");
        passengerPhone = intent.getStringExtra("PASSENGER_PHONE");

        // Initialize Summary Views
        TextView tvFrom = findViewById(R.id.tvFrom);
        TextView tvTo = findViewById(R.id.tvTo);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvSeats = findViewById(R.id.tvSeats);
        TextView tvTotalAmount = findViewById(R.id.tvTotalAmount);

        if (tvFrom != null) tvFrom.setText(from != null ? from : "N/A");
        if (tvTo != null) tvTo.setText(to != null ? to : "N/A");
        
        String fullDate = date != null ? date : "N/A";
        if (time != null && !time.isEmpty()) fullDate += " • " + time;
        if (tvDate != null) tvDate.setText(fullDate);

        if (tvSeats != null && selectedSeats != null) {
            tvSeats.setText(TextUtils.join(", ", selectedSeats));
        }

        if (tvTotalAmount != null && selectedSeats != null) {
            // Assuming LKR 625 per seat for demonstration (1250 / 2 = 625)
            int total = selectedSeats.size() * 625;
            tvTotalAmount.setText("LKR " + total);
        }

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

        // Group all seats into one string (e.g., "1, 2")
        String allSeats = android.text.TextUtils.join(", ", selectedSeats);

        String bookingId = UUID.randomUUID().toString();
        Booking booking = new Booking(bookingId, userId, userName, pName, pPhone, from, to, busName, allSeats, date, time, Timestamp.now());

        db.collection("bookings").document(bookingId).set(booking)
                .addOnSuccessListener(unused -> completeBooking())
                .addOnFailureListener(e -> {
                    Toast.makeText(Payment.this, "Failed to save booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnPayNow.setEnabled(true);
                });
    }

    private void completeBooking() {
        // Confirm booking by adding to local reserved seats
        if (selectedSeats != null) {
            SeatSelectionActivity.reservedSeats.addAll(selectedSeats);
        }

        Intent intent = new Intent(Payment.this, BookingSuccess.class);
        intent.putStringArrayListExtra("SELECTED_SEATS", selectedSeats);
        intent.putExtra("FROM", from);
        intent.putExtra("TO", to);
        intent.putExtra("DATE", date);
        intent.putExtra("TIME", time);
        intent.putExtra("BUS_NAME", busName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}