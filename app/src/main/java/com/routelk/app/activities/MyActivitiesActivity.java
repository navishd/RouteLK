package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.routelk.app.R;
import com.routelk.app.adapters.BookingAdapter;
import com.routelk.app.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class MyActivitiesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private final List<Booking> allBookings = new ArrayList<>();
    private final List<Booking> filteredList = new ArrayList<>();
    private ProgressBar progressBar;
    private View layoutEmpty;
    private TextView tvEmpty;
    private TabLayout tabLayout;
    
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activities);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initViews();
        setupBottomNav();
        setupTabs();
        loadBookings();

        findViewById(R.id.fabVoiceAssistant).setOnClickListener(v -> 
            Toast.makeText(this, "Voice Assistant coming soon", Toast.LENGTH_SHORT).show());
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rvActivities);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        tvEmpty = findViewById(R.id.tvEmpty);
        tabLayout = findViewById(R.id.tabLayout);
        
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingAdapter(filteredList);
        recyclerView.setAdapter(adapter);
    }

    private void setupBottomNav() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_activities);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, Home.class));
                finish();
                return true;
            } else if (id == R.id.nav_activities || id == R.id.nav_tickets) {
                return true;
            } else if (id == R.id.nav_account) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterBookings(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadBookings() {
        if (mAuth.getCurrentUser() == null) return;

        progressBar.setVisibility(View.VISIBLE);
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("bookings")
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        allBookings.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Booking booking = document.toObject(Booking.class);
                            allBookings.add(booking);
                        }
                        filterBookings(tabLayout.getSelectedTabPosition());
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filterBookings(int tabPosition) {
        filteredList.clear();
        Timestamp now = Timestamp.now();

        for (Booking booking : allBookings) {
            // My Bookings (Ongoing/Upcoming) vs Completed Tours
            // Basic heuristic: if timestamp is within last 4 hours or in the future, it's ongoing.
            boolean isOngoing = booking.getTimestamp() != null && 
                               booking.getTimestamp().getSeconds() > (now.getSeconds() - 14400); // 4 hours ago

            if (tabPosition == 0 && isOngoing) {
                filteredList.add(booking);
            } else if (tabPosition == 1 && !isOngoing) {
                filteredList.add(booking);
            }
        }

        adapter.notifyDataSetChanged();
        
        if (filteredList.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText(tabPosition == 0 ? "No active bookings" : "No completed tours");
        } else {
            layoutEmpty.setVisibility(View.GONE);
        }
    }
}
