package com.routelk.app.activities;


import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.routelk.app.R;


import java.util.ArrayList;


public class SeatSelectionActivity extends AppCompatActivity {


    ArrayList<String> selectedSeats = new ArrayList<>();


    Button continueButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_seat_selection);



        continueButton =
                findViewById(R.id.continueButton);



        int[] seatIds = {

                R.id.seat1,
                R.id.seat2,
                R.id.seat3,
                R.id.seat4,
                R.id.seat5,
                R.id.seat6,
                R.id.seat7,
                R.id.seat8,
                R.id.seat9,
                R.id.seat10

        };



        for(int id : seatIds){


            Button seat =
                    findViewById(id);



            seat.setOnClickListener(v -> {


                String number =
                        seat.getText().toString();



                if(selectedSeats.contains(number)){


                    selectedSeats.remove(number);

                    seat.setBackgroundTintList(
                            getColorStateList(
                                    R.color.white
                            )
                    );


                }else{


                    selectedSeats.add(number);


                    seat.setBackgroundTintList(
                            getColorStateList(
                                    R.color.green
                            )
                    );

                }



            });


        }





        continueButton.setOnClickListener(v -> {



            if(selectedSeats.size()==0){


                Toast.makeText(
                        this,
                        "Select seat first",
                        Toast.LENGTH_SHORT
                ).show();


                return;

            }



            Toast.makeText(
                    this,
                    "Selected : "+selectedSeats,
                    Toast.LENGTH_LONG
            ).show();



            // next Payment Activity here



        });



    }


}