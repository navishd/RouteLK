package com.routelk.app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;
import com.routelk.app.adapters.BookingAdapter;
import com.routelk.app.models.Booking;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import android.widget.Button;
import android.widget.Toast;

public class ViewBookingsActivity
        extends AppCompatActivity {

    RecyclerView recyclerBookings;

    List<Booking> bookingList;

    BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        recyclerBookings = findViewById(R.id.recyclerBookings);

        recyclerBookings.setLayoutManager(
                new LinearLayoutManager(this));

        bookingList = new ArrayList<>();

        adapter = new BookingAdapter(bookingList);

        recyclerBookings.setAdapter(adapter);

        adapter.setOnViewTicketClickListener(this::showBookingDialog);

        loadBookings();

    }
    private void loadBookings() {

        FirebaseFirestore.getInstance()
                .collection("bookings")
                .get()
                .addOnSuccessListener(query -> {

                    bookingList.clear();

                    query.forEach(document -> {

                        Booking booking =
                                document.toObject(Booking.class);

                        booking.setDocumentId(document.getId());

                        bookingList.add(booking);
                    });

                    adapter.notifyDataSetChanged();
                });
    }
    private void showBookingDialog(Booking booking) {

        View view = LayoutInflater.from(this)
                .inflate(R.layout.booking_details_dialog, null);

        TextView tvPassenger = view.findViewById(R.id.tvPassenger);
        TextView tvPhone = view.findViewById(R.id.tvPhone);
        TextView tvRoute = view.findViewById(R.id.tvRoute);
        TextView tvBus = view.findViewById(R.id.tvBus);
        TextView tvDate = view.findViewById(R.id.tvDate);
        TextView tvSeat = view.findViewById(R.id.tvSeat);
        TextView tvBookingId = view.findViewById(R.id.tvBookingId);
        TextView tvTime = view.findViewById(R.id.tvTime);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvStatus = view.findViewById(R.id.tvStatus);

        Button btnCancelBooking =
                view.findViewById(R.id.btnCancelBooking);

        tvPassenger.setText("Passenger : " + booking.getPassengerName());

        tvPhone.setText("Phone : " + booking.getPassengerPhone());

        tvRoute.setText(
                "Route : "
                        + booking.getFrom()
                        + " → "
                        + booking.getTo());

        tvBus.setText("Bus : " + booking.getBusName());

        tvDate.setText("Date : " + booking.getDate());

        tvSeat.setText("Seat : " + booking.getSeatNo());

        tvBookingId.setText("Booking ID : " + booking.getId());

        tvTime.setText("Time : " + booking.getTime());

        tvPrice.setText("Price : Rs." + booking.getPrice());

        tvStatus.setText("Status : " + booking.getStatus());

        if (booking.getStatus() == null) {

            tvStatus.setText("Status : Pending");
            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));

        } else if (booking.getStatus().equals("CONFIRMED")) {

            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        } else if (booking.getStatus().equals("Cancelled")) {

            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        } else {

            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        }

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Close", null)
                .create();


        btnCancelBooking.setOnClickListener(v -> {


            FirebaseFirestore.getInstance()
                    .collection("bookings")
                    .document(booking.getDocumentId())
                    .update("status", "Cancelled")

                    .addOnSuccessListener(unused -> {

                        Toast.makeText(
                                ViewBookingsActivity.this,
                                "Booking Cancelled Successfully",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadBookings();

                        dialog.dismiss();

                    })

                    .addOnFailureListener(e -> {

                        Toast.makeText(
                                ViewBookingsActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();

                    });

        });

        dialog.show();


    }
}