package com.routelk.app.activities;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.routelk.app.R;
import com.routelk.app.adapters.AvailableBusAdapter;
import com.routelk.app.models.Bus;


import java.util.ArrayList;


public class BusListScreen extends AppCompatActivity {


    RecyclerView busRecyclerView;

    TextView routeTitle;
    TextView dateSubtitle;
    TextView tvPassengersCount;


    ArrayList<Bus> busList;

    AvailableBusAdapter adapter;


    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_bus_list_screen);



        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );



        busRecyclerView =
                findViewById(R.id.busRecyclerView);


        busRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );



        routeTitle =
                findViewById(R.id.routeTitle);


        dateSubtitle =
                findViewById(R.id.dateSubtitle);


        tvPassengersCount =
                findViewById(R.id.tvPassengersCount);



        // Receive search data

        final String from =
                getIntent().getStringExtra("FROM");


        final String to =
                getIntent().getStringExtra("TO");


        final String date =
                getIntent().getStringExtra("DATE");


        final String time =
                getIntent().getStringExtra("TIME");


        final String passengers =
                getIntent().getStringExtra("PASSENGERS");





        routeTitle.setText(
                from + " → " + to
        );


        dateSubtitle.setText(
                date + " • " + time
        );


        tvPassengersCount.setText(
                passengers
        );




        busList = new ArrayList<>();


        adapter =
                new AvailableBusAdapter(
                        this,
                        busList,
                        from,
                        to,
                        date,
                        time,
                        passengers,
                        ""
                );


        busRecyclerView.setAdapter(adapter);




        db =
                FirebaseFirestore.getInstance();



        searchAvailableBuses(
                from,
                to,
                date
        );



    }





    private void searchAvailableBuses(
            String from,
            String to,
            String date
    ){


        db.collection("schedules")
                .get()
                .addOnSuccessListener(scheduleSnapshot -> {



                    for(DocumentSnapshot scheduleDoc :
                            scheduleSnapshot){



                        String scheduleDate =
                                scheduleDoc.getString(
                                        "travelDate"
                                );



                        String busId =
                                scheduleDoc.getString(
                                        "busId"
                                );



                        String routeId =
                                scheduleDoc.getString(
                                        "routeId"
                                );



                        if(
                                scheduleDate == null ||
                                        busId == null ||
                                        routeId == null
                        ){

                            continue;

                        }



                        // Check date

                        if(
                                !scheduleDate.equals(date)
                        ){

                            continue;

                        }





                        db.collection("routes")
                                .document(routeId)
                                .get()
                                .addOnSuccessListener(routeDoc -> {



                                    String busFrom =
                                            routeDoc.getString(
                                                    "from"
                                            );


                                    String busTo =
                                            routeDoc.getString(
                                                    "to"
                                            );




                                    if(
                                            busFrom == null ||
                                                    busTo == null
                                    ){

                                        return;

                                    }





                                    if(
                                            busFrom.equalsIgnoreCase(from)
                                                    &&
                                                    busTo.equalsIgnoreCase(to)
                                    ){



                                        db.collection("buses")
                                                .document(busId)
                                                .get()
                                                .addOnSuccessListener(busDoc -> {



                                                    if(busDoc.exists()){


                                                        Bus bus =
                                                                new Bus();



                                                        bus.setBusID(
                                                                busId
                                                        );


                                                        bus.setBusName(
                                                                busDoc.getString("busName")
                                                        );


                                                        bus.setBusNumber(
                                                                busDoc.getString("busNumber")
                                                        );


                                                        bus.setBusType(
                                                                busDoc.getString("busType")
                                                        );


                                                        Long seats =
                                                                busDoc.getLong(
                                                                        "seatCount"
                                                                );


                                                        if(seats != null){

                                                            bus.setSeatCount(
                                                                    seats.intValue()
                                                            );

                                                        }



                                                        bus.setFrom(
                                                                busFrom
                                                        );


                                                        bus.setTo(
                                                                busTo
                                                        );



                                                        bus.setRouteId(
                                                                routeId
                                                        );


                                                        bus.setScheduleId(
                                                                scheduleDoc.getId()
                                                        );



                                                        bus.setDepartureTime(
                                                                scheduleDoc.getString(
                                                                        "departureTime"
                                                                )
                                                        );


                                                        bus.setArrivalTime(
                                                                scheduleDoc.getString(
                                                                        "arrivalTime"
                                                                )
                                                        );



                                                        String price =
                                                                String.valueOf(
                                                                        scheduleDoc.getLong("price")
                                                                );


                                                        bus.setPrice(
                                                                price
                                                        );





                                                        busList.add(bus);


                                                        adapter.notifyDataSetChanged();



                                                    }



                                                });



                                    }




                                });



                    }





                })
                .addOnFailureListener(e -> {


                    Toast.makeText(
                            this,
                            e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();


                });


    }



}