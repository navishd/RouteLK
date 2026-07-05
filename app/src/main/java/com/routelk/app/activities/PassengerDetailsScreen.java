package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.routelk.app.R;

public class PassengerDetailsScreen extends AppCompatActivity {

    private EditText etPassengerName, etPassengerAge, etPassengerGender;
    private Button btnConfirmPassenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_details);

        etPassengerName = findViewById(R.id.etPassengerName);
        etPassengerAge = findViewById(R.id.etPassengerAge);
        etPassengerGender = findViewById(R.id.etPassengerGender);
        btnConfirmPassenger = findViewById(R.id.btnConfirmPassenger);

        btnConfirmPassenger.setOnClickListener(v -> {
            // Logic to save passenger details could go here
            
            // Proceed to Payment
            Intent intent = new Intent(PassengerDetailsScreen.this, Payment.class);
            startActivity(intent);
        });
    }
}
