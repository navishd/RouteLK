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

        String from = booking.getFrom() != null ? booking.getFrom() : "Origin";
        String to = booking.getTo() != null ? booking.getTo() : "Destination";
        holder.tvRoute.setText(from + " → " + to);
        
        holder.tvBus.setText(booking.getBusName() != null ? booking.getBusName() : "Bus Name");
        holder.tvSeat.setText(booking.getSeatNo());
        
        String dateText = booking.getDate() != null ? booking.getDate() : "";
        if (booking.getTime() != null && !booking.getTime().isEmpty()) {
            dateText += " • " + booking.getTime();
        }
        holder.tvDate.setText(dateText.isEmpty() ? "Date & Time" : dateText);

        holder.tvBookingId.setText(booking.getId());

        holder.btnViewTicket.setVisibility(View.VISIBLE);
        holder.btnViewTicket.setOnClickListener(v -> {
            if (onViewTicketClickListener != null) {
                onViewTicketClickListener.onViewTicketClick(booking);
            }
        });
    }

    private OnViewTicketClickListener onViewTicketClickListener;

    public interface OnViewTicketClickListener {
        void onViewTicketClick(Booking booking);
    }

    public void setOnViewTicketClickListener(OnViewTicketClickListener listener) {
        this.onViewTicketClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvRoute,
                tvBus,
                tvSeat,
                tvDate,
                tvBookingId,
                btnViewTicket;

        public BookingViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvRoute = itemView.findViewById(R.id.tvRoute);
            tvBus = itemView.findViewById(R.id.tvBus);
            tvSeat = itemView.findViewById(R.id.tvSeat);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            btnViewTicket = itemView.findViewById(R.id.btnViewTicket);
        }
    }
}