package com.routelk.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView tvUserName, tvUserEmail, tvUserInfo, tvTotalTrips, tvTotalTickets, tvThemeStatus;
    private MaterialCardView menuLogout, btnChangePhoto;
    private LinearLayout menuMyBookings, btnEditProfileCard, menuNotifications, menuLanguage, menuSecurity, menuTheme, menuContact, menuPrivacy;
    private ImageView ivProfileImage;
    private androidx.core.widget.NestedScrollView nestedScrollView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    ivProfileImage.setImageURI(uri);
                    ivProfileImage.setPadding(0, 0, 0, 0);
                    ivProfileImage.setAlpha(1.0f);
                    ivProfileImage.setColorFilter(null);
                    saveProfileImage(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();
        setupNavigation();
        loadUserData();
        updateThemeStatusText();
        setupClickListeners();
    }

    private void updateThemeStatusText() {
        SharedPreferences prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("isDarkMode", false);
        tvThemeStatus.setText(isDarkMode ? getString(R.string.theme_dark) : getString(R.string.theme_light));
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserInfo = findViewById(R.id.tvUserInfo);
        tvTotalTrips = findViewById(R.id.tvTotalTrips);
        tvTotalTickets = findViewById(R.id.tvTotalTickets);
        tvThemeStatus = findViewById(R.id.tvThemeStatus);
        
        menuMyBookings = findViewById(R.id.menuMyBookings);
        menuLogout = findViewById(R.id.menuLogout);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        btnEditProfileCard = findViewById(R.id.btnEditProfileCard);
        
        menuNotifications = findViewById(R.id.menuNotifications);
        menuLanguage = findViewById(R.id.menuLanguage);
        menuSecurity = findViewById(R.id.menuSecurity);
        menuTheme = findViewById(R.id.menuTheme);
        menuContact = findViewById(R.id.menuContact);
        menuPrivacy = findViewById(R.id.menuPrivacy);
        
        ivProfileImage = findViewById(R.id.ivProfileImage);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void setupNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.nav_account);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_activities || id == R.id.nav_tickets) {
                startActivity(new Intent(this, MyActivitiesActivity.class));
                return true;
            } else if (id == R.id.nav_account) {
                if (nestedScrollView != null) {
                    nestedScrollView.smoothScrollTo(0, 0);
                }
                return true;
            }
            return false;
        });

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupClickListeners() {
        menuMyBookings.setOnClickListener(v -> startActivity(new Intent(this, MyBookingsActivity.class)));
        
        btnEditProfileCard.setOnClickListener(v -> startActivity(new Intent(this, EditProfileActivity.class)));

        menuLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        View.OnClickListener comingSoon = v -> Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        
        menuNotifications.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));
        menuLanguage.setOnClickListener(comingSoon);
        
        menuSecurity.setOnClickListener(v -> startActivity(new Intent(this, ChangePasswordActivity.class)));
        
        menuTheme.setOnClickListener(v -> toggleTheme());
        
        menuContact.setOnClickListener(comingSoon);
        menuPrivacy.setOnClickListener(comingSoon);

        btnChangePhoto.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));
    }

    private void toggleTheme() {
        SharedPreferences prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("isDarkMode", false);
        boolean newDarkMode = !isDarkMode;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isDarkMode", newDarkMode);
        editor.apply();

        if (newDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        
        recreate(); // Refresh activity to apply theme
    }

    private void loadUserData() {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();
            String email = mAuth.getCurrentUser().getEmail();
            
            tvUserEmail.setText(email);
            updateUserInfo(email, null);
            
            // Initial fallback name from email
            if (email != null && email.contains("@")) {
                String emailName = email.split("@")[0];
                emailName = emailName.substring(0, 1).toUpperCase() + emailName.substring(1);
                tvUserName.setText(emailName);
            }

            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String fullName = documentSnapshot.getString("fullName");
                            String name = documentSnapshot.getString("name");
                            
                            if (fullName != null && !fullName.isEmpty()) {
                                tvUserName.setText(fullName);
                            } else if (name != null && !name.isEmpty()) {
                                tvUserName.setText(name);
                            }
                            
                            String phone = documentSnapshot.getString("phone");
                            updateUserInfo(email, phone);
                            
                            String profileUrl = documentSnapshot.getString("profileImage");
                            if (profileUrl != null && !profileUrl.isEmpty()) {
                                // Logic for loading profile image from URL
                            }
                        }
                    });

            fetchBookingStats(userId);
        }
    }

    private void fetchBookingStats(String userId) {
        db.collection("bookings")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int totalSeats = queryDocumentSnapshots.size();
                    tvTotalTickets.setText(String.valueOf(totalSeats));

                    // For "Trips", let's count unique timestamps or unique bus/date combinations
                    java.util.HashSet<String> uniqueTrips = new java.util.HashSet<>();
                    for (com.google.firebase.firestore.DocumentSnapshot doc : queryDocumentSnapshots) {
                        String bus = doc.getString("busName");
                        String date = doc.getString("date");
                        // We use a combination of bus name and date as a "trip"
                        if (bus != null && date != null) {
                            uniqueTrips.add(bus + "_" + date);
                        }
                    }
                    tvTotalTrips.setText(String.valueOf(uniqueTrips.size()));
                });
    }

    private void updateUserInfo(String email, String phone) {
        String info = (email != null ? email : "no-email@domain.com");
        if (phone != null && !phone.isEmpty()) {
            info += " | " + phone;
        } else {
            info += " | +94 77 123 4567"; // Placeholder phone if none in DB
        }
        tvUserInfo.setText(info);
    }

    private void saveProfileImage(Uri uri) {
        Toast.makeText(this, "Profile photo updated locally", Toast.LENGTH_SHORT).show();
    }
}
