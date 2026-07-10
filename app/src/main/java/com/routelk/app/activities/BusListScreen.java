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
        String time = getIntent().getStringExtra("TIME");
        String passengers = getIntent().getStringExtra("PASSENGERS");
        boolean isForOthers = getIntent().getBooleanExtra("IS_FOR_OTHERS", false);

        TextView routeTitle = findViewById(R.id.routeTitle);
        TextView dateSubtitle = findViewById(R.id.dateSubtitle);
        TextView tvPassengersCount = findViewById(R.id.tvPassengersCount);

        if (from != null && to != null) {
            routeTitle.setText(from + " → " + to);
        }
        if (date != null) {
            String subtitle = date;
            if (time != null && !time.isEmpty()) {
                subtitle += " • " + time;
            }
            dateSubtitle.setText(subtitle);
        }
        if (passengers != null) {
            tvPassengersCount.setText(passengers);
        }

        // Set adapter
        List<Bus> busList = new ArrayList<>();
        // Note: For actual usage, you would populate this from Firestore
        BusAdapter busAdapter = new BusAdapter(this, busList, from, to, date, time);
        busRecyclerView.setAdapter(busAdapter);

        // Since BusAdapter is currently for Admin (Edit/Delete), 
        // I will add a direct way to navigate to Details to test the "Booking for others" flow.
        findViewById(R.id.headerCard).setOnClickListener(v -> {
            Intent intent = new Intent(BusListScreen.this, BusDetails.class);
            intent.putExtra("IS_FOR_OTHERS", isForOthers);
            intent.putExtra("FROM", from);
            intent.putExtra("TO", to);
            intent.putExtra("DATE", date);
            intent.putExtra("TIME", time);
            startActivity(intent);
        });

        // Back button click listener
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
}