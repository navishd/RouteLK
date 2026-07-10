package com.routelk.app.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.routelk.app.R;

import java.util.HashMap;

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



        listenUsers();

        listenBuses();

        listenBookings();

    }



    //================ USERS REAL TIME =================

    private void listenUsers(){


        db.collection("users")
                .addSnapshotListener((value, error) -> {


                    if(value != null){


                        int count = value.size();


                        tvUsers.setText(
                                String.valueOf(count)
                        );

                    }

                });


    }





    //================ BUSES REAL TIME =================


    private void listenBuses(){


        db.collection("buses")
                .addSnapshotListener((value, error) -> {


                    if(value != null){


                        int count = value.size();


                        tvBuses.setText(
                                String.valueOf(count)
                        );

                    }


                });



    }





    //================ BOOKINGS REAL TIME =================


    private void listenBookings(){


        db.collection("bookings")
                .addSnapshotListener((value, error) -> {



                    if(value == null)
                        return;



                    int totalBookings = value.size();


                    double totalRevenue = 0;



                    HashMap<String,Integer> routeMap =
                            new HashMap<>();



                    for(QueryDocumentSnapshot document : value){



                        // Revenue

                        Double price =
                                document.getDouble("price");


                        if(price != null){

                            totalRevenue += price;

                        }





                        // Popular Route

                        String route =
                                document.getString("routeName");



                        if(route != null){


                            if(routeMap.containsKey(route)){


                                routeMap.put(
                                        route,
                                        routeMap.get(route)+1
                                );


                            }else{


                                routeMap.put(
                                        route,
                                        1
                                );


                            }

                        }



                    }




                    tvBookings.setText(
                            String.valueOf(totalBookings)
                    );



                    tvRevenue.setText(
                            "Rs. " + totalRevenue
                    );



                    tvTodayBookings.setText(
                            "Today's Bookings : "
                                    + totalBookings
                    );



                    tvMonthlyRevenue.setText(
                            "Monthly Revenue : Rs. "
                                    + totalRevenue
                    );



                    calculatePopularRoute(routeMap);



                });


    }





    private void calculatePopularRoute(
            HashMap<String,Integer> routeMap){



        String popularRoute =
                "No Route";


        int max = 0;



        for(String route : routeMap.keySet()){



            int count =
                    routeMap.get(route);



            if(count > max){


                max = count;

                popularRoute = route;


            }


        }



        tvPopularRoute.setText(
                popularRoute
        );


    }




}