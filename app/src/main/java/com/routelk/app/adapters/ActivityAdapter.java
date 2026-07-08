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

        holder.tvRoute.setText(booking.getFrom() + " → " + booking.getTo());
        holder.tvBus.setText(booking.getBusName());
        holder.tvSeat.setText(booking.getSeatNo());
        holder.tvDate.setText(booking.getDate());
        holder.tvBookingId.setText(booking.getId());
        
        if (isCompleted) {
            holder.tvStatus.setText("Completed");
            holder.tvStatus.setBackgroundResource(R.drawable.status_confirmed_bg);
            holder.btnActionRight.setText(R.string.add_review);
            holder.btnActionRight.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setText("Upcoming");
            // You might want a different background for upcoming, reusing confirmed for now
            holder.tvStatus.setBackgroundResource(R.drawable.status_confirmed_bg);
            holder.btnActionRight.setVisibility(View.GONE);
            // If hidden, the left button should probably take full width or we adjust layout
        }

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
        }
    }
}
