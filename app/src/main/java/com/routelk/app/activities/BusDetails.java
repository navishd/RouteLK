package com.routelk.app.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.routelk.app.R;
import android.content.Intent;

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

                boolean isForOthers = getIntent().getBooleanExtra("IS_FOR_OTHERS", false);
                Intent intent = new Intent(BusDetails.this, SeatSelectionActivity.class);
                intent.putExtra("IS_FOR_OTHERS", isForOthers);

                startActivity(intent);
            });
        }
    }
}
