package com.routelk.app.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.routelk.app.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SeatSelectionActivity extends AppCompatActivity {


    private Button continueButton;

    private Set<String> selectedSeats = new HashSet<>();

    private List<Button> seatButtons = new ArrayList<>();

    private FirebaseFirestore db;



    private String busId;
    private String busName;
    private String from;
    private String to;
    private String date;
    private double price;
    private String time;



    public static final Set<String> reservedSeats =
            new HashSet<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_seat_selection);


        db = FirebaseFirestore.getInstance();


        continueButton =
                findViewById(R.id.continueButton);



        Intent intent = getIntent();


        busId =
                intent.getStringExtra("BUS_ID");


        busName =
                intent.getStringExtra("BUS_NAME");


        from =
                intent.getStringExtra("FROM");


        to =
                intent.getStringExtra("TO");


        date =
                intent.getStringExtra("DATE");


        time =
                intent.getStringExtra("TIME");


        price =
                intent.getDoubleExtra(
                        "PRICE",
                        0
                );



        ViewGroup root =
                findViewById(R.id.seatSelectionRoot);



        collectAllSeats(root);



        fetchReservedSeats();



        continueButton.setOnClickListener(v -> {



            if(selectedSeats.isEmpty()){


                Toast.makeText(
                        this,
                        "Select seat first",
                        Toast.LENGTH_SHORT
                ).show();


                return;

            }



            Intent next =
                    new Intent(
                            SeatSelectionActivity.this,
                            Payment.class
                    );



            next.putStringArrayListExtra(
                    "SELECTED_SEATS",
                    new ArrayList<>(selectedSeats)
            );


            next.putExtra(
                    "BUS_ID",
                    busId
            );


            next.putExtra(
                    "BUS_NAME",
                    busName
            );


            next.putExtra(
                    "FROM",
                    from
            );


            next.putExtra(
                    "TO",
                    to
            );


            next.putExtra(
                    "DATE",
                    date
            );


            next.putExtra(
                    "TIME",
                    time
            );


            next.putExtra(
                    "PRICE",
                    price
            );



            startActivity(next);



        });



    }





    @Override
    protected void onResume(){

        super.onResume();

        fetchReservedSeats();

    }







    private void fetchReservedSeats(){



        if(busName == null || date == null){

            refreshSeats();

            return;

        }



        db.collection("bookings")

                .whereEqualTo(
                        "busName",
                        busName
                )

                .whereEqualTo(
                        "date",
                        date
                )

                .get()

                .addOnSuccessListener(snapshot -> {


                    reservedSeats.clear();



                    for(QueryDocumentSnapshot doc : snapshot){


                        String seat =
                                doc.getString("seatNo");


                        if(seat != null){

                            reservedSeats.add(seat);

                        }

                    }



                    refreshSeats();



                });

    }








    private void collectAllSeats(ViewGroup parent){



        for(int i=0;i<parent.getChildCount();i++){


            View child =
                    parent.getChildAt(i);



            if(child instanceof Button){


                Button btn =
                        (Button)child;



                String text =
                        btn.getText().toString();



                if(text.matches("\\d+")){


                    seatButtons.add(btn);


                }


            }


            else if(child instanceof ViewGroup){


                collectAllSeats(
                        (ViewGroup)child
                );


            }


        }


    }









    private void refreshSeats(){


        for(Button seat : seatButtons){


            String number =
                    seat.getText().toString();



            if(reservedSeats.contains(number)){


                setSeatReserved(seat);


            }

            else if(selectedSeats.contains(number)){


                setSeatSelected(seat);


            }

            else{


                setSeatAvailable(seat);


            }


        }


    }









    private void toggleSeat(
            Button seat,
            String number
    ){


        if(selectedSeats.contains(number)){


            selectedSeats.remove(number);


        }

        else{


            selectedSeats.add(number);


        }



        refreshSeats();



        continueButton.setText(
                "Continue ("+
                        selectedSeats.size()
                        +" Selected)"
        );

    }








    private void setSeatAvailable(Button seat){



        seat.setEnabled(true);


        seat.setBackgroundTintList(
                ColorStateList.valueOf(
                        Color.WHITE
                )
        );


        seat.setTextColor(
                Color.BLACK
        );



        seat.setOnClickListener(v ->
                toggleSeat(
                        seat,
                        seat.getText().toString()
                )
        );


    }








    private void setSeatSelected(Button seat){


        seat.setEnabled(true);



        seat.setBackgroundTintList(
                ColorStateList.valueOf(
                        Color.GREEN
                )
        );


        seat.setTextColor(
                Color.WHITE
        );



        seat.setOnClickListener(v ->
                toggleSeat(
                        seat,
                        seat.getText().toString()
                )
        );


    }








    private void setSeatReserved(Button seat){



        seat.setEnabled(false);



        seat.setBackgroundTintList(
                ColorStateList.valueOf(
                        Color.parseColor("#9CA3AF")
                )
        );


        seat.setTextColor(
                Color.WHITE
        );


        seat.setOnClickListener(null);



    }


}