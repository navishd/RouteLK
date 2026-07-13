package com.routelk.app.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.routelk.app.R;
import com.routelk.app.models.Booking;


import java.util.List;



public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {


    private Context context;
    private List<Booking> ticketList;



    public TicketAdapter(Context context, List<Booking> ticketList){

        this.context = context;
        this.ticketList = ticketList;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {


        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_ticket_card,
                        parent,
                        false
                );


        return new ViewHolder(view);

    }





    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {


        Booking booking = ticketList.get(position);



        // From
        holder.tvFromCode.setText(
                booking.getFrom()
        );


        holder.tvFromCity.setText(
                booking.getFrom()
        );



        // To
        holder.tvToCode.setText(
                booking.getTo()
        );


        holder.tvToCity.setText(
                booking.getTo()
        );



        // Time
        holder.tvStartTime.setText(
                booking.getTime()
        );



        holder.tvEndTime.setText(
                booking.getTime()
        );



        holder.tvBoardingTime.setText(
                booking.getTime()
        );



        // Details

        holder.tvBusDetail.setText(
                booking.getBusName()
        );



        holder.tvDateDetail.setText(
                booking.getDate()
        );



        holder.tvSeatDetail.setText(
                booking.getSeatNo()
        );



        // Booking ID
        holder.tvBookingCode.setText(
                "BOOK-" + (booking.getId() != null ? booking.getId() : "")
        );

        if (booking.isForOthers()) {
            holder.badgeForOthers.setVisibility(View.VISIBLE);
        } else {
            holder.badgeForOthers.setVisibility(View.GONE);
        }

    }






    @Override
    public int getItemCount() {

        return ticketList.size();

    }







    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvFromCode;
        TextView tvFromCity;

        TextView tvToCode;
        TextView tvToCity;


        TextView tvStartTime;
        TextView tvEndTime;


        TextView tvBusDetail;
        TextView tvDateDetail;
        TextView tvSeatDetail;


        TextView tvBoardingTime;


        TextView tvBookingCode;

        View badgeForOthers;



        public ViewHolder(@NonNull View itemView) {

            super(itemView);



            tvFromCode =
                    itemView.findViewById(
                            R.id.tvFromCode
                    );


            tvFromCity =
                    itemView.findViewById(
                            R.id.tvFromCity
                    );


            tvToCode =
                    itemView.findViewById(
                            R.id.tvToCode
                    );


            tvToCity =
                    itemView.findViewById(
                            R.id.tvToCity
                    );


            tvStartTime =
                    itemView.findViewById(
                            R.id.tvStartTime
                    );


            tvEndTime =
                    itemView.findViewById(
                            R.id.tvEndTime
                    );


            tvBusDetail =
                    itemView.findViewById(
                            R.id.tvBusDetail
                    );


            tvDateDetail =
                    itemView.findViewById(
                            R.id.tvDateDetail
                    );


            tvSeatDetail =
                    itemView.findViewById(
                            R.id.tvSeatDetail
                    );


            tvBoardingTime =
                    itemView.findViewById(
                            R.id.tvBoardingTime
                    );


            tvBookingCode =
                    itemView.findViewById(
                            R.id.tvBookingCode
                    );

            badgeForOthers =
                    itemView.findViewById(
                            R.id.badgeForOthers
                    );


        }

    }

}