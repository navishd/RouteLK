package com.routelk.app.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.routelk.app.R;
import com.routelk.app.adapters.TicketAdapter;
import com.routelk.app.models.Booking;


import java.util.ArrayList;
import java.util.List;



public class TicketsActivity extends AppCompatActivity {


    private RecyclerView rvTickets;

    private TicketAdapter adapter;

    private final List<Booking> ticketList = new ArrayList<>();


    private FirebaseFirestore db;

    private FirebaseAuth auth;


    private ProgressBar progressBar;

    private TextView tvEmpty;


    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tickets);



        db = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();



        initViews();

        setupRecyclerView();

        setupBottomNavigation();

        loadTickets();



    }





    private void initViews(){


        rvTickets = findViewById(R.id.rvTickets);

        progressBar = findViewById(R.id.progressBar);

        tvEmpty = findViewById(R.id.tvEmpty);



    }





    private void setupRecyclerView(){


        rvTickets.setLayoutManager(
                new LinearLayoutManager(this)
        );


        adapter = new TicketAdapter(
                this,
                ticketList
        );


        rvTickets.setAdapter(adapter);



    }







    private void loadTickets(){



        if(auth.getCurrentUser()==null){


            Toast.makeText(
                    this,
                    "Please login first",
                    Toast.LENGTH_SHORT
            ).show();


            return;

        }



        progressBar.setVisibility(View.VISIBLE);



        String userId =
                auth.getCurrentUser().getUid();





        db.collection("bookings")

                .whereEqualTo(
                        "userId",
                        userId
                )

                .get()

                .addOnSuccessListener(queryDocumentSnapshots -> {



                    progressBar.setVisibility(View.GONE);



                    ticketList.clear();




                    for(QueryDocumentSnapshot document :
                            queryDocumentSnapshots){



                        Booking booking =
                                document.toObject(
                                        Booking.class
                                );



                        booking.setId(
                                document.getId()
                        );


                        ticketList.add(booking);



                    }




                    adapter.notifyDataSetChanged();





                    if(ticketList.isEmpty()){


                        tvEmpty.setVisibility(View.VISIBLE);

                        tvEmpty.setText(
                                "No tickets found"
                        );


                    }

                    else{


                        tvEmpty.setVisibility(View.GONE);


                    }





                })


                .addOnFailureListener(e -> {



                    progressBar.setVisibility(View.GONE);


                    Toast.makeText(
                            this,
                            "Error : "+e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();



                });




    }









    private void setupBottomNavigation(){


        bottomNavigationView =
                findViewById(R.id.bottomNavigationView);


        bottomNavigationView.setSelectedItemId(
                R.id.nav_tickets
        );


        bottomNavigationView.setOnItemSelectedListener(item -> {


            int id = item.getItemId();


            if(id == R.id.nav_home){


                startActivity(
                        new Intent(
                                TicketsActivity.this,
                                Home.class
                        )
                );

                finish();

                return true;


            } else if(id == R.id.nav_activities){


                startActivity(
                        new Intent(
                                TicketsActivity.this,
                                MyActivitiesActivity.class
                        )
                );

                finish();

                return true;


            } else if(id == R.id.nav_tickets){


                return true;


            } else if(id == R.id.nav_account){


                startActivity(
                        new Intent(
                                TicketsActivity.this,
                                ProfileActivity.class
                        )
                );

                finish();

                return true;

            }


            return false;

        });

    }




}