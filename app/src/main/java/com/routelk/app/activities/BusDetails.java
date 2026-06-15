package com.routelk.app.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.routelk.app.R;

import java.util.Calendar;
import java.util.Locale;

public class BusDetails extends AppCompatActivity {

    private TextView tvSelectedDate;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        ImageView btnBack = findViewById(R.id.btnBack);
        MaterialButton btnSelectSeats = findViewById(R.id.btnSelectSeats);
        LinearLayout btnChooseDate = findViewById(R.id.btnChooseDate);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        
        calendar = Calendar.getInstance();

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnChooseDate != null) {
            btnChooseDate.setOnClickListener(v -> showDatePicker());
        }

        if (btnSelectSeats != null) {
            btnSelectSeats.setOnClickListener(v -> {
                if (tvSelectedDate.getText().toString().equals("Choose Travel Date")) {
                    Toast.makeText(this, "Please select a travel date first", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Opening Seat Selection for " + tvSelectedDate.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
                    tvSelectedDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        // Prevent selecting past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
}
