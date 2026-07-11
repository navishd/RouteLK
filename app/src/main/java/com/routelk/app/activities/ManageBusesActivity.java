package com.routelk.app.activities;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.routelk.app.adapters.BusAdapter;
import com.routelk.app.models.Bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ManageBusesActivity extends AppCompatActivity {


    private EditText busNameEditText;
    private EditText busNumberEditText;
    private EditText busTypeEditText;
    private EditText totalSeatsEditText;

    private Button addBusBtn;

    private RecyclerView busRecyclerView;


    private FirebaseFirestore db;


    private List<Bus> busList;
    private BusAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_buses);


        db = FirebaseFirestore.getInstance();


        busNameEditText = findViewById(R.id.busNameEditText);
        busNumberEditText = findViewById(R.id.busNumberEditText);
        busTypeEditText = findViewById(R.id.busTypeEditText);
        totalSeatsEditText = findViewById(R.id.totalSeatsEditText);

        addBusBtn = findViewById(R.id.addBusBtn);


        busRecyclerView = findViewById(R.id.busRecyclerView);

        busRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );


        busList = new ArrayList<>();

        adapter = new BusAdapter(
                this,
                busList
        );

        busRecyclerView.setAdapter(adapter);


        loadBuses();



        addBusBtn.setOnClickListener(v -> saveBus());

    }



    private void saveBus(){


        String busName =
                busNameEditText.getText()
                        .toString()
                        .trim();


        String busNumber =
                busNumberEditText.getText()
                        .toString()
                        .trim();


        String busType =
                busTypeEditText.getText()
                        .toString()
                        .trim();


        String totalSeats =
                totalSeatsEditText.getText()
                        .toString()
                        .trim();



        if(TextUtils.isEmpty(busName)
                || TextUtils.isEmpty(busNumber)
                || TextUtils.isEmpty(busType)
                || TextUtils.isEmpty(totalSeats)){


            Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }



        generateNextBusId(new BusIdCallback() {

            @Override
            public void onGenerated(String busId) {


                Map<String,Object> bus =
                        new HashMap<>();


                bus.put("busId", busId);
                bus.put("busName", busName);
                bus.put("busNumber", busNumber);
                bus.put("busType", busType);
                bus.put("totalSeats", totalSeats);



                // Save using B001 as document ID

                db.collection("buses")
                        .document(busId)
                        .set(bus)

                        .addOnSuccessListener(unused -> {


                            Toast.makeText(
                                    ManageBusesActivity.this,
                                    "Bus Added : " + busId,
                                    Toast.LENGTH_SHORT
                            ).show();






                            loadBuses();


                        })

                        .addOnFailureListener(e -> {


                            Toast.makeText(
                                    ManageBusesActivity.this,
                                    e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();


                        });

            }
        });


    }





    private void generateNextBusId(BusIdCallback callback){


        db.collection("buses")
                .orderBy(
                        "busId",
                        Query.Direction.DESCENDING
                )
                .limit(1)
                .get()

                .addOnSuccessListener(snapshot -> {


                    String nextId;


                    if(snapshot.isEmpty()){


                        nextId = "B001";


                    }else{


                        DocumentSnapshot document =
                                snapshot.getDocuments()
                                        .get(0);



                        String lastId =
                                document.getString("busId");



                        if(lastId == null){

                            nextId = "B001";

                        }else{


                            int number =
                                    Integer.parseInt(
                                            lastId.substring(1)
                                    );


                            number++;


                            nextId =
                                    String.format(
                                            "B%03d",
                                            number
                                    );

                        }

                    }



                    callback.onGenerated(nextId);



                });

    }





    private void loadBuses(){

        db.collection("buses")
                .get()

                .addOnSuccessListener(queryDocumentSnapshots -> {


                    busList.clear();


                    for(DocumentSnapshot document : queryDocumentSnapshots){


                        Bus bus =
                                document.toObject(Bus.class);


                        if(bus != null){


                            bus.setBusID(
                                    document.getId()
                            );


                            Long seats =
                                    document.getLong("seatCount");


                            if(seats != null){

                                bus.setSeatCount(
                                        seats.intValue()
                                );

                            }


                            busList.add(bus);

                        }

                    }


                    adapter.notifyDataSetChanged();


                })

                .addOnFailureListener(e -> {

                    Toast.makeText(
                            this,
                            "Failed to load buses : " + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();

                });

    }


    interface BusIdCallback{

        void onGenerated(String busId);

    }

}