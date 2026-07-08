package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.routelk.app.R;
import com.routelk.app.adapters.TicketPagerAdapter;
import com.routelk.app.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class TicketViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_view);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> navigateToHome());

        // Get data
        String bookingId = getIntent().getStringExtra("booking_id");
        String from = getIntent().getStringExtra("from");
        String to = getIntent().getStringExtra("to");
        String date = getIntent().getStringExtra("date");
        String seat = getIntent().getStringExtra("seat"); // Can be "28, 29"
        String bus = getIntent().getStringExtra("bus");

        List<Booking> tickets = new ArrayList<>();
        if (seat != null) {
            String[] seats = seat.split(",");
            for (String s : seats) {
                String trimmedSeat = s.trim();
                // Create a separate booking object for each ticket to be displayed
                Booking b = new Booking();
                b.setId(bookingId + "-" + trimmedSeat);
                b.setFrom(from != null ? from : "N/A");
                b.setTo(to != null ? to : "N/A");
                b.setDate(date != null ? date : "N/A");
                b.setSeatNo(trimmedSeat);
                b.setBusName(bus != null ? bus : "N/A");
                tickets.add(b);
            }
        }

        ViewPager2 viewPager = findViewById(R.id.viewPagerTickets);
        TicketPagerAdapter adapter = new TicketPagerAdapter(tickets);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabIndicator);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Dots are handled by the custom tab background in XML if needed
        }).attach();

        findViewById(R.id.btnDownload).setOnClickListener(v -> 
            Toast.makeText(this, "Downloading all " + tickets.size() + " tickets...", Toast.LENGTH_SHORT).show());
    }

    private void navigateToHome() {
        Intent intent = new Intent(TicketViewActivity.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToHome();
    }
}
