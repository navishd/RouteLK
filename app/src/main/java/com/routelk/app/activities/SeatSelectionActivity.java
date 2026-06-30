package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.routelk.app.R;

public class SeatSelectionActivity extends AppCompatActivity {

    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(SeatSelectionActivity.this, PassengerDetailsScreen.class);
            startActivity(intent);
        });
    }
}