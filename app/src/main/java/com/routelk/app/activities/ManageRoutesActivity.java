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


public class ManageRoutesActivity extends AppCompatActivity {


    EditText etRouteName, etFrom, etTo, etDistance, etPrice;

    Button addRouteBtn;

    RecyclerView routesRecyclerView;


    ArrayList<Route> routeList;

    RouteAdapter routeAdapter;


    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_routes);



        db = FirebaseFirestore.getInstance();



        etRouteName = findViewById(R.id.etRouteName);
        etFrom = findViewById(R.id.etFrom);
        etTo = findViewById(R.id.etTo);
        etDistance = findViewById(R.id.etDistance);
        etPrice = findViewById(R.id.etPrice);


        addRouteBtn = findViewById(R.id.addRouteBtn);


        routesRecyclerView =
                findViewById(R.id.routesRecyclerView);



        routeList = new ArrayList<>();


        routeAdapter =
                new RouteAdapter(
                        this,
                        routeList
                );


        routesRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );


        routesRecyclerView.setAdapter(routeAdapter);



        loadRoutes();



        addRouteBtn.setOnClickListener(v -> addRoute());

    }





    private void addRoute(){


        String distance =
                etDistance.getText()
                        .toString()
                        .trim();


        String price =
                etPrice.getText()
                        .toString()
                        .trim();



        if(distance.isEmpty() || price.isEmpty()){


            Toast.makeText(
                    this,
                    "Distance and Price required",
                    Toast.LENGTH_SHORT
            ).show();


            return;
        }




        db.collection("routes")
                .get()
                .addOnSuccessListener(snapshot -> {



                    int count =
                            snapshot.size()+1;



                    String routeId =
                            String.format(
                                    "R%03d",
                                    count
                            );




                    HashMap<String,Object> route =
                            new HashMap<>();



                    route.put(
                            "routeID",
                            routeId
                    );


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
                            Double.parseDouble(distance)
                    );


                    route.put(
                            "price",
                            Double.parseDouble(price)
                    );





                    db.collection("routes")
                            .document(routeId)
                            .set(route)
                            .addOnSuccessListener(unused -> {


                                Toast.makeText(
                                        this,
                                        "Route Added : "+routeId,
                                        Toast.LENGTH_SHORT
                                ).show();



                                clearFields();

                                loadRoutes();


                            });



                });


    }






    private void loadRoutes(){


        db.collection("routes")
                .get()
                .addOnSuccessListener(snapshot -> {



                    routeList.clear();



                    for(DocumentSnapshot doc :
                            snapshot){



                        Route route =
                                doc.toObject(Route.class);



                        if(route != null){



                            route.setRouteID(
                                    doc.getString("routeID")
                            );



                            routeList.add(route);


                        }


                    }



                    routeAdapter.notifyDataSetChanged();



                });



    }





    private void clearFields(){


        etRouteName.setText("");
        etFrom.setText("");
        etTo.setText("");
        etDistance.setText("");
        etPrice.setText("");

    }


}