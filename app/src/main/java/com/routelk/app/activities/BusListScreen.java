package com.routelk.app.activities;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;
import com.routelk.app.adapters.AvailableBusAdapter;
import com.routelk.app.models.Bus;


import java.util.ArrayList;
import java.util.List;



public class BusListScreen extends AppCompatActivity {



    private FirebaseFirestore db;


    private RecyclerView recyclerView;


    private ArrayList<Bus> busList;


    private AvailableBusAdapter adapter;



    private String from;
    private String to;
    private String date;
    private String time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        EdgeToEdge.enable(this);


        setContentView(R.layout.activity_bus_list_screen);





        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v,insets)->{


                    Insets bars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );


                    v.setPadding(
                            bars.left,
                            bars.top,
                            bars.right,
                            bars.bottom
                    );


                    return insets;

                });






        recyclerView =
                findViewById(
                        R.id.busRecyclerView
                );



        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );







        from =
                getIntent()
                        .getStringExtra("FROM");



        to =
                getIntent()
                        .getStringExtra("TO");



        date =
                getIntent()
                        .getStringExtra("DATE");



        time =
                getIntent()
                        .getStringExtra("TIME");








        TextView routeTitle =
                findViewById(
                        R.id.routeTitle
                );



        TextView dateSubtitle =
                findViewById(
                        R.id.dateSubtitle
                );



        TextView passengers =
                findViewById(
                        R.id.tvPassengersCount
                );






        if(from!=null && to!=null){

            routeTitle.setText(
                    from + " → " + to
            );

        }






        if(date!=null){

            dateSubtitle.setText(
                    date
            );

        }







        passengers.setText(
                getIntent()
                        .getStringExtra("PASSENGERS")
        );







        db =
                FirebaseFirestore.getInstance();




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
                        "",
                        ""
                );




        recyclerView.setAdapter(adapter);




        loadAvailableBuses();







        findViewById(
                R.id.backButton
        )
                .setOnClickListener(v->finish());



    }









    private void loadAvailableBuses(){
        Log.d("SEARCH_DATE", date);
        Log.d("SEARCH_FROM", from);
        Log.d("SEARCH_TO", to);


        busList.clear();



        Log.d(
                "SEARCH",
                "DATE : "+date
        );




        db.collection("schedules")

                .whereEqualTo(
                        "travelDate",
                        date
                )


                .get()



                .addOnSuccessListener(
                        schedules -> {



                            if(schedules.isEmpty()){


                                Toast.makeText(
                                        this,
                                        "No buses available",
                                        Toast.LENGTH_SHORT
                                ).show();



                                return;

                            }







                            for(DocumentSnapshot schedule :
                                    schedules){





                                String routeId =
                                        schedule.getString(
                                                "routeId"
                                        );



                                String busId =
                                        schedule.getString(
                                                "busId"
                                        );






                                if(routeId==null
                                        ||
                                        busId==null)

                                    continue;









                                db.collection("routes")

                                        .document(routeId)


                                        .get()


                                        .addOnSuccessListener(
                                                routeDoc->{





                                                    if(!routeDoc.exists())

                                                        return;






                                                    String routeFrom =
                                                            routeDoc.getString(
                                                                    "from"
                                                            );



                                                    String routeTo =
                                                            routeDoc.getString(
                                                                    "to"
                                                            );







                                                    if(routeFrom==null
                                                            ||
                                                            routeTo==null)

                                                        return;








                                                    if(!routeFrom.trim()
                                                            .equalsIgnoreCase(
                                                                    from.trim()
                                                            ))

                                                        return;






                                                    if(!routeTo.trim()
                                                            .equalsIgnoreCase(
                                                                    to.trim()
                                                            ))

                                                        return;









                                                    db.collection("buses")

                                                            .document(busId)


                                                            .get()


                                                            .addOnSuccessListener(
                                                                    busDoc->{







                                                                        if(!busDoc.exists())

                                                                            return;







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








                                                                        Long seats =
                                                                                busDoc.getLong(
                                                                                        "seatCount"
                                                                                );



                                                                        if(seats!=null){


                                                                            bus.setSeatCount(
                                                                                    seats.intValue()
                                                                            );

                                                                        }







                                                                        bus.setFrom(
                                                                                routeFrom
                                                                        );



                                                                        bus.setTo(
                                                                                routeTo
                                                                        );






                                                                        bus.setDepartureTime(
                                                                                schedule.getString(
                                                                                        "departureTime"
                                                                                )
                                                                        );




                                                                        bus.setArrivalTime(
                                                                                schedule.getString(
                                                                                        "arrivalTime"
                                                                                )
                                                                        );







                                                                        Number price =
                                                                                (Number)
                                                                                        schedule.get(
                                                                                                "price"
                                                                                        );



                                                                        if(price!=null){


                                                                            bus.setPrice(
                                                                                    price.doubleValue()
                                                                            );

                                                                        }






                                                                        bus.setRouteId(
                                                                                routeId
                                                                        );



                                                                        bus.setScheduleId(
                                                                                schedule.getId()
                                                                        );







                                                                        busList.add(bus);




                                                                        adapter.notifyDataSetChanged();






                                                                    });





                                                });





                            }





                        })



                .addOnFailureListener(
                        e->{


                            Log.e(
                                    "BUS_ERROR",
                                    e.getMessage()
                            );



                            Toast.makeText(
                                    this,
                                    e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();


                        });




    }



}