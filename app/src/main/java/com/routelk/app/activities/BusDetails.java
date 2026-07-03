package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView tvBusCompany = findViewById(R.id.tvBusCompany);

        Intent intent = getIntent();
        String busId = intent.getStringExtra("BUS_ID");
        String busName = intent.getStringExtra("BUS_NAME");
        String from = intent.getStringExtra("FROM");
        String to = intent.getStringExtra("TO");
        String date = intent.getStringExtra("DATE");

        if (busName != null && tvBusCompany != null) {
            tvBusCompany.setText(busName);
        }

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnSelectSeats != null) {
            btnSelectSeats.setOnClickListener(v -> {
                Intent nextIntent = new Intent(BusDetails.this, SeatSelectionActivity.class);
                nextIntent.putExtra("BUS_ID", busId);
                nextIntent.putExtra("BUS_NAME", busName);
                nextIntent.putExtra("FROM", from);
                nextIntent.putExtra("TO", to);
                nextIntent.putExtra("DATE", date);
                startActivity(nextIntent);
            });
        }
    }
}