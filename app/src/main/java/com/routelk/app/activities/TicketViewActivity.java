package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
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
        String time = getIntent().getStringExtra("time");
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
                b.setTime(time != null ? time : "N/A");
                b.setSeatNo(trimmedSeat);
                b.setBusName(bus != null ? bus : "N/A");
                tickets.add(b);
            }
        }

        ViewPager2 viewPager = findViewById(R.id.viewPagerTickets);
        TextView tvTicketCounter = findViewById(R.id.tvTicketCounter);
        ImageView btnPrev = findViewById(R.id.btnPrevTicket);
        ImageView btnNext = findViewById(R.id.btnNextTicket);
        
        TicketPagerAdapter adapter = new TicketPagerAdapter(tickets);
        viewPager.setAdapter(adapter);

        // Update counter on swipe
        if (tvTicketCounter != null) {
            tvTicketCounter.setText("1/" + tickets.size());
            
            if (btnPrev != null) btnPrev.setOnClickListener(v -> viewPager.setCurrentItem(viewPager.getCurrentItem() - 1));
            if (btnNext != null) btnNext.setOnClickListener(v -> viewPager.setCurrentItem(viewPager.getCurrentItem() + 1));

            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    tvTicketCounter.setText((position + 1) + "/" + tickets.size());
                    
                    if (btnPrev != null) {
                        btnPrev.setEnabled(position > 0);
                        btnPrev.setAlpha(position > 0 ? 1.0f : 0.3f);
                    }
                    if (btnNext != null) {
                        btnNext.setEnabled(position < tickets.size() - 1);
                        btnNext.setAlpha(position < tickets.size() - 1 ? 1.0f : 0.3f);
                    }
                }
            });
        }

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
