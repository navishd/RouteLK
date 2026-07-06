package com.routelk.app.activities;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.routelk.app.R;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

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

        // Get data from intent
        Intent incomingIntent = getIntent();
        boolean isForOthers = incomingIntent.getBooleanExtra("IS_FOR_OTHERS", false);
        ArrayList<String> selectedSeats = incomingIntent.getStringArrayListExtra("SELECTED_SEATS");
        String busId = incomingIntent.getStringExtra("BUS_ID");
        String busName = incomingIntent.getStringExtra("BUS_NAME");
        String from = incomingIntent.getStringExtra("FROM");
        String to = incomingIntent.getStringExtra("TO");
        String date = incomingIntent.getStringExtra("DATE");
        
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
            String pName = fullNameEditText.getText().toString().trim();
            String pPhone = phoneEditText.getText().toString().trim();

            if (pName.isEmpty() || pPhone.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(PassengerDetailsScreen.this, Payment.class);
            intent.putExtra("IS_FOR_OTHERS", isForOthers);
            intent.putStringArrayListExtra("SELECTED_SEATS", selectedSeats);
            intent.putExtra("BUS_ID", busId);
            intent.putExtra("BUS_NAME", busName);
            intent.putExtra("FROM", from);
            intent.putExtra("TO", to);
            intent.putExtra("DATE", date);
            intent.putExtra("PASSENGER_NAME", pName);
            intent.putExtra("PASSENGER_PHONE", pPhone);
            startActivity(intent);
        });
    }
}