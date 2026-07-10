package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.routelk.app.R;

public class ManageBookingsActivity extends AppCompatActivity {

    private EditText etPhoneNumber;
    private MaterialButton btnContinue;
    private BottomNavigationView bottomNavigationView;

    private String bookingId, from, to, date, time, seat, bus, passengerPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bookings);

        // Get data from Intent
        Intent incomingIntent = getIntent();
        bookingId = incomingIntent.getStringExtra("booking_id");
        from = incomingIntent.getStringExtra("from");
        to = incomingIntent.getStringExtra("to");
        date = incomingIntent.getStringExtra("date");
        time = incomingIntent.getStringExtra("time");
        seat = incomingIntent.getStringExtra("seat");
        bus = incomingIntent.getStringExtra("bus");
        passengerPhone = incomingIntent.getStringExtra("passenger_phone");

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnContinue = findViewById(R.id.btnContinue);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        setupBottomNav();

        btnContinue.setOnClickListener(v -> {
            String enteredPhone = etPhoneNumber.getText().toString().trim();
            if (enteredPhone.isEmpty()) {
                Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verify phone number if we are coming from a specific booking
            if (passengerPhone != null && !passengerPhone.isEmpty()) {
                if (!enteredPhone.equals(passengerPhone)) {
                    Toast.makeText(this, "Phone number does not match this booking", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Navigate to TicketViewActivity for the specific booking
            Intent intent = new Intent(this, TicketViewActivity.class);
            intent.putExtra("booking_id", bookingId != null ? bookingId : "BBK123456");
            intent.putExtra("from", from != null ? from : "Colombo");
            intent.putExtra("to", to != null ? to : "Kandy");
            intent.putExtra("date", date != null ? date : "25 MAY 24");
            intent.putExtra("time", time != null ? time : "07:00 AM");
            intent.putExtra("seat", seat != null ? seat : "28");
            intent.putExtra("bus", bus != null ? bus : "EX-9821");
            
            startActivity(intent);
            finish();
        });
    }

    private void setupBottomNav() {
        bottomNavigationView.setSelectedItemId(R.id.nav_tickets);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, Home.class));
                finish();
                return true;
            } else if (id == R.id.nav_activities) {
                startActivity(new Intent(this, MyActivitiesActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_tickets) {
                return true;
            } else if (id == R.id.nav_account) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
