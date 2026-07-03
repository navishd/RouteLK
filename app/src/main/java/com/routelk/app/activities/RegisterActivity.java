package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmail, etPhone,
            etPassword, etConfirmPassword;

    private Button btnRegister;
    private TextView tvLoginLink;
    private ImageView btnBack;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);
        btnBack = findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        btnRegister.setOnClickListener(v -> performRegistration());

        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void performRegistration() {

        String fullName = String.valueOf(etFullName.getText()).trim();
        String email = String.valueOf(etEmail.getText()).trim();
        String phone = String.valueOf(etPhone.getText()).trim();
        String password = String.valueOf(etPassword.getText()).trim();
        String confirmPassword = String.valueOf(etConfirmPassword.getText()).trim();

        // Validation
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full Name is required");
            etFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone Number is required");
            etPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        btnRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        if (firebaseUser == null) {
                            btnRegister.setEnabled(true);
                            Toast.makeText(
                                    RegisterActivity.this,
                                    "User creation failed",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        String userId = firebaseUser.getUid();

                        Map<String, Object> user = new HashMap<>();
                        user.put("fullName", fullName);
                        user.put("email", email);
                        user.put("phone", phone);
                        user.put("role", "user");
                        user.put("createdAt", Timestamp.now());

                        db.collection("users")
                                .document(userId)
                                .set(user)
                                .addOnSuccessListener(unused -> {

                                    Toast.makeText(
                                            RegisterActivity.this,
                                            "Registration Successful",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    Intent intent = new Intent(
                                            RegisterActivity.this,
                                            Home.class
                                    );

                                    intent.setFlags(
                                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    );

                                    startActivity(intent);
                                    finish();

                                })
                                .addOnFailureListener(e -> {

                                    btnRegister.setEnabled(true);
                                    Toast.makeText(
                                            RegisterActivity.this,
                                            "Database Error : " + e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();

                                });

                    } else {

                        btnRegister.setEnabled(true);

                        Exception e = task.getException();

                        Toast.makeText(
                                RegisterActivity.this,
                                e != null ? e.getMessage() : "Registration Failed",
                                Toast.LENGTH_LONG
                        ).show();
                    }

                });
    }
}