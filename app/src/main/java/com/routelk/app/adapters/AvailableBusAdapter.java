package com.routelk.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.routelk.app.R;
import com.routelk.app.activities.BusDetails;
import com.routelk.app.models.Bus;

import java.util.List;


public class AvailableBusAdapter extends RecyclerView.Adapter<AvailableBusAdapter.ViewHolder> {


    private Context context;
    private List<Bus> busList;

    private String from;
    private String to;
    private String date;
    private String time;


    public AvailableBusAdapter(
            Context context,
            List<Bus> busList,
            String from,
            String to,
            String date,
            String time,
            String routeId,
            String scheduleId
    ){

        this.context = context;
        this.busList = busList;

        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ){

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_available_bus,
                        parent,
                        false
                );


        return new ViewHolder(view);

    }




    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ){

        Bus bus = busList.get(position);



        // Bus Details

        holder.tvBusName.setText(
                bus.getBusName()
        );


        holder.tvBusNumber.setText(
                bus.getBusNumber()
        );


        holder.tvBusType.setText(
                bus.getBusType()
        );


        holder.tvSeats.setText(
                "Seats : " + bus.getSeatCount()
        );



        // Route Details

        if(bus.getFrom() != null && bus.getTo() != null){

            holder.tvRoute.setText(
                    bus.getFrom()
                            +
                            " → "
                            +
                            bus.getTo()
            );

        }



        // Time Details

        holder.tvDepartureTime.setText(
                bus.getDepartureTime()
        );


        holder.tvArrivalTime.setText(
                bus.getArrivalTime()
        );



        // Price

        holder.tvPrice.setText(
                "Rs. " + bus.getPrice()
        );


        holder.btnSelect.setOnClickListener(v -> {

            Intent intent = new Intent(context, BusDetails.class);

            intent.putExtra("BUS_ID", bus.getBusID());
            intent.putExtra("BUS_NAME", bus.getBusName());
            intent.putExtra("BUS_NUMBER", bus.getBusNumber());
            intent.putExtra("BUS_TYPE", bus.getBusType());
            intent.putExtra("SEATS", bus.getSeatCount());

            intent.putExtra("FROM", bus.getFrom());
            intent.putExtra("TO", bus.getTo());

            intent.putExtra("DEPARTURE_TIME", bus.getDepartureTime());
            intent.putExtra("ARRIVAL_TIME", bus.getArrivalTime());

            intent.putExtra("PRICE", bus.getPrice());

            intent.putExtra("DATE", date);
            intent.putExtra("TIME", time);

            // IMPORTANT
            intent.putExtra("ROUTE_ID", bus.getRouteId());
            intent.putExtra("SCHEDULE_ID", bus.getScheduleId());

            context.startActivity(intent);

        });


    }




    @Override
    public int getItemCount(){

        if(busList == null){

            return 0;

        }

        return busList.size();

    }






    public static class ViewHolder extends RecyclerView.ViewHolder{


        TextView tvBusName;
        TextView tvBusNumber;
        TextView tvBusType;
        TextView tvSeats;

        TextView tvRoute;

        TextView tvDepartureTime;
        TextView tvArrivalTime;

        TextView tvPrice;


        MaterialButton btnSelect;



        public ViewHolder(
                @NonNull View itemView
        ){

            super(itemView);



            tvBusName =
                    itemView.findViewById(
                            R.id.tvBusName
                    );


            tvBusNumber =
                    itemView.findViewById(
                            R.id.tvBusNumber
                    );


            tvBusType =
                    itemView.findViewById(
                            R.id.tvBusType
                    );


            tvSeats =
                    itemView.findViewById(
                            R.id.tvSeats
                    );



            tvRoute =
                    itemView.findViewById(
                            R.id.tvRoute
                    );



            tvDepartureTime =
                    itemView.findViewById(
                            R.id.tvDepartureTime
                    );


            tvArrivalTime =
                    itemView.findViewById(
                            R.id.tvArrivalTime
                    );



            tvPrice =
                    itemView.findViewById(
                            R.id.tvPrice
                    );



            btnSelect =
                    itemView.findViewById(
                            R.id.btnSelect
                    );

        }

    }

}