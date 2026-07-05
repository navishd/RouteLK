package com.routelk.app.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.routelk.app.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatSelectionActivity extends AppCompatActivity {

    private Button continueButton;
    private Set<String> selectedSeats = new HashSet<>();
    private List<Button> seatButtons = new ArrayList<>();
    private FirebaseFirestore db;
    private String busName, date;
    
    // Static set to keep track of reserved seats across the app session
    public static final Set<String> reservedSeats = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        continueButton = findViewById(R.id.continueButton);
        db = FirebaseFirestore.getInstance();

        // Find all seat buttons in the layout
        ViewGroup root = findViewById(R.id.seatSelectionRoot);
        if (root != null) {
            collectAllSeats(root);
        }

        Intent intent = getIntent();
        String busId = intent.getStringExtra("BUS_ID");
        busName = intent.getStringExtra("BUS_NAME");
        String from = intent.getStringExtra("FROM");
        String to = intent.getStringExtra("TO");
        date = intent.getStringExtra("DATE");

        fetchReservedSeats();

        continueButton.setOnClickListener(v -> {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "Please select at least one seat", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent nextIntent = new Intent(SeatSelectionActivity.this, Payment.class);
            nextIntent.putStringArrayListExtra("SELECTED_SEATS", new ArrayList<>(selectedSeats));
            nextIntent.putExtra("BUS_ID", busId);
            nextIntent.putExtra("BUS_NAME", busName);
            nextIntent.putExtra("FROM", from);
            nextIntent.putExtra("TO", to);
            nextIntent.putExtra("DATE", date);
            startActivity(nextIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh seat states whenever we return to this screen
        refreshSeats();
    }

    private void fetchReservedSeats() {
        if (busName == null) return;
        
        db.collection("bookings")
                .whereEqualTo("busName", busName)
                .whereEqualTo("date", date)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reservedSeats.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String seatNo = document.getString("seatNo");
                        if (seatNo != null) {
                            reservedSeats.add(seatNo);
                        }
                    }
                    refreshSeats();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load reserved seats", Toast.LENGTH_SHORT).show();
                    refreshSeats();
                });
    }

    private void collectAllSeats(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof Button && child.getId() != R.id.continueButton) {
                seatButtons.add((Button) child);
            } else if (child instanceof ViewGroup) {
                collectAllSeats((ViewGroup) child);
            }
        }
    }

    private void refreshSeats() {
        // First, check for newly reserved seats that might have been selected here previously
        for (String reserved : reservedSeats) {
            selectedSeats.remove(reserved);
        }

        for (Button seat : seatButtons) {
            String seatNum = seat.getText().toString();

            if (reservedSeats.contains(seatNum)) {
                setSeatReserved(seat);
            } else if (selectedSeats.contains(seatNum)) {
                setSeatSelected(seat);
                seat.setEnabled(true);
                seat.setOnClickListener(v -> toggleSeatSelection(seat, seatNum));
            } else {
                setSeatAvailable(seat);
                seat.setEnabled(true);
                seat.setOnClickListener(v -> toggleSeatSelection(seat, seatNum));
            }
        }
        
        updateContinueButton();
    }

    private void toggleSeatSelection(Button seat, String seatNum) {
        if (selectedSeats.contains(seatNum)) {
            selectedSeats.remove(seatNum);
            setSeatAvailable(seat);
        } else {
            selectedSeats.add(seatNum);
            setSeatSelected(seat);
        }
        updateContinueButton();
    }

    private void updateContinueButton() {
        if (!selectedSeats.isEmpty()) {
            continueButton.setText("Continue (" + selectedSeats.size() + " Seats Selected)");
        } else {
            continueButton.setText("Continue");
        }
    }

    private void setSeatAvailable(Button seat) {
        // White background for available seats
        seat.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        seat.setTextColor(Color.BLACK);
    }

    private void setSeatSelected(Button seat) {
        // Primary (Blue) color for selected seats
        seat.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary)));
        seat.setTextColor(Color.WHITE);
    }

    private void setSeatReserved(Button seat) {
        // Darker grey for reserved seats to make them clearly distinct
        seat.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9CA3AF"))); 
        seat.setTextColor(Color.WHITE);
        seat.setEnabled(false);
        seat.setOnClickListener(null);
    }
}