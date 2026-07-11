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

import com.routelk.app.R;
import com.routelk.app.models.Route;
import com.routelk.app.services.RouteService;

import java.util.List;

public class RouteAdapter
        extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private Context context;
    private List<Route> routeList;
    private RouteService routeService;

    public RouteAdapter(Context context,
                        List<Route> routeList) {

        this.context = context;
        this.routeList = routeList;
        this.routeService = new RouteService();
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.route_item,
                                parent,
                                false);

        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RouteViewHolder holder,
            int position) {

        Route route = routeList.get(position);

        holder.tvRouteID.setText(
                "Route ID : " + route.getRouteID()
        );

        holder.tvRouteName.setText(route.getRouteName());

        holder.tvFromTo.setText(
                route.getFrom()
                        + " → "
                        + route.getTo());

        holder.tvDistance.setText(
                "Distance : "
                        + route.getDistance()
                        + " km");

        holder.tvPrice.setText(
                "Price : Rs."
                        + route.getPrice());


        holder.deleteBtn.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete Route")
                    .setMessage("Are you sure?")
                    .setPositiveButton(
                            "Delete",
                            (dialog, which) -> {
                                routeService.deleteRoute(route.getId(), task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Route deleted", Toast.LENGTH_SHORT).show();
                                        routeList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, routeList.size());
                                    } else {
                                        Toast.makeText(context, "Failed to delete route", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            })
                    .setNegativeButton(
                            "Cancel",
                            null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    static class RouteViewHolder
            extends RecyclerView.ViewHolder {
        TextView tvRouteID;
        TextView tvRouteName;
        TextView tvFromTo;
        TextView tvDistance;
        TextView tvPrice;
        Button deleteBtn;

        public RouteViewHolder(
                @NonNull View itemView) {

            super(itemView);

            tvRouteID =
                    itemView.findViewById(
                            R.id.tvRouteID
                    );

            tvRouteName =
                    itemView.findViewById(
                            R.id.tvRouteName);

            tvFromTo =
                    itemView.findViewById(
                            R.id.tvFromTo);

            tvDistance =
                    itemView.findViewById(
                            R.id.tvDistance);

            tvPrice =
                    itemView.findViewById(
                            R.id.tvPrice);

            deleteBtn =
                    itemView.findViewById(
                            R.id.deleteBtn);
        }
    }
}