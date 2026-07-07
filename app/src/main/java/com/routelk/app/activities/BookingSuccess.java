package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.routelk.app.R;

public class BookingSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_success);

        MaterialButton btnViewTicket = findViewById(R.id.btnViewTicket);
        btnViewTicket.setOnClickListener(v -> {
            Intent intent = new Intent(BookingSuccess.this, MyActivitiesActivity.class);
            startActivity(intent);
            finish();
        });

        MaterialButton btnGoHome = findViewById(R.id.btnGoHome);
        btnGoHome.setOnClickListener(v -> {
            Intent intent = new Intent(BookingSuccess.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}