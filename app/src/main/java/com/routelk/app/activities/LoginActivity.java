package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.routelk.app.R;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView registerText;
    private ImageView btnBack;

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.registerText);
        btnBack = findViewById(R.id.btnBack);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        loginButton.setOnClickListener(v -> {

            String email =
                    etEmail.getText().toString().trim();

            String password =
                    etPassword.getText().toString().trim();

            if (email.isEmpty()) {

                etEmail.setError("Email is required");
                return;
            }

            if (password.isEmpty()) {

                etPassword.setError("Password is required");
                return;
            }

            mAuth.signInWithEmailAndPassword(
                            email,
                            password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                            ).show();

                            Intent intent =
                                    new Intent(
                                            LoginActivity.this,
                                            Home.class);

                            startActivity(intent);
                            finish();

                        } else {

                            Toast.makeText(
                                    LoginActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });

        registerText.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            LoginActivity.this,
                            RegisterActivity.class);

            startActivity(intent);
        });
    }
}