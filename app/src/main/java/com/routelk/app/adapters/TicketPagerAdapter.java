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
        
        String from = booking.getFrom() != null ? booking.getFrom() : "";
        holder.tvFromCity.setText(from.toUpperCase());
        holder.tvFromCode.setText(from.substring(0, Math.min(from.length(), 3)).toUpperCase());
        
        String to = booking.getTo() != null ? booking.getTo() : "";
        holder.tvToCity.setText(to.toUpperCase());
        holder.tvToCode.setText(to.substring(0, Math.min(to.length(), 3)).toUpperCase());
        
        String date = booking.getDate() != null ? booking.getDate() : "";
        holder.tvDateDetail.setText(date.toUpperCase());
        
        holder.tvSeatDetail.setText(booking.getSeatNo());
        holder.tvBusDetail.setText(booking.getBusName());
        
        String passengerName = booking.getPassengerName() != null ? booking.getPassengerName() : "";
        holder.tvPassengerDetail.setText(passengerName.toUpperCase());

        if (booking.isForOthers()) {
            holder.badgeForOthers.setVisibility(View.VISIBLE);
        } else {
            holder.badgeForOthers.setVisibility(View.GONE);
        }

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
        TextView tvBookingCode, tvFromCity, tvToCity, tvDateDetail, tvSeatDetail, tvBusDetail, tvFromCode, tvToCode, tvStartTime, tvBoardingTime, tvPassengerDetail;
        View badgeForOthers;

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
            tvPassengerDetail = itemView.findViewById(R.id.tvPassengerDetail);
            badgeForOthers = itemView.findViewById(R.id.badgeForOthers);
        }
    }
}
