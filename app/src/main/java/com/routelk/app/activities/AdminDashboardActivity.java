package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;

public class AdminDashboardActivity extends AppCompatActivity {

    private CardView cardManageBuses;
    private CardView cardManageRoutes;
    private CardView cardManageSchedules;
    private CardView cardManageUsers;
    private CardView cardViewBookings;
    private CardView cardReports;

    private TextView tvTotalBuses;
    private TextView tvTotalRoutes;
    private TextView tvTotalSchedules;
    private TextView tvTotalUsers;
    private TextView tvTotalBookings;
    private TextView tvRevenue;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = FirebaseFirestore.getInstance();

        initializeViews();

        setClickListeners();

        loadDashboardData();
    }

    private void initializeViews() {

        cardManageBuses = findViewById(R.id.cardManageBuses);
        cardManageRoutes = findViewById(R.id.cardManageRoutes);
        cardManageSchedules = findViewById(R.id.cardManageSchedules);
        cardManageUsers = findViewById(R.id.cardManageUsers);
        cardViewBookings = findViewById(R.id.cardViewBookings);
        cardReports = findViewById(R.id.cardReports);

        tvTotalBuses = findViewById(R.id.tvTotalBuses);
        tvTotalRoutes = findViewById(R.id.tvTotalRoutes);
        tvTotalSchedules = findViewById(R.id.tvTotalSchedules);
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTotalBookings = findViewById(R.id.tvTotalBookings);
        tvRevenue = findViewById(R.id.tvRevenue);
    }

    private void setClickListeners() {

        cardManageBuses.setOnClickListener(v ->
                startActivity(new Intent(this, ManageBusesActivity.class)));

        cardManageRoutes.setOnClickListener(v ->
                startActivity(new Intent(this, ManageRoutesActivity.class)));

        cardManageSchedules.setOnClickListener(v ->
                startActivity(new Intent(this, ManageSchedulesActivity.class)));

        cardManageUsers.setOnClickListener(v ->
                startActivity(new Intent(this, ManageUsersActivity.class)));

        cardViewBookings.setOnClickListener(v ->
                startActivity(new Intent(this, ViewBookingsActivity.class)));

        cardReports.setOnClickListener(v ->
                startActivity(new Intent(this, ReportsActivity.class)));
    }

    private void loadDashboardData() {

        loadBusCount();
        loadRouteCount();
        loadScheduleCount();
        loadUserCount();
        loadBookingCount();
        loadRevenue();
    }

    // ---------------- BUSES ----------------

    private void loadBusCount() {

        db.collection("buses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->

                        tvTotalBuses.setText("🚌 Total Buses : "
                                + queryDocumentSnapshots.size()));

    }

    // ---------------- ROUTES ----------------

    private void loadRouteCount() {

        db.collection("routes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->

                        tvTotalRoutes.setText("🛣 Total Routes : "
                                + queryDocumentSnapshots.size()));

    }

    // ---------------- SCHEDULES ----------------

    private void loadScheduleCount() {

        db.collection("schedules")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->

                        tvTotalSchedules.setText("🕒 Total Schedules : "
                                + queryDocumentSnapshots.size()));

    }

    // ---------------- USERS ----------------

    private void loadUserCount() {

        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->

                        tvTotalUsers.setText("👤 Total Users : "
                                + queryDocumentSnapshots.size()));

    }

    // ---------------- BOOKINGS ----------------

    private void loadBookingCount() {

        db.collection("bookings")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->

                        tvTotalBookings.setText("🎫 Total Bookings : "
                                + queryDocumentSnapshots.size()));

    }

    // ---------------- REVENUE ----------------

    private void loadRevenue() {

        db.collection("bookings")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    double revenue = 0;

                    for (var doc : queryDocumentSnapshots) {

                        Double price = doc.getDouble("price");

                        if (price != null) {
                            revenue += price;
                        }
                    }

                    tvRevenue.setText("💰 Revenue : Rs." + revenue);

                });

    }
}