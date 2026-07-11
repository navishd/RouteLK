package com.routelk.app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.routelk.app.activities.BusDetails;
import com.routelk.app.models.Bus;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    private final Context context;
    private final List<Bus> busList;
    private final FirebaseFirestore db;

    private boolean userMode;

    private String from;
    private String to;
    private String date;
    private String time;

    // =========================
    // ADMIN
    // =========================
    public BusAdapter(Context context, List<Bus> busList) {

        this.context = context;
        this.busList = busList;
        this.db = FirebaseFirestore.getInstance();

        this.userMode = false;
    }

    // =========================
    // USER
    // =========================
    public BusAdapter(Context context,
                      List<Bus> busList,
                      String from,
                      String to,
                      String date,
                      String time) {

        this.context = context;
        this.busList = busList;

        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;

        this.db = FirebaseFirestore.getInstance();

        this.userMode = true;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_bus, parent, false);

        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder,
                                 int position) {

        Bus bus = busList.get(position);

        holder.tvBusName.setText(bus.getBusName());
        holder.tvBusId.setText(bus.getBusID());
        holder.tvBusNumber.setText(bus.getBusNumber());
        holder.tvBusType.setText(bus.getBusType());
        holder.tvSeats.setText("Seats : " + bus.getTotalSeats());

        // ================= USER =================

        if (userMode) {

            holder.editBtn.setVisibility(View.GONE);
            holder.deleteBtn.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(v -> {

                Intent intent =
                        new Intent(context, BusDetails.class);

                intent.putExtra("BUS_ID", bus.getBusID());
                intent.putExtra("BUS_NAME", bus.getBusName());

                intent.putExtra("FROM", from);
                intent.putExtra("TO", to);
                intent.putExtra("DATE", date);
                intent.putExtra("TIME", time);

                context.startActivity(intent);

            });

            return;
        }

        // ================= ADMIN =================

        holder.editBtn.setVisibility(View.VISIBLE);
        holder.deleteBtn.setVisibility(View.VISIBLE);

        holder.editBtn.setOnClickListener(v ->

                Toast.makeText(
                        context,
                        "Edit Bus - Coming Soon",
                        Toast.LENGTH_SHORT
                ).show()

        );

        holder.deleteBtn.setOnClickListener(v ->

                new AlertDialog.Builder(context)
                        .setTitle("Delete Bus")
                        .setMessage("Delete this bus?")
                        .setPositiveButton("Delete", (dialog, which) ->

                                db.collection("buses")
                                        .document(bus.getBusID())
                                        .delete()
                                        .addOnSuccessListener(unused -> {

                                            Toast.makeText(
                                                    context,
                                                    "Bus Deleted",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            busList.remove(position);
                                            notifyItemRemoved(position);

                                        })

                        )
                        .setNegativeButton("Cancel", null)
                        .show()

        );

    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public static class BusViewHolder extends RecyclerView.ViewHolder {

        TextView tvBusName;
        TextView tvBusId;
        TextView tvBusNumber;
        TextView tvBusType;
        TextView tvSeats;

        Button editBtn;
        Button deleteBtn;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBusName = itemView.findViewById(R.id.tvBusName);
            tvBusId = itemView.findViewById(R.id.tvBusID);
            tvBusNumber = itemView.findViewById(R.id.tvBusNumber);
            tvBusType = itemView.findViewById(R.id.tvBusType);
            tvSeats = itemView.findViewById(R.id.tvSeats);

            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}