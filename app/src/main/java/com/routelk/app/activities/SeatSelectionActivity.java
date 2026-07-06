package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import com.routelk.app.R;

public class SeatSelectionActivity extends AppCompatActivity {

    public static List<String> reservedSeats = new ArrayList<>();
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(v -> {
            boolean isForOthers = getIntent().getBooleanExtra("IS_FOR_OTHERS", false);
            
            if (isForOthers) {
                // If booking for someone else, go to Passenger Details page
                Intent intent = new Intent(SeatSelectionActivity.this, PassengerDetailsScreen.class);
                intent.putExtra("IS_FOR_OTHERS", isForOthers);
                startActivity(intent);
            } else {
                // If booking for self, skip Passenger Details and go directly to Payment
                Intent intent = new Intent(SeatSelectionActivity.this, Payment.class);
                startActivity(intent);
            }
        });
    }
}