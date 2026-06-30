package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.routelk.app.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextInputEditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        loginButton = findViewById(R.id.loginButton);
        TextView registerText = findViewById(R.id.registerText);
        ImageView btnBack = findViewById(R.id.btnBack);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Login Button
        loginButton.setOnClickListener(v -> performLogin());
        loginButton.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Email is required");
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Password is required");
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                            ).show();

                            // Admin Login
                            if (email.equals("admin@routelk.com")) {

                                Intent intent = new Intent(
                                        LoginActivity.this,
                                        AdminDashboardActivity.class
                                );

                                startActivity(intent);
                                finish();

                            } else {

                                // Normal User Login
                                Intent intent = new Intent(
                                        LoginActivity.this,
                                        Home.class
                                );

                                startActivity(intent);
                                finish();
                            }

                        } else {

                            Toast.makeText(
                                    LoginActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
            performLogin();
        });

        registerText.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );
    private void performLogin() {
        if (emailEditText.getText() == null || passwordEditText.getText() == null) return;

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        loginButton.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        goToHomeScreen();
                    } else {
                        loginButton.setEnabled(true);
                        String error = task.getException() != null ? task.getException().getMessage() : "Authentication failed";
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(LoginActivity.this, Home.class);
        startActivity(intent);
        finish();
    }

            startActivity(intent);
        });
    }
}