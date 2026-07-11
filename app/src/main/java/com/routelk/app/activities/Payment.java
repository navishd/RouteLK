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


    private MaterialCardView cardCredit;
    private RadioButton rbCredit;
    private MaterialButton btnPayNow;


    private ArrayList<String> selectedSeats;


    private String busId;
    private String busName;
    private String from;
    private String to;
    private String date;


    private String passengerName;
    private String passengerPhone;


    private boolean isForOthers;



    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UserService userService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_payment);



        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        userService = new UserService();




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



        isForOthers =
                intent.getBooleanExtra(
                        "IS_FOR_OTHERS",
                        false
                );



        passengerName =
                intent.getStringExtra(
                        "PASSENGER_NAME"
                );


        passengerPhone =
                intent.getStringExtra(
                        "PASSENGER_PHONE"
                );





        // Payment summary update

        TextView tvFrom =
                findViewById(R.id.tvFrom);


        TextView tvTo =
                findViewById(R.id.tvTo);


        TextView tvDate =
                findViewById(R.id.tvDate);


        TextView tvSeats =
                findViewById(R.id.tvSeats);




        if(tvFrom != null)
            tvFrom.setText(from);



        if(tvTo != null)
            tvTo.setText(to);



        if(tvDate != null)
            tvDate.setText(date);



        if(tvSeats != null && selectedSeats != null){

            tvSeats.setText(
                    android.text.TextUtils.join(
                            ", ",
                            selectedSeats
                    )
            );

        }






        ImageView btnBack =
                findViewById(R.id.btnBack);



        cardCredit =
                findViewById(R.id.cardCredit);


        rbCredit =
                findViewById(R.id.rbCredit);


        btnPayNow =
                findViewById(R.id.btnPayNow);





        if(btnBack != null){

            btnBack.setOnClickListener(
                    v -> finish()
            );

        }




        if(cardCredit != null){

            cardCredit.setStrokeColor(
                    ContextCompat.getColor(
                            this,
                            R.color.primary
                    )
            );


            cardCredit.setStrokeWidth(4);


            cardCredit.setCardElevation(8);

        }




        if(rbCredit != null){

            rbCredit.setChecked(true);

        }





        btnPayNow.setOnClickListener(
                v -> processPayment()
        );



    }







    private void processPayment(){



        FirebaseUser currentUser =
                mAuth.getCurrentUser();



        if(currentUser == null){


            Toast.makeText(
                    this,
                    "Please login first",
                    Toast.LENGTH_SHORT
            ).show();


            return;

        }





        btnPayNow.setEnabled(false);



        String userId =
                currentUser.getUid();





        userService.getUser(
                userId,
                task -> {



                    String userName =
                            "Guest";


                    String userPhone =
                            "";



                    if(task.isSuccessful()
                            &&
                            task.getResult()!=null){



                        User user =
                                task.getResult()
                                        .toObject(User.class);



                        if(user!=null){


                            userName =
                                    user.getFullName();


                            userPhone =
                                    user.getPhone();

                        }


                    }




                    if(!isForOthers){


                        passengerName =
                                userName;


                        passengerPhone =
                                userPhone;


                    }




                    saveBookings(
                            userId,
                            userName,
                            passengerName,
                            passengerPhone
                    );



                }
        );



    }







    private void saveBookings(
            String userId,
            String userName,
            String pName,
            String pPhone
    ){



        if(selectedSeats == null ||
                selectedSeats.isEmpty()){


            Toast.makeText(
                    this,
                    "No seats selected",
                    Toast.LENGTH_SHORT
            ).show();


            return;

        }




        final int[] count =
                {0};




        for(String seat:selectedSeats){



            String bookingId =
                    UUID.randomUUID()
                            .toString();





            Booking booking =
                    new Booking(

                            bookingId,
                            userId,
                            userName,
                            pName,
                            pPhone,
                            from,
                            to,
                            busName,
                            seat,
                            date,
                            Timestamp.now(),
                            "CONFIRMED",
                            0.0,
                            "Credit Card"

                    );





            db.collection("bookings")
                    .document(bookingId)
                    .set(booking)



                    .addOnSuccessListener(
                            unused -> {


                                count[0]++;


                                if(count[0]
                                        ==
                                        selectedSeats.size()){


                                    completeBooking();


                                }


                            }
                    )



                    .addOnFailureListener(
                            e -> {


                                btnPayNow.setEnabled(true);


                                Toast.makeText(
                                        this,
                                        e.getMessage(),
                                        Toast.LENGTH_SHORT
                                ).show();



                            }
                    );




        }



    }








    private void completeBooking(){



        Intent intent =
                new Intent(
                        Payment.this,
                        BookingSuccess.class
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
                "BUS_NAME",
                busName
        );



        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );



        startActivity(intent);


        finish();


    }


}