package com.routelk.app.activities;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import com.routelk.app.R;
import com.routelk.app.adapters.TicketPagerAdapter;
import com.routelk.app.models.Booking;


import java.util.ArrayList;
import java.util.List;



public class TicketViewActivity extends AppCompatActivity {



    private ViewPager2 viewPager;

    private TextView tvTicketCounter;

    private ImageView btnPrevTicket;
    private ImageView btnNextTicket;

    private TabLayout tabIndicator;


    private FirebaseFirestore db;



    private ArrayList<Booking> tickets =
            new ArrayList<>();


    private String bookingId;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(
                R.layout.activity_ticket_view
        );



        db =
                FirebaseFirestore.getInstance();



        initializeViews();



        bookingId =
                getIntent()
                        .getStringExtra(
                                "BOOKING_ID"
                        );



        if(bookingId == null){


            Toast.makeText(
                    this,
                    "Booking ID missing",
                    Toast.LENGTH_SHORT
            ).show();


            finish();

            return;

        }



        loadTicket();



        ImageView btnBack =
                findViewById(R.id.btnBack);



        if(btnBack != null){


            btnBack.setOnClickListener(
                    v -> finish()
            );


        }



    }









    private void initializeViews(){



        viewPager =
                findViewById(
                        R.id.viewPagerTickets
                );



        tvTicketCounter =
                findViewById(
                        R.id.tvTicketCounter
                );



        btnPrevTicket =
                findViewById(
                        R.id.btnPrevTicket
                );



        btnNextTicket =
                findViewById(
                        R.id.btnNextTicket
                );



        tabIndicator =
                findViewById(
                        R.id.tabIndicator
                );


    }









    private void loadTicket(){



        db.collection("bookings")
                .whereGreaterThanOrEqualTo(
                        "id",
                        bookingId
                )
                .get()
                .addOnSuccessListener(snapshot -> {



                    tickets.clear();



                    for(QueryDocumentSnapshot doc : snapshot){



                        String id =
                                doc.getString(
                                        "id"
                                );



                        if(id != null &&
                                id.startsWith(bookingId)){



                            Booking booking =
                                    doc.toObject(
                                            Booking.class
                                    );



                            tickets.add(
                                    booking
                            );


                        }



                    }




                    if(tickets.isEmpty()){


                        Toast.makeText(
                                this,
                                "Ticket not found",
                                Toast.LENGTH_SHORT
                        ).show();


                        return;

                    }




                    setupPager();



                })
                .addOnFailureListener(e -> {


                    Toast.makeText(
                            this,
                            "Loading failed",
                            Toast.LENGTH_SHORT
                    ).show();


                });



    }









    private void setupPager(){



        TicketPagerAdapter adapter =
                new TicketPagerAdapter(
                        tickets
                );



        viewPager.setAdapter(
                adapter
        );



        tvTicketCounter.setText(
                "1/" + tickets.size()
        );



        new TabLayoutMediator(
                tabIndicator,
                viewPager,
                (tab, position) -> {

                }

        ).attach();





        viewPager.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {


                    @Override
                    public void onPageSelected(
                            int position
                    ) {


                        super.onPageSelected(position);



                        tvTicketCounter.setText(
                                (position + 1)
                                        +
                                        "/"
                                        +
                                        tickets.size()
                        );



                    }


                }

        );





        btnPrevTicket.setOnClickListener(v -> {


            int current =
                    viewPager.getCurrentItem();



            if(current > 0){


                viewPager.setCurrentItem(
                        current - 1
                );


            }


        });





        btnNextTicket.setOnClickListener(v -> {


            int current =
                    viewPager.getCurrentItem();



            if(current < tickets.size()-1){


                viewPager.setCurrentItem(
                        current + 1
                );


            }


        });



    }




}