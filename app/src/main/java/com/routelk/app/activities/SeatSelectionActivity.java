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


    private FirebaseFirestore db;



    private Set<String> selectedSeats =
            new HashSet<>();


    private List<Button> seatButtons =
            new ArrayList<>();


    public static Set<String> reservedSeats =
            new HashSet<>();


    private String busId;
    private String busName;
    private String from;
    private String to;
    private String date;
    private double price;
    private String time;
    private boolean isForOthers;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        setContentView(
                R.layout.activity_seat_selection
        );



        db =
                FirebaseFirestore.getInstance();




        continueButton =
                findViewById(
                        R.id.continueButton
                );



        getIntentData();




        ViewGroup root =
                findViewById(
                        R.id.seatSelectionRoot
                );



        collectAllSeats(root);




        fetchReservedSeats();





        continueButton.setOnClickListener(v -> {



            if(selectedSeats.isEmpty()){


                Toast.makeText(
                        this,
                        "Please select seat",
                        Toast.LENGTH_SHORT
                ).show();


                return;


            }





            Intent intent =
                    new Intent(
                            SeatSelectionActivity.this,
                            PassengerDetailsScreen.class
                    );

            intent.putExtra("IS_FOR_OTHERS", isForOthers);



            intent.putStringArrayListExtra(
                    "SELECTED_SEATS",
                    new ArrayList<>(selectedSeats)
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
                    "PRICE",
                    price
            );

            intent.putExtra(
                    "TIME",
                    time
            );


            startActivity(intent);



        });



    }








    private void getIntentData(){



        Intent intent =
                getIntent();



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

        time =
                intent.getStringExtra("TIME");

        isForOthers = intent.getBooleanExtra("IS_FOR_OTHERS", false);
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



                        String seatNo =
                                doc.getString(
                                        "seatNo"
                                );



                        if(seatNo != null){


                            reservedSeats.add(
                                    seatNo
                            );


                        }



                    }





                    refreshSeats();



                })



                .addOnFailureListener(e -> {



                    Toast.makeText(
                            this,
                            "Seat loading failed",
                            Toast.LENGTH_SHORT
                    ).show();



                });



    }









    private void collectAllSeats(ViewGroup parent){



        for(int i=0;
            i < parent.getChildCount();
            i++){



            View child =
                    parent.getChildAt(i);




            if(child instanceof Button){



                Button button =
                        (Button) child;



                String text =
                        button.getText()
                                .toString();



                if(text.matches("\\d+")){


                    seatButtons.add(
                            button
                    );


                }



            }

            else if(child instanceof ViewGroup){



                collectAllSeats(
                        (ViewGroup) child
                );


            }



        }



    }









    private void refreshSeats(){



        for(Button seat : seatButtons){



            String number =
                    seat.getText()
                            .toString();




            if(reservedSeats.contains(number)){



                setReserved(seat);



            }

            else if(selectedSeats.contains(number)){



                setSelected(seat);



            }

            else{


                setAvailable(seat);



            }



        }



        updateButton();



    }









    private void toggleSeat(
            Button seat,
            String number
    ){



        if(selectedSeats.contains(number)){



            selectedSeats.remove(
                    number
            );


        }
        else{


            selectedSeats.add(
                    number
            );


        }



        refreshSeats();



    }









    private void updateButton(){



        if(selectedSeats.isEmpty()){


            continueButton.setText(
                    "Continue"
            );


        }
        else{


            continueButton.setText(
                    "Continue ("
                            +
                            selectedSeats.size()
                            +
                            " Selected)"
            );


        }



    }









    private void setAvailable(Button seat){



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









    private void setSelected(Button seat){



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









    private void setReserved(Button seat){



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