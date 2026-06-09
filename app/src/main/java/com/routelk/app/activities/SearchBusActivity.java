package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.routelk.app.R;

public class SearchBusActivity extends AppCompatActivity {

    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus);

        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(SearchBusActivity.this, SeatSelectionActivity.class);
            startActivity(intent);
        });
    }
}