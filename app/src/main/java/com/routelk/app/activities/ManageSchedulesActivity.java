package com.routelk.app.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.routelk.app.R;
import com.routelk.app.adapters.ScheduleAdapter;
import com.routelk.app.models.Schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManageSchedulesActivity extends AppCompatActivity {

    private EditText etScheduleID;
    private EditText etBusID;
    private EditText etRouteID;
    private EditText etDeparture;
    private EditText etArrival;
    private EditText etPrice;
    private EditText etOperatingDays;

    private Button btnAddSchedule;

    private RecyclerView scheduleRecyclerView;

    private ScheduleAdapter adapter;

    private ArrayList<Schedule> scheduleList;

    private FirebaseFirestore db;

    private boolean isEdit = false;

    private String currentScheduleID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedules);

        db = FirebaseFirestore.getInstance();

        etScheduleID = findViewById(R.id.etScheduleID);
        etBusID = findViewById(R.id.etBusID);
        etRouteID = findViewById(R.id.etRouteID);
        etDeparture = findViewById(R.id.etDeparture);
        etArrival = findViewById(R.id.etArrival);
        etPrice = findViewById(R.id.etPrice);
        etOperatingDays = findViewById(R.id.etOperatingDays);

        btnAddSchedule = findViewById(R.id.btnAddSchedule);

        scheduleRecyclerView = findViewById(R.id.scheduleRecyclerView);

        scheduleRecyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        scheduleList = new ArrayList<>();

        adapter = new ScheduleAdapter(this, scheduleList, schedule -> {

            isEdit = true;

            currentScheduleID = schedule.getScheduleID();

            etScheduleID.setText(schedule.getScheduleID());

            etBusID.setText(schedule.getBusID());

            etRouteID.setText(schedule.getRouteID());

            etDeparture.setText(schedule.getDepartureTime());

            etArrival.setText(schedule.getArrivalTime());

            etPrice.setText(String.valueOf(schedule.getPrice()));

            etOperatingDays.setText(
                    TextUtils.join(",", schedule.getOperatingDays())
            );

            btnAddSchedule.setText("Update Schedule");

        });

        scheduleRecyclerView.setAdapter(adapter);

        loadSchedules();

        btnAddSchedule.setOnClickListener(v -> {

            if (isEdit) {

                updateSchedule();

            } else {

                addSchedule();

            }

        });

    }
    private void addSchedule() {

        String scheduleID = etScheduleID.getText().toString().trim();
        String busID = etBusID.getText().toString().trim();
        String routeID = etRouteID.getText().toString().trim();
        String departure = etDeparture.getText().toString().trim();
        String arrival = etArrival.getText().toString().trim();
        String priceText = etPrice.getText().toString().trim();
        String daysText = etOperatingDays.getText().toString().trim();

        if (scheduleID.isEmpty() ||
                busID.isEmpty() ||
                routeID.isEmpty() ||
                departure.isEmpty() ||
                arrival.isEmpty() ||
                priceText.isEmpty() ||
                daysText.isEmpty()) {

            Toast.makeText(this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceText);

        List<String> operatingDays =
                Arrays.asList(daysText.split(","));

        Schedule schedule = new Schedule(
                scheduleID,
                busID,
                routeID,
                departure,
                arrival,
                price,
                operatingDays
        );

        db.collection("schedules")
                .document(scheduleID)
                .set(schedule)
                .addOnSuccessListener(unused -> {

                    Toast.makeText(this,
                            "Schedule Added",
                            Toast.LENGTH_SHORT).show();

                    clearFields();

                    loadSchedules();

                })
                .addOnFailureListener(e ->

                        Toast.makeText(this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show());

    }

    private void loadSchedules() {

        scheduleList.clear();

        db.collection("schedules")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot document
                            : queryDocumentSnapshots) {

                        Schedule schedule =
                                document.toObject(Schedule.class);

                        scheduleList.add(schedule);

                    }

                    adapter.notifyDataSetChanged();

                });

    }

    private void updateSchedule() {

        String scheduleID = etScheduleID.getText().toString().trim();
        String busID = etBusID.getText().toString().trim();
        String routeID = etRouteID.getText().toString().trim();
        String departure = etDeparture.getText().toString().trim();
        String arrival = etArrival.getText().toString().trim();
        String priceText = etPrice.getText().toString().trim();
        String daysText = etOperatingDays.getText().toString().trim();

        if (scheduleID.isEmpty() ||
                busID.isEmpty() ||
                routeID.isEmpty() ||
                departure.isEmpty() ||
                arrival.isEmpty() ||
                priceText.isEmpty() ||
                daysText.isEmpty()) {

            Toast.makeText(this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceText);

        List<String> operatingDays =
                Arrays.asList(daysText.split(","));

        Schedule schedule = new Schedule(
                scheduleID,
                busID,
                routeID,
                departure,
                arrival,
                price,
                operatingDays
        );

        db.collection("schedules")
                .document(currentScheduleID)
                .set(schedule)
                .addOnSuccessListener(unused -> {

                    Toast.makeText(this,
                            "Schedule Updated",
                            Toast.LENGTH_SHORT).show();

                    isEdit = false;

                    currentScheduleID = "";

                    btnAddSchedule.setText("Add Schedule");

                    clearFields();

                    loadSchedules();

                })
                .addOnFailureListener(e ->

                        Toast.makeText(this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show());

    }

    private void clearFields() {

        etScheduleID.setText("");

        etBusID.setText("");

        etRouteID.setText("");

        etDeparture.setText("");

        etArrival.setText("");

        etPrice.setText("");

        etOperatingDays.setText("");

    }
}

