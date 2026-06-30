package com.routelk.app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.routelk.app.R;
import com.routelk.app.models.Route;

import java.util.List;

public class RouteAdapter
        extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private Context context;
    private List<Route> routeList;

    public RouteAdapter(Context context,
                        List<Route> routeList) {

        this.context = context;
        this.routeList = routeList;
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

                                FirebaseFirestore
                                        .getInstance()
                                        .collection("routes")
                                        .document(route.getId())
                                        .delete();
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

        TextView tvRouteName;
        TextView tvFromTo;
        TextView tvDistance;
        TextView tvPrice;
        Button deleteBtn;

        public RouteViewHolder(
                @NonNull View itemView) {

            super(itemView);

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