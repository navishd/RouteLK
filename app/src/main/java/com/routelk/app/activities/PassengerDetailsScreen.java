package com.routelk.app.activities;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.routelk.app.R;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.models.User;
import java.util.ArrayList;

public class PassengerDetailsScreen extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_passenger_details_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // Get data from intent
        Intent incomingIntent = getIntent();
        boolean isForOthers = incomingIntent.getBooleanExtra("IS_FOR_OTHERS", false);
        ArrayList<String> selectedSeats = incomingIntent.getStringArrayListExtra("SELECTED_SEATS");
        String busId = incomingIntent.getStringExtra("BUS_ID");
        String busName = incomingIntent.getStringExtra("BUS_NAME");
        String from = incomingIntent.getStringExtra("FROM");
        String to = incomingIntent.getStringExtra("TO");
        String date = incomingIntent.getStringExtra("DATE");
        String time = incomingIntent.getStringExtra("TIME");
        double price = incomingIntent.getDoubleExtra("PRICE", 0.0);
        
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText fullNameEditText = findViewById(R.id.fullNameEditText);
        EditText phoneEditText = findViewById(R.id.phoneEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        TextView passengerLabel = findViewById(R.id.passengerLabel);

        if (isForOthers) {
            passengerLabel.setText("Passenger Details");
            fullNameEditText.setText(""); // Clear default value
            phoneEditText.setText(""); // Clear default value
            emailEditText.setText("");
        } else {
            // Fetch current user details
            if (auth.getCurrentUser() != null) {
                db.collection("users").document(auth.getCurrentUser().getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                fullNameEditText.setText(user.getFullName());
                                phoneEditText.setText(user.getPhone());
                                emailEditText.setText(user.getEmail());
                            }
                        });
            }
        }

        // Back button click listener
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // Next button click listener
        findViewById(R.id.nextButton).setOnClickListener(v -> {
            String pName = fullNameEditText.getText().toString().trim();
            String pPhone = phoneEditText.getText().toString().trim();
            String pEmail = emailEditText.getText().toString().trim();

            if (pName.isEmpty() || pPhone.isEmpty() || pEmail.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simple email validation
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(pEmail).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(PassengerDetailsScreen.this, Payment.class);
            intent.putExtra("IS_FOR_OTHERS", isForOthers);
            intent.putStringArrayListExtra("SELECTED_SEATS", selectedSeats);
            intent.putExtra("BUS_ID", busId);
            intent.putExtra("BUS_NAME", busName);
            intent.putExtra("FROM", from);
            intent.putExtra("TO", to);
            intent.putExtra("DATE", date);
            intent.putExtra("TIME", time);
            intent.putExtra("PRICE", price);
            intent.putExtra("PASSENGER_NAME", pName);
            intent.putExtra("PASSENGER_PHONE", pPhone);
            intent.putExtra("PASSENGER_EMAIL", pEmail);
            startActivity(intent);
        });
    }
}