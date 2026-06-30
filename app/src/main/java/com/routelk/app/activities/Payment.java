package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;

public class Payment extends AppCompatActivity {

    private String bookingId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        db = FirebaseFirestore.getInstance();
        bookingId = getIntent().getStringExtra("BOOKING_ID");

        ImageView btnBack = findViewById(R.id.btnBack);
        MaterialCardView cardCredit = findViewById(R.id.cardCredit);
        RadioButton rbCredit = findViewById(R.id.rbCredit);
        MaterialButton btnPayNow = findViewById(R.id.btnPayNow);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Only one payment method available, so it's selected by default
        if (cardCredit != null && rbCredit != null) {
            cardCredit.setStrokeColor(androidx.core.content.ContextCompat.getColor(this, R.color.primary));
            cardCredit.setStrokeWidth(4);
            cardCredit.setCardElevation(8);
            rbCredit.setChecked(true);
        }

        // Pay Now Button
        if (btnPayNow != null) {
            btnPayNow.setOnClickListener(v -> processPayment());
        }
    }

    private void processPayment() {
        Toast.makeText(this, "Processing Payment...", Toast.LENGTH_LONG).show();
        
        if (bookingId != null) {
            db.collection("bookings").document(bookingId)
                    .update("status", "confirmed", "paymentStatus", "paid")
                    .addOnSuccessListener(aVoid -> {
                        Intent intent = new Intent(Payment.this, BookingSuccess.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(Payment.this, "Payment update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Intent intent = new Intent(Payment.this, BookingSuccess.class);
            startActivity(intent);
            finish();
        }
    }
}