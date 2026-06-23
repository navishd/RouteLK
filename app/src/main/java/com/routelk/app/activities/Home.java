package com.routelk.app.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.routelk.app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private EditText etFrom, etTo;
    private TextView tvDate, tvTime;
    private MaterialCardView btnSwap, btnMenu, btnNotification;
    private LinearLayout layoutDate, layoutTime;
    private MaterialButton btnSearch;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI components
        etFrom = findViewById(R.id.etFrom);
        etTo = findViewById(R.id.etTo);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        btnSwap = findViewById(R.id.btnSwap);
        btnMenu = findViewById(R.id.btnMenu);
        btnNotification = findViewById(R.id.btnNotification);
        layoutDate = findViewById(R.id.layoutDate);
        layoutTime = findViewById(R.id.layoutTime);
        btnSearch = findViewById(R.id.btnSearchBuses);
        
        calendar = Calendar.getInstance();

        // Menu Click
        btnMenu.setOnClickListener(v -> Toast.makeText(this, "Opening Menu...", Toast.LENGTH_SHORT).show());

        // Notification Click
        btnNotification.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, NotificationsActivity.class);
            startActivity(intent);
        });

        // Swap From <-> To
        btnSwap.setOnClickListener(v -> {
            String from = etFrom.getText().toString();
            String to = etTo.getText().toString();
            etFrom.setText(to);
            etTo.setText(from);
            Toast.makeText(this, "Locations swapped", Toast.LENGTH_SHORT).show();
        });

        // Date Selection
        layoutDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                        tvDate.setText(sdf.format(calendar.getTime()));
                    }, 
                    calendar.get(Calendar.YEAR), 
                    calendar.get(Calendar.MONTH), 
                    calendar.get(Calendar.DAY_OF_MONTH));
            
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        // Time Selection
        layoutTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        tvTime.setText(sdf.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false);
            timePickerDialog.show();
        });

        // Search Button
        btnSearch.setOnClickListener(v -> {

            String from = etFrom.getText().toString().trim();
            String to = etTo.getText().toString().trim();

            if (from.isEmpty() || to.isEmpty()) {

                Toast.makeText(
                        Home.this,
                        "Please enter both origin and destination",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Intent intent = new Intent(Home.this, SearchBusActivity.class);

                // Send data to SearchBusActivity
                intent.putExtra("FROM", from);
                intent.putExtra("TO", to);
                intent.putExtra("DATE", tvDate.getText().toString());
                intent.putExtra("TIME", tvTime.getText().toString());

                startActivity(intent);
            }
        });
    }
}
