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

public class ViewOrSearchOrdersActivity extends AppCompatActivity {
    private OrderAdapter adapter;
    private List<Order> ordersList;
    private List<Order> filteredList;
    private EditText searchOrderEdtTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_or_search_orders);

        ordersList = new ArrayList<>();
        filteredList = new ArrayList<>();
        Button searchOrderBtn = findViewById(R.id.searchOrderBtn);
        searchOrderEdtTxt = findViewById(R.id.searchOrderEdtTxt);

        RecyclerView recyclerView = findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter(ordersList);
        recyclerView.setAdapter(adapter);

        DatabaseReference databaseOrders = FirebaseDatabase.getInstance().getReference("orders");
        databaseOrders.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Order order = postSnapshot.getValue(Order.class);
                    ordersList.add(order);
                }
                // Update the adapter data
                adapter.updateOrdersData(ordersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewOrSearchOrdersActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        // Search button on click listener
        searchOrderBtn.setOnClickListener(v -> {
            String searchQuery = searchOrderEdtTxt.getText().toString().trim();
            if (!searchQuery.isEmpty()) {
                filteredList.clear();
                for (Order order : ordersList) {
                    if (!order.getOrderNumber().isEmpty() && order.getOrderNumber().contains(searchQuery)) {
                        filteredList.add(order);
                    }
                }
                adapter.updateOrdersData(filteredList);
            } else {
                adapter.updateOrdersData(ordersList); // Reset to original list if search is empty
            }
        });
    }
}
