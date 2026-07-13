package com.routelk.app.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.routelk.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class Home extends AppCompatActivity {


    private AutoCompleteTextView etFrom, etTo;

    private TextView tvDate;
    private TextView tvTime;
    private TextView tvPassengers;


    private MaterialCardView btnSwap;
    private MaterialCardView btnNotification;
    private MaterialCardView fabVoiceAssistant;


    private LinearLayout layoutDate;
    private LinearLayout layoutTime;
    private LinearLayout layoutPassengers;


    private MaterialButton btnSearch;


    private Calendar calendar;



    private com.google.android.material.switchmaterial.SwitchMaterial switchBookingForOthers;



    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        EdgeToEdge.enable(this);


        setContentView(R.layout.activity_home);



        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v,insets)->{


                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );


                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );


                    return insets;

                });



        etFrom = findViewById(R.id.etFrom);

        etTo = findViewById(R.id.etTo);



        tvDate = findViewById(R.id.tvDate);

        tvTime = findViewById(R.id.tvTime);

        tvPassengers = findViewById(R.id.tvPassengers);



        btnSwap = findViewById(R.id.btnSwap);

        btnNotification = findViewById(R.id.btnNotification);

        fabVoiceAssistant = findViewById(R.id.fabVoiceAssistant);
        fabVoiceAssistant.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, ChatAssistantActivity.class));
        });

        layoutDate = findViewById(R.id.layoutDate);

        layoutTime = findViewById(R.id.layoutTime);

        layoutPassengers =
                findViewById(R.id.layoutPassengers);



        btnSearch =
                findViewById(R.id.btnSearchBuses);



        switchBookingForOthers =
                findViewById(R.id.switchBookingForOthers);

        loadRoutes();

        bottomNavigationView =
                findViewById(R.id.bottomNavigationView);

        setupBottomNavigation();

        calendar = Calendar.getInstance();



        // Default date

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );


        tvDate.setText(
                sdf.format(calendar.getTime())
        );



        tvTime.setText("Select Time");

        tvPassengers.setText("1 Adult");





        // Swap button

        btnSwap.setOnClickListener(v->{


            String from =
                    etFrom.getText().toString();


            String to =
                    etTo.getText().toString();



            etFrom.setText(to);

            etTo.setText(from);



        });

        // Date Selection
        layoutDate.setOnClickListener(v -> {

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            this,
                            (view, year, month, dayOfMonth) -> {


                                calendar.set(
                                        Calendar.YEAR,
                                        year
                                );

                                calendar.set(
                                        Calendar.MONTH,
                                        month
                                );

                                calendar.set(
                                        Calendar.DAY_OF_MONTH,
                                        dayOfMonth
                                );


                                SimpleDateFormat dateFormat =
                                        new SimpleDateFormat(
                                                "yyyy-MM-dd",
                                                Locale.getDefault()
                                        );


                                tvDate.setText(
                                        dateFormat.format(
                                                calendar.getTime()
                                        )
                                );


                            },

                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    );


            datePickerDialog.show();

        });

        // Time Picker


        layoutTime.setOnClickListener(v->{



            TimePickerDialog dialog =
                    new TimePickerDialog(
                            this,
                            (view,hour,minute)->{


                                String time =
                                        String.format(
                                                Locale.getDefault(),
                                                "%02d:%02d",
                                                hour,
                                                minute
                                        );


                                tvTime.setText(time);


                            },


                            calendar.get(Calendar.HOUR_OF_DAY),

                            calendar.get(Calendar.MINUTE),

                            true

                    );



            dialog.show();



        });









        // Passenger


        layoutPassengers.setOnClickListener(v->{



            String[] list={

                    "1 Adult",
                    "2 Adults",
                    "3 Adults",
                    "4 Adults",
                    "5 Adults"

            };



            new AlertDialog.Builder(this)

                    .setTitle("Passengers")

                    .setItems(
                            list,
                            (dialog,which)->{


                                tvPassengers.setText(
                                        list[which]
                                );


                            })

                    .show();


        });









        // Search


        btnSearch.setOnClickListener(v->{



            String from =
                    etFrom.getText()
                            .toString()
                            .trim();



            String to =
                    etTo.getText()
                            .toString()
                            .trim();





            if(from.isEmpty()
                    ||
                    to.isEmpty()){


                Toast.makeText(
                        this,
                        "Enter From and To",
                        Toast.LENGTH_SHORT
                ).show();


                return;

            }







            Intent intent =
                    new Intent(
                            Home.this,
                            BusListScreen.class
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
                    tvDate.getText()
                            .toString()
            );


            intent.putExtra(
                    "TIME",
                    tvTime.getText()
                            .toString()
            );


            intent.putExtra(
                    "PASSENGERS",
                    tvPassengers.getText()
                            .toString()
            );



            intent.putExtra(
                    "IS_FOR_OTHERS",
                    switchBookingForOthers.isChecked()
            );



            startActivity(intent);



        });




    }
    private void loadRoutes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("routes").get().addOnSuccessListener(queryDocumentSnapshots -> {
            Set<String> fromList = new HashSet<>();
            Set<String> toList = new HashSet<>();

            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                String from = document.getString("from");
                String to = document.getString("to");
                if (from != null) fromList.add(from);
                if (to != null) toList.add(to);
            }

            setupAdapter(etFrom, new ArrayList<>(fromList));
            setupAdapter(etTo, new ArrayList<>(toList));
        });
    }

    private void setupAdapter(AutoCompleteTextView view, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list);
        view.setAdapter(adapter);
        view.setOnClickListener(v -> view.showDropDown());
    }

    private void setupBottomNavigation() {


        bottomNavigationView.setSelectedItemId(R.id.nav_home);


        bottomNavigationView.setOnItemSelectedListener(item -> {


            int id = item.getItemId();


            if(id == R.id.nav_home){

                return true;

            }


            else if(id == R.id.nav_activities){


                startActivity(
                        new Intent(
                                Home.this,
                                MyActivitiesActivity.class
                        )
                );

                return true;

            }


            else if(id == R.id.nav_tickets){


                startActivity(
                        new Intent(
                                Home.this,
                                TicketsActivity.class
                        )
                );

                return true;

            }


            else if(id == R.id.nav_account){


                startActivity(
                        new Intent(
                                Home.this,
                                ProfileActivity.class
                        )
                );

                return true;

            }


            return false;

        });


    }

}