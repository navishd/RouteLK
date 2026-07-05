package com.routelk.app.activities;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.routelk.app.R;
import android.widget.EditText;
import android.widget.TextView;

public class PassengerDetailsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_passenger_details_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // Check if booking for someone else
        boolean isForOthers = getIntent().getBooleanExtra("IS_FOR_OTHERS", false);
        
        EditText fullNameEditText = findViewById(R.id.fullNameEditText);
        EditText phoneEditText = findViewById(R.id.phoneEditText);
        TextView passengerLabel = findViewById(R.id.passengerLabel);

        if (isForOthers) {
            passengerLabel.setText("Passenger Details");
            fullNameEditText.setText(""); // Clear default value
            phoneEditText.setText(""); // Clear default value
        }

        // Initialize ID Type Dropdown
        String[] idTypes = {"NIC", "Passport", "Driving License"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, idTypes);
        AutoCompleteTextView idTypeDropdown = findViewById(R.id.idTypeDropdown);
        idTypeDropdown.setAdapter(adapter);

        // Back button click listener
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // Next button click listener
        findViewById(R.id.nextButton).setOnClickListener(v -> {
            Intent intent = new Intent(PassengerDetailsScreen.this, Payment.class);
            startActivity(intent);
        });
    }
}