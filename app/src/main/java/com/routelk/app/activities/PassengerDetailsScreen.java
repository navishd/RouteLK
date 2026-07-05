package com.routelk.app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.routelk.app.R;

public class PassengerDetailsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            
            Intent intent = new Intent(PassengerDetailsScreen.this, Payment.class);
            startActivity(intent);
        });
    }
}