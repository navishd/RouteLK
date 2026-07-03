package com.routelk.app.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.routelk.app.R;
import com.routelk.app.adapters.BusAdapter;
import com.routelk.app.models.Bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageBusesActivity extends AppCompatActivity {

    private EditText busNameEditText;
    private EditText busNumberEditText;
    private EditText busTypeEditText;
    private EditText totalSeatsEditText;
    private Button addBusBtn;

    private RecyclerView busRecyclerView;

    private FirebaseFirestore db;

    private List<Bus> busList;
    private BusAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_buses);

        db = FirebaseFirestore.getInstance();

        busNameEditText = findViewById(R.id.busNameEditText);
        busNumberEditText = findViewById(R.id.busNumberEditText);
        busTypeEditText = findViewById(R.id.busTypeEditText);
        totalSeatsEditText = findViewById(R.id.totalSeatsEditText);
        addBusBtn = findViewById(R.id.addBusBtn);

        busRecyclerView = findViewById(R.id.busRecyclerView);
        busRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        busList = new ArrayList<>();
        adapter = new BusAdapter(this, busList);
        busRecyclerView.setAdapter(adapter);

        loadBuses();

        addBusBtn.setOnClickListener(v -> saveBus());
    }

    private void saveBus() {

        String busName = busNameEditText.getText().toString().trim();
        String busNumber = busNumberEditText.getText().toString().trim();
        String busType = busTypeEditText.getText().toString().trim();
        String totalSeats = totalSeatsEditText.getText().toString().trim();

        if (TextUtils.isEmpty(busName)
                || TextUtils.isEmpty(busNumber)
                || TextUtils.isEmpty(busType)
                || TextUtils.isEmpty(totalSeats)) {

            Toast.makeText(this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> bus = new HashMap<>();

        bus.put("busName", busName);
        bus.put("busNumber", busNumber);
        bus.put("busType", busType);
        bus.put("totalSeats", totalSeats);

        db.collection("buses")
                .add(bus)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(
                            ManageBusesActivity.this,
                            "Bus Added Successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    busNameEditText.setText("");
                    busNumberEditText.setText("");
                    busTypeEditText.setText("");
                    totalSeatsEditText.setText("");

                    loadBuses();

                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                ManageBusesActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show());
    }

    private void loadBuses() {

        db.collection("buses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    busList.clear();

                    for (DocumentSnapshot document : queryDocumentSnapshots) {

                        Bus bus = document.toObject(Bus.class);

                        if (bus != null) {

                            bus.setId(document.getId());

                            busList.add(bus);
                        }
                    }

                    adapter.notifyDataSetChanged();

                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBuses();
    }
}