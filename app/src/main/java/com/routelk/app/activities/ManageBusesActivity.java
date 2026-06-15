package com.routelk.app.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.routelk.app.R;

public class ManageBusesActivity extends AppCompatActivity {

    Button addBusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_buses);

        addBusBtn = findViewById(R.id.addBusBtn);

        addBusBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Bus added successfully", Toast.LENGTH_SHORT).show();
        });
    }
}