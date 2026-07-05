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

import com.routelk.app.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatSelectionActivity extends AppCompatActivity {

    private Button continueButton;

    private Set<String> selectedSeats = new HashSet<>();
    private List<Button> seatButtons = new ArrayList<>();

    public static final Set<String> reservedSeats = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        continueButton = findViewById(R.id.continueButton);

        ViewGroup root = findViewById(R.id.seatSelectionRoot);

        if (root != null) {
            collectAllSeats(root);
        }

        refreshSeats();

        continueButton.setOnClickListener(v -> {

            if (selectedSeats.isEmpty()) {
                Toast.makeText(
                        this,
                        "Please select at least one seat",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            boolean isForOthers =
                    getIntent().getBooleanExtra(
                            "IS_FOR_OTHERS",
                            false
                    );

            Intent intent;

            if (isForOthers) {

                intent = new Intent(
                        SeatSelectionActivity.this,
                        PassengerDetailsScreen.class
                );

                intent.putExtra(
                        "IS_FOR_OTHERS",
                        true
                );

            } else {

                intent = new Intent(
                        SeatSelectionActivity.this,
                        Payment.class
                );
            }

            intent.putStringArrayListExtra(
                    "SELECTED_SEATS",
                    new ArrayList<>(selectedSeats)
            );

            startActivity(intent);

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSeats();
    }

    private void collectAllSeats(ViewGroup viewGroup) {

        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            if (child instanceof Button &&
                    child.getId() != R.id.continueButton) {

                seatButtons.add((Button) child);

            } else if (child instanceof ViewGroup) {

                collectAllSeats((ViewGroup) child);
            }
        }
    }

    private void refreshSeats() {

        for (Button seat : seatButtons) {

            String seatNum =
                    seat.getText().toString();

            if (reservedSeats.contains(seatNum)) {

                seat.setEnabled(false);

            } else {

                seat.setEnabled(true);

                seat.setOnClickListener(v ->
                        toggleSeatSelection(
                                seat,
                                seatNum
                        ));
            }
        }
    }

    private void toggleSeatSelection(
            Button seat,
            String seatNum
    ) {

        if (selectedSeats.contains(seatNum)) {

            selectedSeats.remove(seatNum);

            seat.setBackgroundTintList(
                    ColorStateList.valueOf(
                            Color.GRAY
                    )
            );

        } else {

            selectedSeats.add(seatNum);

            seat.setBackgroundTintList(
                    ColorStateList.valueOf(
                            Color.GREEN
                    )
            );
        }
    }
}