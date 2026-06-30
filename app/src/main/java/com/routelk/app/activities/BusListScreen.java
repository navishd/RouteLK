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
        String passengers = getIntent().getStringExtra("PASSENGERS");

        TextView routeTitle = findViewById(R.id.routeTitle);
        TextView dateSubtitle = findViewById(R.id.dateSubtitle);
        TextView tvPassengersCount = findViewById(R.id.tvPassengersCount);

        if (from != null && to != null) {
            routeTitle.setText(from + " → " + to);
        }
        if (date != null) {
            dateSubtitle.setText(date);
        }
        if (passengers != null) {
            tvPassengersCount.setText(passengers);
        }

        // Create dummy data
        List<Bus> busList = new ArrayList<>();
        busList.add(new Bus("Super Luxury", "07:00 AM - 10:30 AM", "L01", "LKR 1,250"));
        busList.add(new Bus("Luxury Bus", "08:30 AM - 12:00 PM", "L02", "LKR 1,350"));
        busList.add(new Bus("Semi Luxury", "10:15 AM - 01:45 PM", "S05", "LKR 1,150"));
        busList.add(new Bus("A/C Express", "01:00 PM - 04:30 PM", "E10", "LKR 1,300"));

        // Set adapter
        BusAdapter busAdapter = new BusAdapter(this, busList);        busRecyclerView.setAdapter(busAdapter);

        // Back button click listener
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
}