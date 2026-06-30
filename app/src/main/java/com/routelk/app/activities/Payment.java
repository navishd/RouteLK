package com.routelk.app.activities;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.routelk.app.R;

import java.util.ArrayList;

public class Payment extends AppCompatActivity {

    private MaterialCardView cardCredit;
    private RadioButton rbCredit;
    private MaterialButton btnPayNow;
    private ArrayList<String> selectedSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        selectedSeats = getIntent().getStringArrayListExtra("SELECTED_SEATS");

        ImageView btnBack = findViewById(R.id.btnBack);
        cardCredit = findViewById(R.id.cardCredit);
        rbCredit = findViewById(R.id.rbCredit);
        btnPayNow = findViewById(R.id.btnPayNow);

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
            btnPayNow.setOnClickListener(v -> {
                Toast.makeText(this, "Processing Payment...", Toast.LENGTH_LONG).show();
                
                // Confirm booking by adding to reserved seats
                if (selectedSeats != null) {
                    SeatSelectionActivity.reservedSeats.addAll(selectedSeats);
                }

                Intent intent = new Intent(Payment.this, BookingSuccess.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
    }
}
