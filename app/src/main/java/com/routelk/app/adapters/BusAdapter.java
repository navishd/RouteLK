package com.routelk.app.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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


    private final Context context;
    private final List<Bus> busList;

    private String from;
    private String to;
    private String date;
    private String time;


    public BusAdapter(Context context, List<Bus> busList) {

        this.context = context;
        this.busList = busList;

    }


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

    }


    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_bus, parent, false);

        return new BusViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(
            @NonNull BusViewHolder holder,
            int position) {

        Bus bus = busList.get(position);


        holder.tvBusName.setText(
                bus.getBusName()
        );


        holder.tvBusNumber.setText(
                "Number : " + bus.getBusNumber()
        );


        holder.tvBusType.setText(
                "Type : " + bus.getBusType()
        );


        holder.tvSeats.setText(
                "Seats : " + bus.getSeatCount()
        );



        // Passenger flow
        holder.itemView.setOnClickListener(v -> {


            Intent intent =
                    new Intent(
                            context,
                            com.routelk.app.activities.BusDetails.class
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
                    "FROM",
                    from
            );


            intent.putExtra(
                    "TO",
                    to
            );


            intent.putExtra(
                    "DATE",
                    date
            );


            intent.putExtra(
                    "TIME",
                    time
            );


            context.startActivity(intent);


        });



        // ================= EDIT =================


        holder.editBtn.setOnClickListener(v -> {


            View dialogView =
                    LayoutInflater.from(context)
                            .inflate(
                                    R.layout.dialog_edit_bus,
                                    null
                            );

            EditText etName =
                    dialogView.findViewById(
                            R.id.etBusName
                    );


            EditText etNumber =
                    dialogView.findViewById(
                            R.id.etBusNumber
                    );


            EditText etType =
                    dialogView.findViewById(
                            R.id.etBusType
                    );


            EditText etSeats =
                    dialogView.findViewById(
                            R.id.etSeats
                    );



            etName.setText(
                    bus.getBusName()
            );


            etNumber.setText(
                    bus.getBusNumber()
            );


            etType.setText(
                    bus.getBusType()
            );


            etSeats.setText(
                    String.valueOf(
                            bus.getSeatCount()
                    )
            );



            new AlertDialog.Builder(context)

                    .setTitle("Update Bus")

                    .setView(dialogView)

                    .setPositiveButton(
                            "Update",
                            (dialog, which) -> {


                                Map<String,Object> update =
                                        new HashMap<>();


                                update.put(
                                        "busName",
                                        etName.getText()
                                                .toString()
                                                .trim()
                                );


                                update.put(
                                        "busNumber",
                                        etNumber.getText()
                                                .toString()
                                                .trim()
                                );


                                update.put(
                                        "busType",
                                        etType.getText()
                                                .toString()
                                                .trim()
                                );


                                update.put(
                                        "seatCount",
                                        Integer.parseInt(
                                                etSeats.getText()
                                                        .toString()
                                                        .trim()
                                        )
                                );



                                FirebaseFirestore
                                        .getInstance()
                                        .collection("buses")
                                        .document(
                                                bus.getBusID()
                                        )
                                        .update(update)

                                        .addOnSuccessListener(unused -> {


                                            bus.setBusName(
                                                    etName.getText()
                                                            .toString()
                                            );


                                            bus.setBusNumber(
                                                    etNumber.getText()
                                                            .toString()
                                            );


                                            bus.setBusType(
                                                    etType.getText()
                                                            .toString()
                                            );


                                            bus.setSeatCount(
                                                    Integer.parseInt(
                                                            etSeats.getText()
                                                                    .toString()
                                                    )
                                            );


                                            notifyItemChanged(
                                                    holder.getBindingAdapterPosition()
                                            );


                                        });


                            })

                    .setNegativeButton(
                            "Cancel",
                            null
                    )

                    .show();


        });



        // ================= DELETE =================


        holder.deleteBtn.setOnClickListener(v -> {


            new AlertDialog.Builder(context)

                    .setTitle("Delete Bus")

                    .setMessage(
                            "Are you sure you want to delete this bus?"
                    )

                    .setPositiveButton(
                            "Delete",
                            (dialog, which) -> {


                                FirebaseFirestore
                                        .getInstance()
                                        .collection("buses")
                                        .document(
                                                bus.getBusID()
                                        )
                                        .delete()

                                        .addOnSuccessListener(unused -> {


                                            int pos =
                                                    holder.getBindingAdapterPosition();


                                            busList.remove(pos);


                                            notifyItemRemoved(pos);


                                        });


                            })

                    .setNegativeButton(
                            "Cancel",
                            null
                    )

                    .show();


        });

        holder.tvBusID.setText("ID : " + bus.getBusID());


    }



    @Override
    public int getItemCount() {
        return busList.size();
    }



    public static class BusViewHolder
            extends RecyclerView.ViewHolder {


        TextView tvBusName;
        TextView tvBusNumber;
        TextView tvBusType;
        TextView tvSeats;
        TextView tvBusID;


        Button editBtn;
        Button deleteBtn;



        public BusViewHolder(
                @NonNull View itemView) {

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


            editBtn =
                    itemView.findViewById(
                            R.id.editBtn
                    );


            deleteBtn =
                    itemView.findViewById(
                            R.id.deleteBtn
                    );

            tvBusID = itemView.findViewById(R.id.tvBusID);

        }

    }

}