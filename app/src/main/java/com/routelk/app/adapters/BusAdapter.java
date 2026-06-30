package com.routelk.app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;
import com.routelk.app.models.Bus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    Context context;
    List<Bus> busList;

    public BusAdapter(Context context, List<Bus> busList) {
        this.context = context;
        this.busList = busList;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_bus, parent, false);

        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {

        Bus bus = busList.get(position);

        holder.tvBusName.setText(bus.getBusName());
        holder.tvBusNumber.setText("Number : " + bus.getBusNumber());
        holder.tvBusType.setText("Type : " + bus.getBusType());
        holder.tvSeats.setText("Seats : " + bus.getTotalSeats());

        // ================= EDIT ====================

        holder.editBtn.setOnClickListener(v -> {

            View dialogView = LayoutInflater.from(context)
                    .inflate(R.layout.dialog_edit_bus, null);

            EditText etName = dialogView.findViewById(R.id.etBusName);
            EditText etNumber = dialogView.findViewById(R.id.etBusNumber);
            EditText etType = dialogView.findViewById(R.id.etBusType);
            EditText etSeats = dialogView.findViewById(R.id.etSeats);

            etName.setText(bus.getBusName());
            etNumber.setText(bus.getBusNumber());
            etType.setText(bus.getBusType());
            etSeats.setText(bus.getTotalSeats());

            new AlertDialog.Builder(context)
                    .setTitle("Update Bus")
                    .setView(dialogView)

                    .setPositiveButton("Update", (dialog, which) -> {

                        Map<String,Object> update = new HashMap<>();

                        update.put("busName", etName.getText().toString().trim());
                        update.put("busNumber", etNumber.getText().toString().trim());
                        update.put("busType", etType.getText().toString().trim());
                        update.put("totalSeats", etSeats.getText().toString().trim());

                        FirebaseFirestore.getInstance()
                                .collection("buses")
                                .document(bus.getId())
                                .update(update)
                                .addOnSuccessListener(unused -> {

                                    bus.setBusName(etName.getText().toString());
                                    bus.setBusNumber(etNumber.getText().toString());
                                    bus.setBusType(etType.getText().toString());
                                    bus.setTotalSeats(etSeats.getText().toString());

                                    notifyItemChanged(holder.getAdapterPosition());

                                });

                    })

                    .setNegativeButton("Cancel", null)
                    .show();

        });

        // ================= DELETE ====================

        holder.deleteBtn.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete Bus")
                    .setMessage("Are you sure you want to delete this bus?")

                    .setPositiveButton("Delete", (dialog, which) -> {

                        FirebaseFirestore.getInstance()
                                .collection("buses")
                                .document(bus.getId())
                                .delete()
                                .addOnSuccessListener(unused -> {

                                    int pos = holder.getAdapterPosition();

                                    busList.remove(pos);

                                    notifyItemRemoved(pos);
                                    notifyItemRangeChanged(pos, busList.size());

                                });

                    })

                    .setNegativeButton("Cancel", null)
                    .show();

        });

    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    static class BusViewHolder extends RecyclerView.ViewHolder {

        TextView tvBusName;
        TextView tvBusNumber;
        TextView tvBusType;
        TextView tvSeats;

        Button editBtn;
        Button deleteBtn;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBusName = itemView.findViewById(R.id.tvBusName);
            tvBusNumber = itemView.findViewById(R.id.tvBusNumber);
            tvBusType = itemView.findViewById(R.id.tvBusType);
            tvSeats = itemView.findViewById(R.id.tvSeats);

            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}