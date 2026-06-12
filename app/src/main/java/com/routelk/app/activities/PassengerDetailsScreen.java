package com.routelk.app.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.routelk.app.R;

public class PassengerDetailsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_passenger_details_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize ID Type Dropdown
        String[] idTypes = {"NIC", "Passport", "Driving License"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, idTypes);
        AutoCompleteTextView idTypeDropdown = findViewById(R.id.idTypeDropdown);
        idTypeDropdown.setAdapter(adapter);

        // Back button click listener
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // Next button click listener
        findViewById(R.id.nextButton).setOnClickListener(v -> {
            // Logic for the next screen (e.g., Payment) goes here
        });
    }
}