package com.routelk.app.activities;


import android.content.Intent;
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



    private RecyclerView busRecyclerView;


    private TextView routeTitle;
    private TextView dateSubtitle;
    private TextView tvPassengersCount;



    private ArrayList<Bus> busList;


    private AvailableBusAdapter adapter;


    private FirebaseFirestore db;




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
                safe(from)
                        +
                        " → "
                        +
                        safe(to)
        );



        dateSubtitle.setText(
                safe(date)
                        +
                        " • "
                        +
                        safe(time)
        );



        tvPassengersCount.setText(
                safe(passengers)
        );







        busList =
                new ArrayList<>();




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

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

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





                        if(
                                !scheduleDate.equals(date)
                        ){

                            continue;

                        }






                        db.collection("routes")
                                .document(routeId)
                                .get()
                                .addOnSuccessListener(routeDoc -> {



                                    if(!routeDoc.exists()){

                                        return;

                                    }






                                    final String busFrom =
                                            routeDoc.getString(
                                                    "from"
                                            );



                                    final String busTo =
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
                                            !busFrom.equalsIgnoreCase(
                                                    from
                                            )
                                                    ||
                                                    !busTo.equalsIgnoreCase(
                                                            to
                                                    )
                                    ){

                                        return;

                                    }









                                    db.collection("buses")
                                            .document(busId)
                                            .get()
                                            .addOnSuccessListener(busDoc -> {



                                                if(
                                                        !busDoc.exists()
                                                ){

                                                    return;

                                                }





                                                Bus bus =
                                                        new Bus();





                                                bus.setBusID(
                                                        busId
                                                );



                                                bus.setBusName(
                                                        busDoc.getString(
                                                                "busName"
                                                        )
                                                );



                                                bus.setBusNumber(
                                                        busDoc.getString(
                                                                "busNumber"
                                                        )
                                                );



                                                bus.setBusType(
                                                        busDoc.getString(
                                                                "busType"
                                                        )
                                                );





                                                Long seat =
                                                        busDoc.getLong(
                                                                "seatCount"
                                                        );



                                                if(seat != null){

                                                    bus.setSeatCount(
                                                            seat.intValue()
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






                                                Long price =
                                                        scheduleDoc.getLong(
                                                                "price"
                                                        );



                                                if(price != null){

                                                    bus.setPrice(
                                                            String.valueOf(price)
                                                    );

                                                }
                                                else{

                                                    bus.setPrice(
                                                            "0"
                                                    );

                                                }






                                                busList.add(bus);



                                                adapter.notifyItemInserted(
                                                        busList.size()-1
                                                );





                                            });





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








    private String safe(String value){


        return value == null ? "" : value;


    }





}