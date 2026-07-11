package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.routelk.app.R;

public class BusDetails extends AppCompatActivity {


    TextView tvBusCompany;
    TextView tvStartTime;
    TextView tvStartLocation;
    TextView tvEndTime;
    TextView tvEndLocation;
    TextView tvPrice;


    String busId;
    String routeId;
    String price;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);



        ImageView btnBack = findViewById(R.id.btnBack);
        MaterialButton btnSelectSeats = findViewById(R.id.btnSelectSeats);



        tvBusCompany = findViewById(R.id.tvBusCompany);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvStartLocation = findViewById(R.id.tvStartLocation);
        tvEndTime = findViewById(R.id.tvEndTime);
        tvEndLocation = findViewById(R.id.tvEndLocation);
        tvPrice = findViewById(R.id.tvPrice);



        // Receive data from AvailableBusAdapter

        Intent intent = getIntent();


        busId = intent.getStringExtra("busId");
        routeId = intent.getStringExtra("routeId");
        price = intent.getStringExtra("price");

        String company =
                intent.getStringExtra("company");

        String departure =
                intent.getStringExtra("departure");

        String arrival =
                intent.getStringExtra("arrival");

        String from =
                intent.getStringExtra("from");

        String to =
                intent.getStringExtra("to");



        if(company != null)
            tvBusCompany.setText(company);



        if(departure != null)
            tvStartTime.setText(departure);



        if(arrival != null)
            tvEndTime.setText(arrival);



        if(from != null)
            tvStartLocation.setText(from);



        if(to != null)
            tvEndLocation.setText(to);



        if(price != null)
            tvPrice.setText(
                    "Rs. " + price
            );




        btnBack.setOnClickListener(v -> finish());



        btnSelectSeats.setOnClickListener(v -> {


            Intent seatIntent =
                    new Intent(
                            BusDetails.this,
                            SeatSelectionActivity.class
                    );


            seatIntent.putExtra(
                    "busId",
                    busId
            );


            seatIntent.putExtra(
                    "routeId",
                    routeId
            );


            seatIntent.putExtra(
                    "price",
                    price
            );


            seatIntent.putExtra(
                    "departure",
                    departure
            );


            seatIntent.putExtra(
                    "arrival",
                    arrival
            );


            boolean isForOthers =
                    getIntent()
                            .getBooleanExtra(
                                    "IS_FOR_OTHERS",
                                    false
                            );


            seatIntent.putExtra(
                    "IS_FOR_OTHERS",
                    isForOthers
            );


            startActivity(seatIntent);


        });


    }


}