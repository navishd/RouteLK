package com.routelk.app.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.routelk.app.R;
import com.routelk.app.activities.BusDetails;
import com.routelk.app.models.Bus;


import java.util.ArrayList;
public class AvailableBusAdapter extends RecyclerView.Adapter<AvailableBusAdapter.BusViewHolder> {


    private Context context;
    private ArrayList<Bus> busList;

    private String from;
    private String to;
    private String date;
    private String time;
    private String passengers;
    private boolean isForOthers;



    public AvailableBusAdapter(
            Context context,
            ArrayList<Bus> busList,
            String from,
            String to,
            String date,
            String time,
            String passengers,
            boolean isForOthers
    ){

        this.context = context;
        this.busList = busList;

        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
        this.passengers = passengers;
        this.isForOthers = isForOthers;
    }





    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ){

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_available_bus,
                                parent,
                                false
                        );


        return new BusViewHolder(view);

    }





    @Override
    public void onBindViewHolder(
            @NonNull BusViewHolder holder,
            int position
    ){

        Bus bus =
                busList.get(position);



        holder.tvBusName.setText(
                bus.getBusName()
        );


        holder.tvBusNumber.setText(
                "Bus No : " + bus.getBusNumber()
        );


        holder.tvBusType.setText(
                "Type : " + bus.getBusType()
        );


        holder.tvRoute.setText(
                bus.getFrom() + " → " + bus.getTo()
        );


        holder.tvDeparture.setText(
                "Departure : " + bus.getDepartureTime()
        );


        holder.tvArrival.setText(
                "Arrival : " + bus.getArrivalTime()
        );


        holder.tvPrice.setText(
                "Price : Rs. " + bus.getPrice()
        );


        holder.tvSeats.setText(
                "Seats : " + bus.getSeatCount()
        );





        holder.selectSeatBtn.setOnClickListener(v -> {


            Intent intent =
                    new Intent(
                            context,
                            BusDetails.class
                    );



            intent.putExtra(
                    "BUS_ID",
                    bus.getBusID()
            );


            intent.putExtra(
                    "BUS_NAME",
                    bus.getBusName()
            );


            intent.putExtra(
                    "BUS_NUMBER",
                    bus.getBusNumber()
            );


            intent.putExtra(
                    "BUS_TYPE",
                    bus.getBusType()
            );


            intent.putExtra(
                    "ROUTE_ID",
                    bus.getRouteId()
            );


            intent.putExtra(
                    "SCHEDULE_ID",
                    bus.getScheduleId()
            );


            intent.putExtra(
                    "FROM",
                    bus.getFrom()
            );


            intent.putExtra(
                    "TO",
                    bus.getTo()
            );


            intent.putExtra(
                    "DEPARTURE_TIME",
                    bus.getDepartureTime()
            );


            intent.putExtra(
                    "ARRIVAL_TIME",
                    bus.getArrivalTime()
            );


            intent.putExtra(
                    "PRICE",
                    bus.getPrice()
            );


            intent.putExtra(
                    "SEATS",
                    bus.getSeatCount()
            );


            intent.putExtra(
                    "DATE",
                    date
            );

            intent.putExtra("IS_FOR_OTHERS", isForOthers);

            context.startActivity(intent);


        });



    }





    @Override
    public int getItemCount(){

        return busList.size();

    }





    public static class BusViewHolder
            extends RecyclerView.ViewHolder{


        TextView tvBusName;
        TextView tvBusNumber;
        TextView tvBusType;
        TextView tvRoute;
        TextView tvDeparture;
        TextView tvArrival;
        TextView tvPrice;
        TextView tvSeats;

        Button selectSeatBtn;



        public BusViewHolder(
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


            tvRoute =
                    itemView.findViewById(
                            R.id.tvRoute
                    );


            tvDeparture =
                    itemView.findViewById(
                            R.id.tvDeparture
                    );


            tvArrival =
                    itemView.findViewById(
                            R.id.tvArrival
                    );


            tvPrice =
                    itemView.findViewById(
                            R.id.tvPrice
                    );


            tvSeats =
                    itemView.findViewById(
                            R.id.tvSeats
                    );


            selectSeatBtn =
                    itemView.findViewById(
                            R.id.selectSeatBtn
                    );


        }


    }


}