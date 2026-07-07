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
    private String busId, busName, from, to, date;
    
    public static final Set<String> reservedSeats = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        db = FirebaseFirestore.getInstance();
        continueButton = findViewById(R.id.continueButton);

        Intent intent = getIntent();
        busId = intent.getStringExtra("BUS_ID");
        busName = intent.getStringExtra("BUS_NAME");
        from = intent.getStringExtra("FROM");
        to = intent.getStringExtra("TO");
        date = intent.getStringExtra("DATE");

        ViewGroup root = findViewById(R.id.seatSelectionRoot);
        if (root != null) {
            seatButtons.clear();
            collectAllSeats(root);
        }

        fetchReservedSeats();

        continueButton.setOnClickListener(v -> {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "Please select at least one seat", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isForOthers = getIntent().getBooleanExtra("IS_FOR_OTHERS", false);
            Intent nextIntent;

            if (isForOthers) {
                nextIntent = new Intent(SeatSelectionActivity.this, PassengerDetailsScreen.class);
            } else {
                nextIntent = new Intent(SeatSelectionActivity.this, Payment.class);
            }

            nextIntent.putExtra("IS_FOR_OTHERS", isForOthers);
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
        fetchReservedSeats();
    }

    private void fetchReservedSeats() {
        if (busName == null) {
            refreshSeats();
            return;
        }
        
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
            if (child instanceof Button) {
                Button btn = (Button) child;
                if (btn.getId() != R.id.continueButton) {
                    String text = btn.getText().toString();
                    // Identify seats by their numeric labels (1-50)
                    if (text.matches("\\d+")) {
                        seatButtons.add(btn);
                    }
                }
            } else if (child instanceof ViewGroup) {
                collectAllSeats((ViewGroup) child);
            }
        }
    }

    private void refreshSeats() {
        // Clear selection if any previously selected seats are now reserved by someone else
        selectedSeats.removeIf(reservedSeats::contains);

        for (Button seat : seatButtons) {
            final String seatNum = seat.getText().toString();

            if (reservedSeats.contains(seatNum)) {
                // Booked seats: Gray and disabled
                seat.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9CA3AF")));
                seat.setTextColor(Color.WHITE);
                seat.setEnabled(false);
                seat.setOnClickListener(v -> Toast.makeText(this, "Seat " + seatNum + " is already booked", Toast.LENGTH_SHORT).show());
            } else if (selectedSeats.contains(seatNum)) {
                // Currently selected by user: Green and enabled
                seat.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                seat.setTextColor(Color.WHITE);
                seat.setEnabled(true);
                seat.setOnClickListener(v -> toggleSeatSelection(seat, seatNum));
            } else {
                // Available seats: White and enabled
                seat.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                seat.setTextColor(Color.BLACK);
                seat.setEnabled(true);
                seat.setOnClickListener(v -> toggleSeatSelection(seat, seatNum));
            }
        }
        updateContinueButton();
    }

    private void toggleSeatSelection(Button seat, String seatNum) {
        if (selectedSeats.contains(seatNum)) {
            selectedSeats.remove(seatNum);
            // Revert to Available state
            seat.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            seat.setTextColor(Color.BLACK);
        } else {
            selectedSeats.add(seatNum);
            // Change to Selected state
            seat.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            seat.setTextColor(Color.WHITE);
        }
        updateContinueButton();
    }

    private void updateContinueButton() {
        if (!selectedSeats.isEmpty()) {
            continueButton.setText("Continue (" + selectedSeats.size() + " Selected)");
            continueButton.setEnabled(true);
        } else {
            continueButton.setText("Continue");
            // Optionally disable continue if no seats are selected
            // continueButton.setEnabled(false);
        }
    }
}