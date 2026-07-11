package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class BusDetails extends AppCompatActivity {


    private TextView tvBusCompany;
    private TextView tvStartTime;
    private TextView tvStartLocation;
    private TextView tvEndTime;
    private TextView tvEndLocation;
    private TextView tvDuration;
    private TextView tvPrice;


    private MaterialButton btnSelectSeats;


    private FirebaseFirestore db;


    private String busId;
    private String busName;

    private String from;
    private String to;

    private String departure;
    private String arrival;

    private String date;

    private double price;

    private int seats;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bus_details);


        db = FirebaseFirestore.getInstance();


        initializeViews();


        getIntentData();


        setData();



        btnSelectSeats.setOnClickListener(v -> {


            Intent intent =
                    new Intent(
                            BusDetails.this,
                            SeatSelectionActivity.class
                    );


            intent.putExtra(
                    "BUS_ID",
                    busId
            );


            intent.putExtra(
                    "BUS_NAME",
                    busName
            );


            intent.putExtra(
                    "FROM",
                    from
            );


            intent.putExtra(
                    "TO",
                    to
            );


            intent.putExtra(
                    "DATE",
                    date
            );


            intent.putExtra(
                    "DEPARTURE_TIME",
                    departure
            );


            intent.putExtra(
                    "ARRIVAL_TIME",
                    arrival
            );


            intent.putExtra(
                    "PRICE",
                    price
            );


            intent.putExtra(
                    "SEATS",
                    seats
            );


            startActivity(intent);


        });


    }




    private void initializeViews(){


        tvBusCompany =
                findViewById(R.id.tvBusCompany);


        tvStartTime =
                findViewById(R.id.tvStartTime);


        tvStartLocation =
                findViewById(R.id.tvStartLocation);


        tvEndTime =
                findViewById(R.id.tvEndTime);


        tvEndLocation =
                findViewById(R.id.tvEndLocation);


        tvDuration =
                findViewById(R.id.tvDuration);


        tvPrice =
                findViewById(R.id.tvPrice);


        btnSelectSeats =
                findViewById(R.id.btnSelectSeats);

    }





    private void getIntentData(){


        Intent intent = getIntent();


        busId =
                intent.getStringExtra("BUS_ID");


        busName =
                intent.getStringExtra("BUS_NAME");


        from =
                intent.getStringExtra("FROM");


        to =
                intent.getStringExtra("TO");


        departure =
                intent.getStringExtra("DEPARTURE_TIME");


        arrival =
                intent.getStringExtra("ARRIVAL_TIME");


        date =
                intent.getStringExtra("DATE");


        price =
                intent.getDoubleExtra(
                        "PRICE",
                        0
                );


        seats =
                intent.getIntExtra(
                        "SEATS",
                        50
                );

    }





    private void setData(){


        tvBusCompany.setText(
                busName != null
                        ? busName
                        : "Bus"
        );


        tvStartTime.setText(
                departure != null
                        ? departure
                        : "--"
        );


        tvStartLocation.setText(
                from != null
                        ? from
                        : "--"
        );


        tvEndTime.setText(
                arrival != null
                        ? arrival
                        : "--"
        );


        tvEndLocation.setText(
                to != null
                        ? to
                        : "--"
        );


        tvDuration.setText(
                calculateDuration(
                        departure,
                        arrival
                )
        );


        tvPrice.setText(
                "Rs. " + price
        );

    }





    private String calculateDuration(
            String start,
            String end
    ){

        try {


            SimpleDateFormat sdf =
                    new SimpleDateFormat(
                            "h.mm a",
                            Locale.ENGLISH
                    );


            Date startDate =
                    sdf.parse(start);


            Date endDate =
                    sdf.parse(end);



            long difference =
                    endDate.getTime()
                            -
                            startDate.getTime();



            // next day journey
            if(difference < 0){

                difference +=
                        24 * 60 * 60 * 1000;

            }



            long hours =
                    difference /
                            (1000 * 60 * 60);



            long minutes =
                    (difference /
                            (1000 * 60))
                            % 60;



            return hours +
                    "h " +
                    minutes +
                    "m";



        }catch(Exception e){


            return "--";

        }

    }


}