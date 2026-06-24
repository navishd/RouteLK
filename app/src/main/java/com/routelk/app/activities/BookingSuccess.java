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

        MaterialButton btnViewBookings = findViewById(R.id.btnViewBookings);
        btnViewBookings.setOnClickListener(v -> {
            Intent intent = new Intent(BookingSuccess.this, MyBookingsActivity.class);
            startActivity(intent);
            finish();
        });
    }
}