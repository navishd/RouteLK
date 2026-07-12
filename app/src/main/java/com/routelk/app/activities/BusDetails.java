package com.routelk.app.activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
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

    private TextView tvPrice;
    private TextView tvDuration;


    private MaterialButton btnSelectSeats;


    private String busId;
    private String busName;

    private String from;
    private String to;

    private String departure;
    private String arrival;

    private String price;

    private String date;

    private int seats;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bus_details);



        initializeViews();


        getIntentData();


        updateUI();




        btnSelectSeats.setOnClickListener(v -> {


            Toast.makeText(
                    this,
                    "Opening Seat Selection",
                    Toast.LENGTH_SHORT
            ).show();




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
                    "TIME",
                    departure
            );


            intent.putExtra(
                    "DEPARTURE_TIME",
                    departure
            );


            intent.putExtra(
                    "ARRIVAL_TIME",
                    arrival
            );


            double busPrice = 0;

            try {

                busPrice = Double.parseDouble(price);

            }
            catch(Exception e){

                busPrice = 0;

            }


            intent.putExtra(
                    "PRICE",
                    busPrice
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



        tvPrice =
                findViewById(R.id.tvPrice);



        tvDuration =
                findViewById(R.id.tvDuration);



        btnSelectSeats =
                findViewById(R.id.btnSelectSeats);


    }





    private void getIntentData(){



        busId =
                getIntent()
                        .getStringExtra("BUS_ID");



        busName =
                getIntent()
                        .getStringExtra("BUS_NAME");



        from =
                getIntent()
                        .getStringExtra("FROM");



        to =
                getIntent()
                        .getStringExtra("TO");



        departure =
                getIntent()
                        .getStringExtra("DEPARTURE_TIME");



        arrival =
                getIntent()
                        .getStringExtra("ARRIVAL_TIME");



        price =
                getIntent()
                        .getStringExtra("PRICE");



        date =
                getIntent()
                        .getStringExtra("DATE");



        seats =
                getIntent()
                        .getIntExtra(
                                "SEATS",
                                0
                        );


    }





    private void updateUI(){



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



        tvEndTime.setText(
                arrival != null
                        ? arrival
                        : "--"
        );



        tvStartLocation.setText(
                from != null
                        ? from
                        : "--"
        );



        tvEndLocation.setText(
                to != null
                        ? to
                        : "--"
        );



        tvPrice.setText(
                "Rs. " +
                        (price != null ? price : "0")
        );



        tvDuration.setText(
                calculateDuration(
                        departure,
                        arrival
                )
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


        }
        catch(Exception e){


            e.printStackTrace();

            return "--";

        }

    }


    }

