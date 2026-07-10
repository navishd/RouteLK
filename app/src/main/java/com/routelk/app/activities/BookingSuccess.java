package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.routelk.app.R;

import java.util.ArrayList;

public class BookingSuccess extends AppCompatActivity {

    private ArrayList<String> selectedSeats;
    private String from, to, date, time, busName;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable autoNavigateRunnable = this::navigateToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_success);

        selectedSeats = getIntent().getStringArrayListExtra("SELECTED_SEATS");
        from = getIntent().getStringExtra("FROM");
        to = getIntent().getStringExtra("TO");
        date = getIntent().getStringExtra("DATE");
        time = getIntent().getStringExtra("TIME");
        busName = getIntent().getStringExtra("BUS_NAME");

        TextView tvBookingId = findViewById(R.id.tvBookingId);
        String bookingId = "BBK" + System.currentTimeMillis() / 1000;
        if (tvBookingId != null) {
            tvBookingId.setText(bookingId);
        }

        MaterialButton btnViewTicket = findViewById(R.id.btnViewTicket);
        btnViewTicket.setOnClickListener(v -> {
            handler.removeCallbacks(autoNavigateRunnable);
            Intent intent = new Intent(BookingSuccess.this, TicketViewActivity.class);
            intent.putExtra("booking_id", bookingId);
            intent.putExtra("from", from != null ? from : "Colombo");
            intent.putExtra("to", to != null ? to : "Kandy");
            intent.putExtra("date", date != null ? date : "25 MAY 24");
            intent.putExtra("time", time != null ? time : "07:00 AM");
            
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
        btnGoHome.setOnClickListener(v -> navigateToHome());

        // Handle back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToHome();
            }
        });

        // Automatically navigate to Home after 5 seconds
        handler.postDelayed(autoNavigateRunnable, 5000);
    }

    private void navigateToHome() {
        handler.removeCallbacks(autoNavigateRunnable);
        if (!isFinishing()) {
            Intent intent = new Intent(BookingSuccess.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(autoNavigateRunnable);
    }
}
