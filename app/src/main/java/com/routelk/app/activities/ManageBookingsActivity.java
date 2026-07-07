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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bookings);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnContinue = findViewById(R.id.btnContinue);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        setupBottomNav();

        btnContinue.setOnClickListener(v -> {
            String phone = etPhoneNumber.getText().toString().trim();
            if (phone.isEmpty()) {
                Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
            } else {
                // Here you would typically send an OTP or lookup the booking
                Toast.makeText(this, "Verification code sent to " + phone, Toast.LENGTH_SHORT).show();
                
                // For now, let's just go to MyBookingsActivity as a simulation
                Intent intent = new Intent(this, MyBookingsActivity.class);
                startActivity(intent);
            }
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
