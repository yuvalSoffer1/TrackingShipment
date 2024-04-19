package com.example.trackingshipment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private final List<Order> ordersList;

    public OrderAdapter(List<Order> ordersList) {
        this.ordersList = new ArrayList<>(ordersList);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = ordersList.get(position);

        // Displaying other details
        holder.orderNumberTextView.setText("Order Number: " + order.getOrderNumber());
        holder.itemNumberTextView.setText("Item Number: " + order.getItemNumber());
        holder.itemDescriptionTextView.setText("Item: " + order.getItemDescription());
        holder.originCountryTextView.setText("Origin: " + order.getOriginCountry());
        holder.destinationCountryTextView.setText("Destination: " + order.getDestinationCountry());
        holder.orderStatusTextView.setText("Status: " + order.getOrderStatus());
        holder.shipmentNumberTextView.setText("Shipment Number: " + order.getShipmentNumber());


        // Date details
        if (order.getOrderDate() != null) {
            holder.orderDateTextView.setText("Order Date: " + order.getOrderDate());
        } else {
            holder.orderDateTextView.setText("Order Date: N/A");
        }

        if (order.getDepartureDate() != null) {
            holder.departureDateTextView.setText("Departure: " + order.getDepartureDate());
        } else {
            holder.departureDateTextView.setText("Departure: N/A");
        }

        if (order.getEstimatedArrivalDate() != null) {
            holder.estimatedArrivalDateTextView.setText("Estimated Arrival: " + order.getEstimatedArrivalDate());
        } else {
            holder.estimatedArrivalDateTextView.setText("Estimated Arrival: N/A");
        }

        if (order.getDeliveryDate() != null) {
            holder.deliveryDateTextView.setText("Delivery Date: " + order.getDeliveryDate());
        } else {
            holder.deliveryDateTextView.setText("Delivery Date: N/A");
        }
    }

    // Getter
    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateOrdersData(List<Order> newOrders) {
        this.ordersList.clear();
        this.ordersList.addAll(newOrders);
        notifyDataSetChanged();
    }

    // Order View holder inner class
    protected class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumberTextView, itemDescriptionTextView, orderStatusTextView, orderDateTextView,
                originCountryTextView, departureDateTextView, destinationCountryTextView,
                estimatedArrivalDateTextView, deliveryDateTextView, shipmentNumberTextView,
                itemNumberTextView;

        Button updateButton, deleteButton;

        // Constructor
        public OrderViewHolder(View itemView) {
            super(itemView);
            orderNumberTextView = itemView.findViewById(R.id.orderNumberTextView);
            itemDescriptionTextView = itemView.findViewById(R.id.itemDescriptionTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            originCountryTextView = itemView.findViewById(R.id.originCountryTextView);
            departureDateTextView = itemView.findViewById(R.id.departureDateTextView);
            destinationCountryTextView = itemView.findViewById(R.id.destinationCountryTextView);
            estimatedArrivalDateTextView = itemView.findViewById(R.id.estimatedArrivalDateTextView);
            deliveryDateTextView = itemView.findViewById(R.id.deliveryDateTextView);
            shipmentNumberTextView = itemView.findViewById(R.id.shipmentNumberTextView);
            itemNumberTextView = itemView.findViewById(R.id.itemNumberTextView);

            updateButton = itemView.findViewById(R.id.updateOrderBtn);
            deleteButton = itemView.findViewById(R.id.deleteOrderBtn);

            updateButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Order order = ordersList.get(position);
                    updateOrder(order);
                }
            });

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Order order = ordersList.get(position);
                    deleteOrder(order, position);
                }
            });
        }

        // Update an order
        private void updateOrder(Order order) {
            Context context = itemView.getContext();
            Intent intent = new Intent(context, AddOrderActivity.class);
            intent.putExtra("order_number", order.getOrderNumber());
            intent.putExtra("shipment_number", order.getShipmentNumber());
            intent.putExtra("order_date", order.getOrderDate());
            intent.putExtra("item_number", order.getItemNumber());
            intent.putExtra("item_description", order.getItemDescription());
            intent.putExtra("origin_country", order.getOriginCountry());
            intent.putExtra("departure_date", order.getDepartureDate());
            intent.putExtra("destination_country", order.getDestinationCountry());
            intent.putExtra("estimated_arrival_date", order.getEstimatedArrivalDate());
            intent.putExtra("delivery_date", order.getDeliveryDate());
            intent.putExtra("order_status", order.getOrderStatus());
            context.startActivity(intent);
        }

        // Delete an order
        private void deleteOrder(Order order, int position) {
            // Validate position and order match
            if (position < 0 || position >= ordersList.size() || !ordersList.get(position).getOrderNumber().equals(order.getOrderNumber())) {
                Toast.makeText(itemView.getContext(), "Order deletion failed: Invalid location or mismatch to order", Toast.LENGTH_SHORT).show();
                return;
            }

            // Remove the order from the order list
            ordersList.remove(order);

            DatabaseReference dbOrder = FirebaseDatabase.getInstance().getReference("orders").child(order.getOrderNumber());
            dbOrder.removeValue().addOnSuccessListener(aVoid -> Toast.makeText(itemView.getContext(), "Order deleted successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(itemView.getContext(), "Failed to delete the order from Firebase", Toast.LENGTH_SHORT).show());
        }
    }
}
