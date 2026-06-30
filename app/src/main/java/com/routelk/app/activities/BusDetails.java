package com.routelk.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.routelk.app.R;
import android.content.Intent;

import java.util.Objects;

public class BusDetails extends AppCompatActivity {

    private RadioGroup rgBookingFor;
    private LinearLayout layoutFriendDetails;
    private TextInputEditText etFriendName, etFriendPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        ImageView btnBack = findViewById(R.id.btnBack);
        MaterialButton btnSelectSeats = findViewById(R.id.btnSelectSeats);
        
        rgBookingFor = findViewById(R.id.rgBookingFor);
        layoutFriendDetails = findViewById(R.id.layoutFriendDetails);
        etFriendName = findViewById(R.id.etFriendName);
        etFriendPhone = findViewById(R.id.etFriendPhone);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        rgBookingFor.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbForFriend) {
                layoutFriendDetails.setVisibility(View.VISIBLE);
            } else {
                layoutFriendDetails.setVisibility(View.GONE);
            }
        });

        if (btnSelectSeats != null) {
            btnSelectSeats.setOnClickListener(v -> {
                String bookingFor = (rgBookingFor.getCheckedRadioButtonId() == R.id.rbForMe) ? "ME" : "FRIEND";
                String selectedFriendName = "";
                String selectedFriendPhone = "";

                if (Objects.equals(bookingFor, "FRIEND")) {
                    if (etFriendName.getText() != null) {
                        selectedFriendName = etFriendName.getText().toString().trim();
                    }
                    if (etFriendPhone.getText() != null) {
                        selectedFriendPhone = etFriendPhone.getText().toString().trim();
                    }

                    if (selectedFriendName.isEmpty() || selectedFriendPhone.isEmpty()) {
                        Toast.makeText(this, "Please enter friend's details", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Intent intent = new Intent(BusDetails.this, SeatSelectionActivity.class);
                intent.putExtra("BOOKING_FOR", bookingFor);
                intent.putExtra("FRIEND_NAME", selectedFriendName);
                intent.putExtra("FRIEND_PHONE", selectedFriendPhone);
                startActivity(intent);
            });
        }
    }
}
