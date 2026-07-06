package com.routelk.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.routelk.app.R;
import com.routelk.app.models.Booking;

import java.util.List;

public class BookingAdapter
        extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking,
                        parent,
                        false);

        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull BookingViewHolder holder,
            int position) {

        Booking booking = bookingList.get(position);

        holder.tvUserName.setText("Booked by: " + booking.getUserName());
        
        if (booking.getPassengerName() != null && !booking.getPassengerName().equals(booking.getUserName())) {
            holder.tvPassengerName.setVisibility(View.VISIBLE);
            holder.tvPassengerName.setText("Passenger: " + booking.getPassengerName());
        } else {
            holder.tvPassengerName.setVisibility(View.GONE);
        }

        holder.tvRoute.setText(booking.getFrom() + " → " + booking.getTo());
        holder.tvBus.setText("Bus: " + booking.getBusName());
        holder.tvSeat.setText("Seat: " + booking.getSeatNo());
        holder.tvDate.setText(booking.getDate());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvUserName,
                tvPassengerName,
                tvRoute,
                tvBus,
                tvSeat,
                tvDate;

        public BookingViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvPassengerName = itemView.findViewById(R.id.tvPassengerName);
            tvRoute = itemView.findViewById(R.id.tvRoute);
            tvBus = itemView.findViewById(R.id.tvBus);
            tvSeat = itemView.findViewById(R.id.tvSeat);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}