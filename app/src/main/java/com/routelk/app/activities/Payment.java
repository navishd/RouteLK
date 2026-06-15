package com.routelk.app.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.routelk.app.R;

public class Payment extends AppCompatActivity {

    private MaterialCardView cardCredit;
    private RadioButton rbCredit;
    private MaterialButton btnPayNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

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
                // Later: Start Payment Success Screen
            });
        }
    }
}
