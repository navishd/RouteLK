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
import com.routelk.app.services.UserService;

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



    private ArrayList<String> selectedSeats;


    private String busId;
    private String busName;
    private String from;
    private String to;
    private String date;


    private double price;


    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private UserService userService;


    private String userName = "";
    private String userPhone = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);



        auth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        userService = new UserService();



        initializeViews();


        getData();


        updateUI();



        loadUserDetails();



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



        cardCredit.setStrokeColor(
                ContextCompat.getColor(
                        this,
                        R.color.primary
                )
        );


        rbCredit.setChecked(true);


    }





    private void getData(){


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


        price =
                intent.getDoubleExtra(
                        "PRICE",
                        0
                );


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





    // Get logged user details from Firestore

    private void loadUserDetails(){


        FirebaseUser firebaseUser =
                auth.getCurrentUser();



        if(firebaseUser == null){

            Toast.makeText(
                    this,
                    "Please Login",
                    Toast.LENGTH_SHORT
            ).show();

            return;

        }



        String uid =
                firebaseUser.getUid();



        userService.getUser(
                uid,
                task -> {


                    if(task.isSuccessful()
                            &&
                            task.getResult()!=null){


                        User user =
                                task.getResult()
                                        .toObject(User.class);



                        if(user != null){


                            userName =
                                    user.getFullName();


                            userPhone =
                                    user.getPhone();


                        }


                    }


                }

        );


    }





    private void saveBooking(){



        FirebaseUser firebaseUser =
                auth.getCurrentUser();



        if(firebaseUser == null){

            Toast.makeText(
                    this,
                    "Please Login",
                    Toast.LENGTH_SHORT
            ).show();

            return;

        }



        if(selectedSeats == null ||
                selectedSeats.isEmpty()){


            Toast.makeText(
                    this,
                    "Please Select Seats",
                    Toast.LENGTH_SHORT
            ).show();


            return;

        }





        for(String seat : selectedSeats){


            String bookingId =
                    UUID.randomUUID()
                            .toString();



            Booking booking =
                    new Booking(

                            bookingId,

                            firebaseUser.getUid(),

                            userName,

                            userName,

                            userPhone,

                            from,

                            to,

                            busName,

                            seat,

                            date,

                            Timestamp.now(),

                            "CONFIRMED",

                            price,

                            "Credit Card"

                    );



            db.collection("bookings")
                    .document(bookingId)
                    .set(booking);


        }




        Toast.makeText(
                this,
                "Booking Successful",
                Toast.LENGTH_SHORT
        ).show();




        Intent intent = new Intent(
                Payment.this,
                BookingSuccess.class
        );

// Previous pages remove karanawa
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        intent.putStringArrayListExtra(
                "SELECTED_SEATS",
                selectedSeats
        );

        intent.putExtra("FROM", from);
        intent.putExtra("TO", to);
        intent.putExtra("DATE", date);
        intent.putExtra("BUS_NAME", busName);

        startActivity(intent);
        finish();


        intent.putStringArrayListExtra(
                "SELECTED_SEATS",
                selectedSeats
        );


        startActivity(intent);


        finish();


    }


}