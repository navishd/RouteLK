package com.routelk.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.routelk.app.R;
import com.routelk.app.models.Booking;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ActivityAdapter
        extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private final List<Booking> activityList;
    private final boolean isCompleted;

    public ActivityAdapter(List<Booking> activityList, boolean isCompleted) {
        this.activityList = activityList;
        this.isCompleted = isCompleted;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity,
                        parent,
                        false);

        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ActivityViewHolder holder,
            int position) {

        Booking booking = activityList.get(position);

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

        // Show "For Others" badge if passenger name is different from user name
        if (booking.getUserName() != null && booking.getPassengerName() != null &&
                !booking.getUserName().equalsIgnoreCase(booking.getPassengerName())) {
            holder.layoutForOthers.setVisibility(View.VISIBLE);
        } else {
            holder.layoutForOthers.setVisibility(View.GONE);
        }
        
        if (isCompleted) {
            holder.tvStatus.setText("Completed");
            holder.tvStatus.setBackgroundResource(R.drawable.status_confirmed_bg);
            holder.btnActionRight.setText(R.string.add_review);
            holder.btnActionRight.setVisibility(View.VISIBLE);
            holder.btnDivider.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setText("Upcoming");
            holder.tvStatus.setBackgroundResource(R.drawable.status_confirmed_bg);
            holder.btnActionRight.setVisibility(View.GONE);
            holder.btnDivider.setVisibility(View.GONE);
        }

        holder.btnActionLeft.setVisibility(View.VISIBLE);
        holder.btnActionLeft.setOnClickListener(v -> {
            if (onActivityClickListener != null) {
                onActivityClickListener.onViewTicketClick(booking);
            }
        });
        
        holder.btnActionRight.setOnClickListener(v -> {
            if (onActivityClickListener != null) {
                onActivityClickListener.onSecondaryActionClick(booking);
            }
        });
    }

    private OnActivityClickListener onActivityClickListener;

    public interface OnActivityClickListener {
        void onViewTicketClick(Booking booking);
        void onSecondaryActionClick(Booking booking);
    }

    public void setOnActivityClickListener(OnActivityClickListener listener) {
        this.onActivityClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    static class ActivityViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvRoute,
                tvBus,
                tvSeat,
                tvDate,
                tvBookingId,
                tvStatus,
                btnActionLeft,
                btnActionRight;
        View btnDivider, layoutForOthers;

        public ActivityViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvRoute = itemView.findViewById(R.id.tvRoute);
            tvBus = itemView.findViewById(R.id.tvBus);
            tvSeat = itemView.findViewById(R.id.tvSeat);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnActionLeft = itemView.findViewById(R.id.btnActionLeft);
            btnActionRight = itemView.findViewById(R.id.btnActionRight);
            btnDivider = itemView.findViewById(R.id.btnDivider);
            layoutForOthers = itemView.findViewById(R.id.layoutForOthers);
        }
    }
}
