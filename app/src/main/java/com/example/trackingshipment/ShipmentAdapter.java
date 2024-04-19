package com.example.trackingshipment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShipmentAdapter extends RecyclerView.Adapter<ShipmentAdapter.ShipmentViewHolder> {
    private List<Shipment> shipmentList;


    public ShipmentAdapter(List<Shipment> shipmentList) {
        this.shipmentList = new ArrayList<>(shipmentList);
    }

    @NonNull
    @Override
    public ShipmentAdapter.ShipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipment_item, parent, false);
        return new ShipmentAdapter.ShipmentViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ShipmentAdapter.ShipmentViewHolder holder, int position) {
        Shipment shipment = shipmentList.get(position);

        // Displaying other details
        holder.orderNumberTextView.setText("Order Number: " + shipment.getOrderNumber());
        holder.shipmentNumberTextView.setText("Shipment Number: " + shipment.getShipmentNumber());
        holder.shipmentDateTextView.setText("Shipment Date: " + shipment.getShipmentDate());
        holder.shipmentTimeTextView.setText("Shipment Time: " + shipment.getShipmentTime());
        holder.shipmentStatusTextView.setText("Status: " + shipment.getShipmentStatus());
    }

    // Getter
    @Override
    public int getItemCount() {
        return shipmentList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateShipmentsData(List<Shipment> newShipments) {
        this.shipmentList.clear();
        this.shipmentList.addAll(newShipments);
        notifyDataSetChanged();
    }

    // Shipment View holder inner class
    protected class ShipmentViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumberTextView, shipmentNumberTextView, shipmentDateTextView,
                shipmentTimeTextView, shipmentStatusTextView;
        Button updateButton, deleteButton;

        // Constructor
        public ShipmentViewHolder(View itemView) {
            super(itemView);
            orderNumberTextView = itemView.findViewById(R.id.orderNumberTextView);
            shipmentNumberTextView = itemView.findViewById(R.id.shipmentNumberTextView);
            shipmentDateTextView = itemView.findViewById(R.id.shipmentDateTextView);
            shipmentTimeTextView = itemView.findViewById(R.id.shipmentTimeTextView);
            shipmentStatusTextView = itemView.findViewById(R.id.shipmentStatusTextView);

            updateButton = itemView.findViewById(R.id.updateShipmentBtn);
            deleteButton = itemView.findViewById(R.id.deleteShipmentBtn);

            updateButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Shipment shipment = shipmentList.get(position);
                    updateShipment(shipment);
                }
            });

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Shipment shipment = shipmentList.get(position);
                    deleteShipment(shipment, position);
                }
            });
        }

        // Update an shipment
        private void updateShipment(Shipment shipment) {
            Context context = itemView.getContext();
            Intent intent = new Intent(context, AddShipmentActivity.class);
            intent.putExtra("shipment_number", shipment.getShipmentNumber());
            intent.putExtra("order_number", shipment.getOrderNumber());
            intent.putExtra("shipment_date", shipment.getShipmentDate());
            intent.putExtra("shipment_time", shipment.getShipmentTime());
            intent.putExtra("shipment_status", shipment.getShipmentStatus());
            context.startActivity(intent);
        }

        // Delete an shipment
        private void deleteShipment(Shipment shipment, int position) {
            // Validate position and shipment match
            if (position < 0 || position >= shipmentList.size() || !shipmentList.get(position).getShipmentNumber().equals(shipment.getShipmentNumber())) {
                Toast.makeText(itemView.getContext(), "Shipment deletion failed: Invalid location or mismatch to shipment", Toast.LENGTH_SHORT).show();
                return;
            }

            // Remove the shipment from the shipment list
            shipmentList.remove(shipment);

            DatabaseReference dbShipment = FirebaseDatabase.getInstance().getReference("shipments").child(shipment.getShipmentNumber());
            dbShipment.removeValue().addOnSuccessListener(aVoid -> Toast.makeText(itemView.getContext(), "Shipment deleted successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(itemView.getContext(), "Failed to delete the shipment from Firebase", Toast.LENGTH_SHORT).show());
        }
    }
}
