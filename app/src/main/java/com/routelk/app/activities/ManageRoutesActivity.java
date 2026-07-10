package com.routelk.app.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.routelk.app.R;
import com.routelk.app.adapters.RouteAdapter;
import com.routelk.app.models.Route;
import com.routelk.app.services.RouteService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ManageRoutesActivity extends AppCompatActivity {

    EditText etRouteName, etFrom, etTo, etDistance, etPrice;
    Button addRouteBtn;
    RecyclerView routesRecyclerView;
    List<Route> routeList;
    RouteAdapter routeAdapter;
    RouteService routeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_routes);

        routeService = new RouteService();

        etRouteName = findViewById(R.id.etRouteName);
        etFrom = findViewById(R.id.etFrom);
        etTo = findViewById(R.id.etTo);
        etDistance = findViewById(R.id.etDistance);
        etPrice = findViewById(R.id.etPrice);
        addRouteBtn = findViewById(R.id.addRouteBtn);
        routesRecyclerView = findViewById(R.id.routesRecyclerView);

        routeList = new ArrayList<>();
        routeAdapter = new RouteAdapter(this, routeList);

        routesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        routesRecyclerView.setAdapter(routeAdapter);

        loadRoutes();

        addRouteBtn.setOnClickListener(v -> {
            String name = etRouteName.getText().toString().trim();
            String from = etFrom.getText().toString().trim();
            String to = etTo.getText().toString().trim();
            String distanceStr = etDistance.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();

            if (name.isEmpty() || from.isEmpty() || to.isEmpty() || distanceStr.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int distance = Integer.parseInt(distanceStr);
            int price = Integer.parseInt(priceStr);
            String id = UUID.randomUUID().toString();

            Route route = new Route(id, name, from, to, distance, price);

            routeService.addRoute(route, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Route Added", Toast.LENGTH_SHORT).show();
                    clearFields();
                    loadRoutes();
                } else {
                    Toast.makeText(this, "Failed to add route", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void clearFields() {
        etRouteName.setText("");
        etFrom.setText("");
        etTo.setText("");
        etDistance.setText("");
        etPrice.setText("");
    }

    private void loadRoutes() {
        routeService.getAllRoutes(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                routeList.clear();
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    Route route = doc.toObject(Route.class);
                    if (route != null) {
                        route.setId(doc.getId());
                        routeList.add(route);
                    }
                }
                routeAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Error loading routes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}