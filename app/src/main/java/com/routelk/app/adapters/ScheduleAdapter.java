package com.routelk.app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;
import com.routelk.app.models.Schedule;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private Context context;
    private List<Schedule> scheduleList;
    private FirebaseFirestore db;

    public interface OnEditClickListener {
        void onEdit(Schedule schedule);
    }

    private OnEditClickListener listener;

    public ScheduleAdapter(Context context,
                           List<Schedule> scheduleList,
                           OnEditClickListener listener) {

        this.context = context;
        this.scheduleList = scheduleList;
        this.listener = listener;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_schedule, parent, false);

        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {

        Schedule schedule = scheduleList.get(position);

        holder.tvScheduleID.setText("Schedule : " + schedule.getScheduleID());

        holder.tvBusID.setText("Bus : " + schedule.getBusID());

        holder.tvRouteID.setText("Route : " + schedule.getRouteID());

        holder.tvDeparture.setText("Departure : " + schedule.getDepartureTime());

        holder.tvArrival.setText("Arrival : " + schedule.getArrivalTime());

        holder.tvPrice.setText("Price : Rs." + schedule.getPrice());

        if (schedule.getOperatingDays() != null) {
            holder.tvOperatingDays.setText(schedule.getOperatingDays().toString());
        } else {
            holder.tvOperatingDays.setText("");
        }

        // EDIT

        holder.editBtn.setOnClickListener(v -> {

            if (listener != null) {
                listener.onEdit(schedule);
            }

        });

        // DELETE

        holder.deleteBtn.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete Schedule")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Delete", (dialog, which) -> {

                        db.collection("schedules")
                                .document(schedule.getScheduleID())
                                .delete()
                                .addOnSuccessListener(unused ->

                                        Toast.makeText(context,
                                                "Schedule Deleted",
                                                Toast.LENGTH_SHORT).show())

                                .addOnFailureListener(e ->

                                        Toast.makeText(context,
                                                e.getMessage(),
                                                Toast.LENGTH_SHORT).show());

                    })
                    .setNegativeButton("Cancel", null)
                    .show();

        });

    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {

        TextView tvScheduleID;
        TextView tvBusID;
        TextView tvRouteID;
        TextView tvDeparture;
        TextView tvArrival;
        TextView tvPrice;
        TextView tvOperatingDays;

        Button editBtn;
        Button deleteBtn;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);

            tvScheduleID = itemView.findViewById(R.id.tvScheduleID);
            tvBusID = itemView.findViewById(R.id.tvBusID);
            tvRouteID = itemView.findViewById(R.id.tvRouteID);
            tvDeparture = itemView.findViewById(R.id.tvDeparture);
            tvArrival = itemView.findViewById(R.id.tvArrival);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOperatingDays = itemView.findViewById(R.id.tvOperatingDays);

            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}