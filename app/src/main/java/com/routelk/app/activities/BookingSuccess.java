package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.routelk.app.R;

import java.util.ArrayList;

public class BookingSuccess extends AppCompatActivity {

    private ArrayList<String> selectedSeats;
    private String from, to, date, busName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_success);

        selectedSeats = getIntent().getStringArrayListExtra("SELECTED_SEATS");
        from = getIntent().getStringExtra("FROM");
        to = getIntent().getStringExtra("TO");
        date = getIntent().getStringExtra("DATE");
        busName = getIntent().getStringExtra("BUS_NAME");

        MaterialButton btnViewTicket = findViewById(R.id.btnViewTicket);
        btnViewTicket.setOnClickListener(v -> {
            Intent intent = new Intent(BookingSuccess.this, TicketViewActivity.class);
            intent.putExtra("booking_id", "BBK" + System.currentTimeMillis() / 1000);
            intent.putExtra("from", from != null ? from : "Colombo");
            intent.putExtra("to", to != null ? to : "Kandy");
            intent.putExtra("date", date != null ? date : "25 MAY 24");
            
            String seatsStr = "28";
            if (selectedSeats != null && !selectedSeats.isEmpty()) {
                seatsStr = TextUtils.join(", ", selectedSeats);
            }
            
            intent.putExtra("seat", seatsStr);
            intent.putExtra("bus", busName != null ? busName : "EX-9821");
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
