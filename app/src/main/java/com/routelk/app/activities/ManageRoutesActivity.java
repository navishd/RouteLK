package com.routelk.app.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;
import com.routelk.app.adapters.RouteAdapter;
import com.routelk.app.models.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageRoutesActivity
        extends AppCompatActivity {

    EditText etRouteName,
            etFrom,
            etTo,
            etDistance,
            etPrice;

    Button addRouteBtn;

    RecyclerView routesRecyclerView;

    List<Route> routeList;

    RouteAdapter routeAdapter;

    FirebaseFirestore db;

    @Override
    protected void onCreate(
            Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_manage_routes);

        db = FirebaseFirestore.getInstance();

        etRouteName =
                findViewById(R.id.etRouteName);

        etFrom =
                findViewById(R.id.etFrom);

        etTo =
                findViewById(R.id.etTo);

        etDistance =
                findViewById(R.id.etDistance);

        etPrice =
                findViewById(R.id.etPrice);

        addRouteBtn =
                findViewById(R.id.addRouteBtn);

        routesRecyclerView =
                findViewById(
                        R.id.routesRecyclerView);

        routeList =
                new ArrayList<>();

        routeAdapter =
                new RouteAdapter(
                        this,
                        routeList);

        routesRecyclerView
                .setLayoutManager(
                        new LinearLayoutManager(
                                this));

        routesRecyclerView
                .setAdapter(routeAdapter);

        loadRoutes();

        addRouteBtn.setOnClickListener(v -> {


            String distanceText =
                    etDistance.getText().toString().trim();

            String priceText =
                    etPrice.getText().toString().trim();


            if(distanceText.isEmpty() || priceText.isEmpty()){

                Toast.makeText(
                        this,
                        "Distance and Price required",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }


            HashMap<String,Object> route =
                    new HashMap<>();


            route.put(
                    "routeName",
                    etRouteName.getText().toString()
            );


            route.put(
                    "from",
                    etFrom.getText().toString()
            );


            route.put(
                    "to",
                    etTo.getText().toString()
            );


            route.put(
                    "distance",
                    Double.parseDouble(distanceText)
            );


            route.put(
                    "price",
                    Double.parseDouble(priceText)
            );


            db.collection("routes")
                    .add(route)
                    .addOnSuccessListener(documentReference -> {

                        Toast.makeText(
                                this,
                                "Route Added",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadRoutes();

                    });

        });
    }


    private void loadRoutes() {

        db.collection("routes")
                .get()
                .addOnSuccessListener(query -> {

                    routeList.clear();

                    Toast.makeText(
                            this,
                            "Firestore Count : " + query.size(),
                            Toast.LENGTH_LONG
                    ).show();


                    for (DocumentSnapshot doc : query.getDocuments()) {

                        Toast.makeText(
                                this,
                                "Doc : " + doc.getId(),
                                Toast.LENGTH_SHORT
                        ).show();


                        Route route = doc.toObject(Route.class);


                        if(route != null){

                            route.setId(doc.getId());

                            routeList.add(route);
                        }
                    }


                    routeAdapter.notifyDataSetChanged();


                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            this,
                            "Error : " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                });
    }
}