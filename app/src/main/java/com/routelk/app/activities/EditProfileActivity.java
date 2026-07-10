package com.routelk.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.routelk.app.R;
import com.routelk.app.models.User;
import com.routelk.app.services.UserService;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etPhone, etEmail;
    private MaterialButton btnSaveChanges;
    private View btnChangePassword;
    private FirebaseAuth mAuth;
    private UserService userService;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        userService = new UserService();
        
        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        } else {
            finish();
            return;
        }

        initViews();
        loadCurrentData();
        setupClickListeners();
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadCurrentData() {
        if (mAuth.getCurrentUser() != null) {
            etEmail.setText(mAuth.getCurrentUser().getEmail());
        }
        
        userService.getUser(userId, task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                User user = task.getResult().toObject(User.class);
                if (user != null) {
                    if (user.getFullName() != null) etFullName.setText(user.getFullName());
                    if (user.getPhone() != null) etPhone.setText(user.getPhone());
                }
            }
        });
    }

    private void setupClickListeners() {
        btnSaveChanges.setOnClickListener(v -> saveChanges());
        btnChangePassword.setOnClickListener(v -> startActivity(new Intent(this, ChangePasswordActivity.class)));
    }

    private void saveChanges() {
        if (etFullName.getText() == null || etPhone.getText() == null) return;

        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            return;
        }

        btnSaveChanges.setEnabled(false);
        btnSaveChanges.setText(R.string.saving);

        User updatedUser = new User(userId, fullName, email, phone);

        userService.updateUser(updatedUser, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Exception e = task.getException();
                Toast.makeText(this, "Update failed: " + (e != null ? e.getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                btnSaveChanges.setEnabled(true);
                btnSaveChanges.setText(R.string.save_changes);
            }
        });
    }
}
