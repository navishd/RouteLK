package com.routelk.app.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;
import android.content.Intent;

import com.routelk.app.R;
import com.routelk.app.adapters.BusAdapter;
import com.routelk.app.models.Bus;

import java.util.ArrayList;
import java.util.List;

public class BusListScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bus_list_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        RecyclerView busRecyclerView = findViewById(R.id.busRecyclerView);
        busRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get Search Details
        String from = getIntent().getStringExtra("FROM");
        String to = getIntent().getStringExtra("TO");
        String date = getIntent().getStringExtra("DATE");

        TextView routeTitle = findViewById(R.id.routeTitle);
        TextView dateSubtitle = findViewById(R.id.dateSubtitle);

        if (from != null && to != null) {
            routeTitle.setText(from + " → " + to);
        }
        if (date != null) {
            dateSubtitle.setText(date);
        }

        // Create dummy data
        List<Bus> busList = new ArrayList<>();

        // Set adapter
        BusAdapter busAdapter = new BusAdapter(this, busList);        busRecyclerView.setAdapter(busAdapter);

        // Back button click listener
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
}