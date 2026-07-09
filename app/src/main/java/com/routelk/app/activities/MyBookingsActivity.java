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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.routelk.app.R;
import com.routelk.app.adapters.BookingAdapter;
import com.routelk.app.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private List<Booking> bookingList;
    private ProgressBar progressBar;
    private TextView tvNoBookings;
    
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_activities);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_activities) {
                startActivity(new Intent(this, MyActivitiesActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_tickets) {
                startActivity(new Intent(this, ManageBookingsActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_account) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        recyclerView = findViewById(R.id.rvMyBookings);
        progressBar = findViewById(R.id.progressBar);
        tvNoBookings = findViewById(R.id.tvNoBookings);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            bookingList = new ArrayList<>();
            adapter = new BookingAdapter(bookingList);
            adapter.setOnViewTicketClickListener(booking -> {
                // Navigate to ManageBookingsActivity for verification
                Intent intent = new Intent(MyBookingsActivity.this, ManageBookingsActivity.class);
                intent.putExtra("booking_id", booking.getId());
                intent.putExtra("from", booking.getFrom());
                intent.putExtra("to", booking.getTo());
                intent.putExtra("date", booking.getDate());
                intent.putExtra("time", booking.getTime());
                intent.putExtra("seat", booking.getSeatNo());
                intent.putExtra("bus", booking.getBusName());
                intent.putExtra("passenger_phone", booking.getPassengerPhone());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }

        loadUserBookings();

        // Handle system back button
        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void loadUserBookings() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please login to see bookings", Toast.LENGTH_SHORT).show();
            return;
        }

        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        String userId = mAuth.getCurrentUser().getUid();

        db.collection("bookings")
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    
                    if (task.isSuccessful()) {
                        bookingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Booking booking = document.toObject(Booking.class);
                            bookingList.add(booking);
                        }
                        
                        if (adapter != null) adapter.notifyDataSetChanged();
                        
                        if (bookingList.isEmpty()) {
                            if (tvNoBookings != null) tvNoBookings.setVisibility(View.VISIBLE);
                        } else {
                            if (tvNoBookings != null) tvNoBookings.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(MyBookingsActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
