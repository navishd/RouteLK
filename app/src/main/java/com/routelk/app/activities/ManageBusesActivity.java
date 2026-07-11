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
    private EditText seatCountEditText;


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



        busNameEditText =
                findViewById(R.id.busNameEditText);


        busNumberEditText =
                findViewById(R.id.busNumberEditText);


        busTypeEditText =
                findViewById(R.id.busTypeEditText);


        seatCountEditText =
                findViewById(R.id.seatCountEditText);



        addBusBtn =
                findViewById(R.id.addBusBtn);



        busRecyclerView =
                findViewById(R.id.busRecyclerView);



        busRecyclerView =
                findViewById(R.id.busRecyclerView);


        busRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );


        busRecyclerView.setHasFixedSize(true);


        busRecyclerView.setNestedScrollingEnabled(true);

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


        String seatText =
                seatCountEditText.getText()
                        .toString()
                        .trim();



        if(TextUtils.isEmpty(busName)
                || TextUtils.isEmpty(busNumber)
                || TextUtils.isEmpty(busType)
                || TextUtils.isEmpty(seatText)){


            Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
            ).show();


            return;

        }




        int seatCount =
                Integer.parseInt(seatText);




        generateNextBusId(busId -> {



            Map<String,Object> bus =
                    new HashMap<>();



            bus.put(
                    "busId",
                    busId
            );


            bus.put(
                    "busName",
                    busName
            );


            bus.put(
                    "busNumber",
                    busNumber
            );


            bus.put(
                    "busType",
                    busType
            );


            // ONLY seatCount
            bus.put(
                    "seatCount",
                    seatCount
            );



            db.collection("buses")
                    .document(busId)
                    .set(bus)


                    .addOnSuccessListener(unused -> {


                        Toast.makeText(
                                this,
                                "Bus Added : " + busId,
                                Toast.LENGTH_SHORT
                        ).show();



                        clearFields();


                        loadBuses();



                    });


        });



    }







    private void generateNextBusId(
            BusIdCallback callback){



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


                    }
                    else{


                        DocumentSnapshot doc =
                                snapshot.getDocuments()
                                        .get(0);



                        String lastId =
                                doc.getString("busId");



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



                    callback.onGenerated(nextId);



                });



    }








    private void loadBuses(){

        db.collection("buses")
                .get()
                .addOnSuccessListener(snapshot -> {


                    busList.clear();

                    android.util.Log.d("BUS_TEST", "Documents = " + snapshot.size());
                    for(DocumentSnapshot document : snapshot){

                        android.util.Log.d("BUS_TEST", document.getId());
                        Bus bus = new Bus();


                        bus.setBusID(
                                document.getId()
                        );


                        bus.setBusName(
                                document.getString("busName")
                        );


                        bus.setBusNumber(
                                document.getString("busNumber")
                        );


                        bus.setBusType(
                                document.getString("busType")
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
                    adapter.notifyDataSetChanged();


                });

    }
    private void clearFields(){


        busNameEditText.setText("");

        busNumberEditText.setText("");

        busTypeEditText.setText("");

        seatCountEditText.setText("");

    }






    interface BusIdCallback{

        void onGenerated(String busId);

    }


}