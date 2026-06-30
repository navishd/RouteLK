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

public class ViewBookingsActivity
        extends AppCompatActivity {

    RecyclerView recyclerBookings;

    List<Booking> bookingList;

    BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        recyclerBookings =
                findViewById(R.id.recyclerBookings);

        recyclerBookings.setLayoutManager(
                new LinearLayoutManager(this));

        bookingList = new ArrayList<>();

        adapter =
                new BookingAdapter(bookingList);

        recyclerBookings.setAdapter(adapter);

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
                                document.toObject(
                                        Booking.class);

                        bookingList.add(booking);
                    });

                    adapter.notifyDataSetChanged();
                });
    }
}