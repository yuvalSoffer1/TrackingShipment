package com.example.trackingshipment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewOrSearchShipmentsActivity extends AppCompatActivity {
    private ShipmentAdapter adapter;
    private List<Shipment> shipmentList;
    private List<Shipment> filteredList;
    private EditText searchShipmentEdtTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_or_search_shipments);

        shipmentList = new ArrayList<>();
        filteredList = new ArrayList<>();
        Button searchShipmentBtn = findViewById(R.id.searchShipmentBtn);
        searchShipmentEdtTxt = findViewById(R.id.searchShipmentEdtTxt);

        RecyclerView recyclerView = findViewById(R.id.shipmentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShipmentAdapter(shipmentList);
        recyclerView.setAdapter(adapter);

        DatabaseReference databaseShipments = FirebaseDatabase.getInstance().getReference("shipments");
        databaseShipments.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shipmentList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Shipment shipment = postSnapshot.getValue(Shipment.class);
                    shipmentList.add(shipment);
                }
                adapter.updateShipmentsData(shipmentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewOrSearchShipmentsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Search button on click listener
        searchShipmentBtn.setOnClickListener(v -> {
            String searchQuery = searchShipmentEdtTxt.getText().toString().trim();
            if (!searchQuery.isEmpty()) {
                filteredList.clear();
                for (Shipment shipment : shipmentList) {
                    if (!shipment.getShipmentNumber().isEmpty() && shipment.getShipmentNumber().contains(searchQuery)) {
                        filteredList.add(shipment);
                    }
                }
                adapter.updateShipmentsData(filteredList);
            } else {
                adapter.updateShipmentsData(shipmentList); // Reset to original list if search is empty
            }
        });
    }
}
