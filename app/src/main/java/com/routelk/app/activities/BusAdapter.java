package com.routelk.app.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.routelk.app.R;
import com.routelk.app.models.Bus;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    private List<Bus> busList;

    public BusAdapter(List<Bus> busList) {
        this.busList = busList;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.busName.setText(bus.getName());
        holder.busTime.setText(bus.getTime());
        holder.busType.setText(bus.getType());
        holder.busPrice.setText(bus.getPrice());
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    static class BusViewHolder extends RecyclerView.ViewHolder {
        TextView busName, busTime, busType, busPrice;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            busName = itemView.findViewById(R.id.busName);
            busTime = itemView.findViewById(R.id.busTime);
            busType = itemView.findViewById(R.id.busType);
            busPrice = itemView.findViewById(R.id.busPrice);
        }
    }
}