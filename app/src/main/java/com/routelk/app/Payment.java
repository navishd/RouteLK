package com.routelk.app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class Payment extends AppCompatActivity {

    private MaterialCardView cardCredit, cardMobile, cardBanking;
    private RadioButton rbCredit, rbMobile, rbBanking;
    private MaterialButton btnPayNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ImageView btnBack = findViewById(R.id.btnBack);
        cardCredit = findViewById(R.id.cardCredit);
        cardMobile = findViewById(R.id.cardMobile);
        cardBanking = findViewById(R.id.cardBanking);

        rbCredit = findViewById(R.id.rbCredit);
        rbMobile = findViewById(R.id.rbMobile);
        rbBanking = findViewById(R.id.rbBanking);

        btnPayNow = findViewById(R.id.btnPayNow);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Payment Method Selection
        if (cardCredit != null) cardCredit.setOnClickListener(v -> selectPayment(cardCredit, rbCredit));
        if (cardMobile != null) cardMobile.setOnClickListener(v -> selectPayment(cardMobile, rbMobile));
        if (cardBanking != null) cardBanking.setOnClickListener(v -> selectPayment(cardBanking, rbBanking));

        // Initial selection
        selectPayment(cardCredit, rbCredit);

        // Pay Now Button
        if (btnPayNow != null) {
            btnPayNow.setOnClickListener(v -> {
                Toast.makeText(this, "Processing Payment...", Toast.LENGTH_LONG).show();
                // Later: Start Payment Success Screen
            });
        }
    }

    private void selectPayment(MaterialCardView selectedLayout, RadioButton selectedRb) {
        if (selectedLayout == null || selectedRb == null) return;

        // Reset all
        resetSelection(cardCredit, rbCredit);
        resetSelection(cardMobile, rbMobile);
        resetSelection(cardBanking, rbBanking);

        // Highlight selected
        selectedLayout.setStrokeColor(getResources().getColor(R.color.primary));
        selectedLayout.setStrokeWidth(4);
        selectedLayout.setCardElevation(8);
        selectedRb.setChecked(true);
    }

    private void resetSelection(MaterialCardView layout, RadioButton rb) {
        if (layout != null) {
            layout.setStrokeColor(android.graphics.Color.parseColor("#F1F5F9"));
            layout.setStrokeWidth(2);
            layout.setCardElevation(2);
        }
        if (rb != null) {
            rb.setChecked(false);
        }
    }
}
