package com.routelk.app.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.routelk.app.R;

import java.util.HashMap;
import java.util.Map;

public class ReportsActivity extends AppCompatActivity {

    private TextView tvRevenue;
    private TextView tvBookings;
    private TextView tvUsers;
    private TextView tvBuses;
    private TextView tvPopularRoute;
    private TextView tvTodayBookings;
    private TextView tvMonthlyRevenue;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        db = FirebaseFirestore.getInstance();

        tvRevenue = findViewById(R.id.tvRevenue);
        tvBookings = findViewById(R.id.tvBookings);
        tvUsers = findViewById(R.id.tvUsers);
        tvBuses = findViewById(R.id.tvBuses);
        tvPopularRoute = findViewById(R.id.tvPopularRoute);
        tvTodayBookings = findViewById(R.id.tvTodayBookings);
        tvMonthlyRevenue = findViewById(R.id.tvMonthlyRevenue);

        loadReports();
        loadPopularRoute();
    }

    private void loadReports() {

        loadUsers();
        loadBuses();
        loadBookings();
        loadPopularRoute();

    }

    //================ USERS =================

    private void loadUsers() {

        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    int totalUsers = queryDocumentSnapshots.size();

                    tvUsers.setText(String.valueOf(totalUsers));

                });

    }

    //================ BUSES =================

    private void loadBuses() {

        db.collection("buses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    int totalBuses = queryDocumentSnapshots.size();

                    tvBuses.setText(String.valueOf(totalBuses));

                });

    }

    //================ BOOKINGS =================

    private void loadBookings() {

        db.collection("bookings")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    int totalBookings = queryDocumentSnapshots.size();

                    double revenue = 0;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        Double price = document.getDouble("price");

                        if (price != null) {
                            revenue += price;
                        }

                    }

                    tvBookings.setText(String.valueOf(totalBookings));

                    tvRevenue.setText("Rs. " + revenue);

                    tvTodayBookings.setText("Today's Bookings : " + totalBookings);

                    tvMonthlyRevenue.setText("Monthly Revenue : Rs. " + revenue);

                });

    }
    private void loadPopularRoute() {

        db.collection("bookings")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    HashMap<String, Integer> routeCount = new HashMap<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        String route = document.getString("routeName");

                        if (route != null) {

                            if (routeCount.containsKey(route)) {
                                routeCount.put(route, routeCount.get(route) + 1);
                            } else {
                                routeCount.put(route, 1);
                            }

                        }
                    }

                    String popularRoute = "No Route";
                    int max = 0;

                    for (String route : routeCount.keySet()) {

                        if (routeCount.get(route) > max) {

                            max = routeCount.get(route);
                            popularRoute = route;

                        }

                    }

                    tvPopularRoute.setText(popularRoute);

                });

    }


}