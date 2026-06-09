package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.routelk.app.R;

public class AdminDashboardActivity extends AppCompatActivity {

    Button manageBusesBtn, manageRoutesBtn, viewBookingsBtn, manageUsersBtn, reportsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        manageBusesBtn = findViewById(R.id.manageBusesBtn);
        manageRoutesBtn = findViewById(R.id.manageRoutesBtn);
        viewBookingsBtn = findViewById(R.id.viewBookingsBtn);
        manageUsersBtn = findViewById(R.id.manageUsersBtn);
        reportsBtn = findViewById(R.id.reportsBtn);

        manageBusesBtn.setOnClickListener(v ->
                startActivity(new Intent(this, ManageBusesActivity.class)));

        manageRoutesBtn.setOnClickListener(v ->
                startActivity(new Intent(this, ManageRoutesActivity.class)));

        viewBookingsBtn.setOnClickListener(v ->
                startActivity(new Intent(this, ViewBookingsActivity.class)));

        manageUsersBtn.setOnClickListener(v ->
                startActivity(new Intent(this, ManageUsersActivity.class)));

        reportsBtn.setOnClickListener(v ->
                startActivity(new Intent(this, ReportsActivity.class)));
    }
}