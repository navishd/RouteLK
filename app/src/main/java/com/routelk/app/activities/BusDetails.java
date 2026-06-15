package com.routelk.app.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.routelk.app.R;

public class BusDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        ImageView btnBack = findViewById(R.id.btnBack);
        MaterialButton btnSelectSeats = findViewById(R.id.btnSelectSeats);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnSelectSeats != null) {
            btnSelectSeats.setOnClickListener(v -> {
                Toast.makeText(this, "Opening Seat Selection...", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
