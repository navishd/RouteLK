package com.routelk.app.activities;

import android.app.DatePickerDialog;
import java.util.Calendar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.routelk.app.R;
import com.routelk.app.adapters.ScheduleAdapter;
import com.routelk.app.models.Schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ManageSchedulesActivity extends AppCompatActivity {


    private EditText etBusID;
    private EditText etRouteID;
    private EditText etDeparture;
    private EditText etArrival;
    private EditText etTravelDate;
    private EditText etPrice;


    private Button btnAddSchedule;


    private RecyclerView scheduleRecyclerView;


    private ScheduleAdapter adapter;


    private ArrayList<Schedule> scheduleList;


    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedules);


        db = FirebaseFirestore.getInstance();



        etBusID = findViewById(R.id.etBusID);

        etRouteID = findViewById(R.id.etRouteID);

        etDeparture = findViewById(R.id.etDeparture);

        etArrival = findViewById(R.id.etArrival);

        etTravelDate = findViewById(R.id.etTravelDate);

        etPrice = findViewById(R.id.etPrice);

        etTravelDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog dialog =
                    new DatePickerDialog(
                            ManageSchedulesActivity.this,
                            (view, selectedYear, selectedMonth, selectedDay) -> {

                                String date =
                                        selectedYear + "-" +
                                                String.format("%02d", selectedMonth + 1) + "-" +
                                                String.format("%02d", selectedDay);


                                etTravelDate.setText(date);

                            },
                            year,
                            month,
                            day
                    );


            dialog.show();

        });


        btnAddSchedule =
                findViewById(R.id.btnAddSchedule);



        scheduleRecyclerView =
                findViewById(R.id.scheduleRecyclerView);



        scheduleRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );



        scheduleList = new ArrayList<>();



        adapter = new ScheduleAdapter(
                this,
                scheduleList,
                schedule -> {

                    Toast.makeText(
                            this,
                            "Selected : " + schedule.getId(),
                            Toast.LENGTH_SHORT
                    ).show();

                }
        );



        scheduleRecyclerView.setAdapter(adapter);



        loadSchedules();



        btnAddSchedule.setOnClickListener(v -> {

            addSchedule();

        });


    }






    private void addSchedule(){


        String busID =
                etBusID.getText()
                        .toString()
                        .trim();



        String routeID =
                etRouteID.getText()
                        .toString()
                        .trim();



        String departure =
                etDeparture.getText()
                        .toString()
                        .trim();



        String arrival =
                etArrival.getText()
                        .toString()
                        .trim();



        String travelDate =
                etTravelDate.getText()
                        .toString()
                        .trim();



        String priceText =
                etPrice.getText()
                        .toString()
                        .trim();





        if(busID.isEmpty()
                || routeID.isEmpty()
                || departure.isEmpty()
                || arrival.isEmpty()
                || travelDate.isEmpty()
                || priceText.isEmpty()){


            Toast.makeText(
                    this,
                    "Fill all fields",
                    Toast.LENGTH_SHORT
            ).show();


            return;

        }





        int price =
                Integer.parseInt(priceText);





        generateScheduleId(
                scheduleID -> {



                    Map<String,Object> schedule =
                            new HashMap<>();



                    schedule.put(
                            "id",
                            scheduleID
                    );



                    schedule.put(
                            "busId",
                            busID
                    );



                    schedule.put(
                            "routeId",
                            routeID
                    );



                    schedule.put(
                            "departureTime",
                            departure
                    );



                    schedule.put(
                            "arrivalTime",
                            arrival
                    );



                    schedule.put(
                            "travelDate",
                            travelDate
                    );



                    schedule.put(
                            "price",
                            price
                    );






                    db.collection("schedules")
                            .document(scheduleID)
                            .set(schedule)


                            .addOnSuccessListener(unused -> {


                                Toast.makeText(
                                        this,
                                        "Schedule Added : "
                                                + scheduleID,
                                        Toast.LENGTH_SHORT
                                ).show();



                                clearFields();


                                loadSchedules();


                            });



                });


    }









    private void generateScheduleId(
            ScheduleIdCallback callback){



        db.collection("schedules")
                .orderBy(
                        "id",
                        Query.Direction.DESCENDING
                )
                .limit(1)
                .get()


                .addOnSuccessListener(snapshot -> {



                    String nextID;



                    if(snapshot.isEmpty()){


                        nextID = "S001";


                    }
                    else{


                        DocumentSnapshot doc =
                                snapshot.getDocuments()
                                        .get(0);



                        String lastID =
                                doc.getString("id");



                        if(lastID == null){


                            nextID = "S001";


                        }
                        else{


                            int number =
                                    Integer.parseInt(
                                            lastID.substring(1)
                                    );



                            number++;




                            nextID =
                                    String.format(
                                            "S%03d",
                                            number
                                    );

                        }


                    }



                    callback.onGenerated(nextID);



                });



    }









    private void loadSchedules(){


        db.collection("schedules")
                .get()


                .addOnSuccessListener(snapshot -> {



                    scheduleList.clear();




                    for(DocumentSnapshot doc : snapshot){



                        Schedule schedule =
                                doc.toObject(
                                        Schedule.class
                                );



                        if(schedule != null){



                            schedule.setId(
                                    doc.getId()
                            );



                            scheduleList.add(schedule);


                        }


                    }



                    adapter.notifyDataSetChanged();



                });


    }









    private void clearFields(){


        etBusID.setText("");

        etRouteID.setText("");

        etDeparture.setText("");

        etArrival.setText("");

        etTravelDate.setText("");

        etPrice.setText("");

    }








    interface ScheduleIdCallback{

        void onGenerated(String scheduleID);

    }


}