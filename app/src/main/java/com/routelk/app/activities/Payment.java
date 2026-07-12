package com.routelk.app.activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import com.routelk.app.R;
import com.routelk.app.models.Booking;
import com.routelk.app.models.User;

import java.util.ArrayList;
import java.util.UUID;


public class Payment extends AppCompatActivity {


    private TextView tvFrom;
    private TextView tvTo;
    private TextView tvDate;
    private TextView tvSeats;
    private TextView tvTotalAmount;


    private MaterialButton btnPayNow;

    private MaterialCardView cardCredit;

    private RadioButton rbCredit;


    private FirebaseAuth auth;

    private FirebaseFirestore db;



    private ArrayList<String> selectedSeats;



    private String busId;
    private String busName;

    private String from;
    private String to;

    private String date;
    private String time;


    private double price;



    private String passengerName;
    private String passengerPhone;
    private String passengerEmail;

    private String bookingId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);



        auth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();



        initializeViews();

        getIntentData();

        updateUI();



        btnPayNow.setOnClickListener(v -> {

            saveBooking();

        });


    }





    private void initializeViews(){


        tvFrom = findViewById(R.id.tvFrom);

        tvTo = findViewById(R.id.tvTo);

        tvDate = findViewById(R.id.tvDate);

        tvSeats = findViewById(R.id.tvSeats);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);



        btnPayNow = findViewById(R.id.btnPayNow);



        cardCredit = findViewById(R.id.cardCredit);

        rbCredit = findViewById(R.id.rbCredit);



        if(cardCredit != null){

            cardCredit.setStrokeColor(
                    ContextCompat.getColor(
                            this,
                            R.color.primary
                    )
            );

        }



        if(rbCredit != null){

            rbCredit.setChecked(true);

        }



        ImageView btnBack =
                findViewById(R.id.btnBack);



        if(btnBack != null){

            btnBack.setOnClickListener(v -> finish());

        }


    }







    private void getIntentData(){


        Intent intent = getIntent();



        selectedSeats =
                intent.getStringArrayListExtra(
                        "SELECTED_SEATS"
                );



        busId =
                intent.getStringExtra(
                        "BUS_ID"
                );



        busName =
                intent.getStringExtra(
                        "BUS_NAME"
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



        price =
                intent.getDoubleExtra(
                        "PRICE",
                        0
                );

        passengerName = intent.getStringExtra("PASSENGER_NAME");
        passengerPhone = intent.getStringExtra("PASSENGER_PHONE");
        passengerEmail = intent.getStringExtra("PASSENGER_EMAIL");
    }







    private void updateUI(){


        tvFrom.setText(
                from != null ? from : "--"
        );


        tvTo.setText(
                to != null ? to : "--"
        );


        tvDate.setText(
                date != null ? date : "--"
        );



        if(selectedSeats != null){


            tvSeats.setText(
                    String.join(
                            ", ",
                            selectedSeats
                    )
            );



            double total =
                    price *
                            selectedSeats.size();



            tvTotalAmount.setText(
                    "LKR " +
                            String.format(
                                    "%.2f",
                                    total
                            )
            );


        }


    }








    private void saveBooking(){



        FirebaseUser currentUser =
                auth.getCurrentUser();



        if(currentUser == null){


            Toast.makeText(
                    this,
                    "Please login first",
                    Toast.LENGTH_SHORT
            ).show();


            return;

        }




        String uid =
                currentUser.getUid();




        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(snapshot -> {

                    String currentUserName = "";
                    User user =
                            snapshot.toObject(
                                    User.class
                            );

                    if(user != null){
                        currentUserName = user.getFullName();
                    }

                    createBooking(uid, currentUserName);

                });



    }









    private void createBooking(String uid, String currentUserName){

        if(selectedSeats == null ||
                selectedSeats.isEmpty()){
            Toast.makeText(
                    this,
                    "No seat selected",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        bookingId =
                UUID.randomUUID()
                        .toString();

        for(String seat : selectedSeats){

            String documentId =
                    bookingId + "-" + seat;

                    Booking booking =
                            new Booking(
                                    documentId,
                                    uid,
                                    currentUserName,
                                    passengerName,
                                    passengerPhone,
                                    passengerEmail,
                                    from,
                                    to,
                                    busName,
                                    seat,
                                    date,
                                    Timestamp.now(),
                                    "CONFIRMED",
                                    price,
                                    time
                            );

            db.collection("bookings")
                    .document(documentId)
                    .set(booking);
        }






        Toast.makeText(
                this,
                "Booking Successful",
                Toast.LENGTH_SHORT
        ).show();






        Intent intent =
                new Intent(
                        Payment.this,
                        BookingSuccess.class
                );



        intent.putExtra(
                "BOOKING_ID",
                bookingId
        );



        intent.putStringArrayListExtra(
                "SELECTED_SEATS",
                selectedSeats
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



        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );



        startActivity(intent);



        finish();


    }


}