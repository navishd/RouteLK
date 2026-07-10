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

                Intent currentIntent = getIntent();
                boolean isForOthers = currentIntent.getBooleanExtra("IS_FOR_OTHERS", false);
                
                Intent intent = new Intent(BusDetails.this, SeatSelectionActivity.class);
                intent.putExtra("IS_FOR_OTHERS", isForOthers);
                intent.putExtra("BUS_ID", currentIntent.getStringExtra("BUS_ID"));
                intent.putExtra("BUS_NAME", currentIntent.getStringExtra("BUS_NAME"));
                intent.putExtra("FROM", currentIntent.getStringExtra("FROM"));
                intent.putExtra("TO", currentIntent.getStringExtra("TO"));
                intent.putExtra("DATE", currentIntent.getStringExtra("DATE"));
                intent.putExtra("TIME", currentIntent.getStringExtra("TIME"));

                startActivity(intent);
            });
        }
    }
}
