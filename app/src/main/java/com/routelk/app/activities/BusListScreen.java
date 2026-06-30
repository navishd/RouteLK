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
        TextView tvBusCount = findViewById(R.id.tvBusCount);
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
        busList.add(new Bus("1", "Super Line Express", "NB-1234", "Luxury AC", "45"));
        busList.add(new Bus("2", "Kandy Metro", "NC-5678", "Super Luxury", "40"));
        busList.add(new Bus("3", "Southern Link", "ND-9012", "Semi Luxury", "50"));
        busList.add(new Bus("4", "Night Rider", "NE-3456", "Luxury AC", "45"));
        busList.add(new Bus("5", "City Runner", "NF-7890", "Normal", "55"));

        if (tvBusCount != null) {
            tvBusCount.setText(String.valueOf(busList.size()));
        }

        // Set adapter
        BusAdapter busAdapter = new BusAdapter(this, busList);
        busRecyclerView.setAdapter(busAdapter);

        // Back button click listener
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
}