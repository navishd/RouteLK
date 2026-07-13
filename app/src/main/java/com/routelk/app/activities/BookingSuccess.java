package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.routelk.app.R;

import java.util.ArrayList;


public class BookingSuccess extends AppCompatActivity {


    private Button btnViewTicket;
    private Button btnGoHome;

    private TextView tvBookingId;
    private TextView tvBookingDetailsSummary;


    private String bookingId;
    private ArrayList<String> seats;

    private String from;
    private String to;
    private String date;
    private String time;
    private String busName;

    private double price;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_booking_success);



        tvBookingId =
                findViewById(R.id.tvBookingId);

        tvBookingDetailsSummary =
                findViewById(R.id.tvBookingDetailsSummary);


        btnViewTicket =
                findViewById(R.id.btnViewTicket);


        btnGoHome =
                findViewById(R.id.btnGoHome);



        getData();



        tvBookingId.setText(
                bookingId
        );

        if (tvBookingDetailsSummary != null) {
            String summary = String.format("%s to %s\n%s at %s", 
                    from != null ? from : "", 
                    to != null ? to : "", 
                    date != null ? date : "", 
                    time != null ? time : "");
            tvBookingDetailsSummary.setText(summary);
        }



        btnViewTicket.setOnClickListener(v -> {


            Intent intent =
                    new Intent(
                            BookingSuccess.this,
                            TicketViewActivity.class
                    );


            intent.putExtra(
                    "BOOKING_ID",
                    bookingId
            );


            intent.putStringArrayListExtra(
                    "SELECTED_SEATS",
                    seats
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
                    time
            );


            intent.putExtra(
                    "BUS_NAME",
                    busName
            );


            intent.putExtra(
                    "PRICE",
                    price
            );



            startActivity(intent);



        });



        btnGoHome.setOnClickListener(v -> {


            Intent intent =
                    new Intent(
                            BookingSuccess.this,
                            Home.class
                    );


            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );


            startActivity(intent);


        });



    }




    private void getData(){


        Intent intent =
                getIntent();



        bookingId =
                intent.getStringExtra(
                        "BOOKING_ID"
                );


        seats =
                intent.getStringArrayListExtra(
                        "SELECTED_SEATS"
                );


        from =
                intent.getStringExtra(
                        "FROM"
                );


        to =
                intent.getStringExtra(
                        "TO"
                );


        date =
                intent.getStringExtra(
                        "DATE"
                );


        time =
                intent.getStringExtra(
                        "TIME"
                );


        busName =
                intent.getStringExtra(
                        "BUS_NAME"
                );


        price =
                intent.getDoubleExtra(
                        "PRICE",
                        0
                );


    }


}