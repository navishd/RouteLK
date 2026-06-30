package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.routelk.app.R;

public class SearchBusActivity extends AppCompatActivity {

    private Button searchButton;
    private TextInputEditText fromEditText, toEditText, dateEditText;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus);

        // Initialize Views
        searchButton = findViewById(R.id.searchButton);
        fromEditText = findViewById(R.id.fromEditText);
        toEditText = findViewById(R.id.toEditText);
        dateEditText = findViewById(R.id.dateEditText);
        btnBack = findViewById(R.id.btnBack);

        // Pre-fill data from Home
        Intent intentFromHome = getIntent();
        if (intentFromHome != null) {
            fromEditText.setText(intentFromHome.getStringExtra("FROM"));
            toEditText.setText(intentFromHome.getStringExtra("TO"));
            dateEditText.setText(intentFromHome.getStringExtra("DATE"));
        }

        // Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(SearchBusActivity.this, BusListScreen.class);
            // Pass the details to the list screen
            intent.putExtra("FROM", fromEditText.getText().toString());
            intent.putExtra("TO", toEditText.getText().toString());
            intent.putExtra("DATE", dateEditText.getText().toString());
            startActivity(intent);
        });
    }
}