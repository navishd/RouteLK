package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.routelk.app.R;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView registerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Views
        loginButton = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.registerText);

        // Login Button
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Home.class);
            startActivity(intent);
            finish();
        });

        // Register Text
        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(LoginActivity.this, Home.class);
        startActivity(intent);
        finish();
    }

    private void goToRegisterScreen() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}