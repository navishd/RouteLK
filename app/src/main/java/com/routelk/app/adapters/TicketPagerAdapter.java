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

public class TicketPagerAdapter extends RecyclerView.Adapter<TicketPagerAdapter.TicketViewHolder> {

    private final List<Booking> bookingList;

    public TicketPagerAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket_card, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvBookingCode.setText(booking.getId());
        holder.tvFromCity.setText(booking.getFrom().toUpperCase());
        holder.tvFromCode.setText(booking.getFrom().substring(0, Math.min(booking.getFrom().length(), 3)).toUpperCase());
        
        holder.tvToCity.setText(booking.getTo().toUpperCase());
        holder.tvToCode.setText(booking.getTo().substring(0, Math.min(booking.getTo().length(), 3)).toUpperCase());
        
        holder.tvDateDetail.setText(booking.getDate().toUpperCase());
        holder.tvSeatDetail.setText(booking.getSeatNo());
        holder.tvBusDetail.setText(booking.getBusName());

        if (booking.getTime() != null && !booking.getTime().equals("N/A")) {
            holder.tvStartTime.setText(booking.getTime());
            holder.tvBoardingTime.setText(booking.getTime()); // Or subtract 15 mins
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookingCode, tvFromCity, tvToCity, tvDateDetail, tvSeatDetail, tvBusDetail, tvFromCode, tvToCode, tvStartTime, tvBoardingTime;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingCode = itemView.findViewById(R.id.tvBookingCode);
            tvFromCity = itemView.findViewById(R.id.tvFromCity);
            tvToCity = itemView.findViewById(R.id.tvToCity);
            tvDateDetail = itemView.findViewById(R.id.tvDateDetail);
            tvSeatDetail = itemView.findViewById(R.id.tvSeatDetail);
            tvBusDetail = itemView.findViewById(R.id.tvBusDetail);
            tvFromCode = itemView.findViewById(R.id.tvFromCode);
            tvToCode = itemView.findViewById(R.id.tvToCode);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvBoardingTime = itemView.findViewById(R.id.tvBoardingTime);
        }
    }
}
