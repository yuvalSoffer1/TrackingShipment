package com.example.trackingshipment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddOrderActivity extends AppCompatActivity {
    private DatabaseReference databaseOrders;

    private String orderNumber;
    private String shipmentNumber;
    private EditText orderDateEdtTxt;
    private EditText itemNumberEdtTxt;
    private EditText itemDescriptionEdtTxt;
    private EditText originCountryEdtTxt;
    private EditText departureDateEdtTxt;
    private EditText destinationCountryEdtTxt;
    private EditText estimatedArrivalDateEdtTxt;
    private EditText deliveryDateEdtTxt;
    private Spinner orderStatusSpinner;
    private String orderStatus = "Select";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        orderStatusSpinner = findViewById(R.id.orderStatusSpinner);
        loadOrderStatusSpinner();
        initFields();


        // Change selected item listener
        orderStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOrderStatus = (String) parent.getItemAtPosition(position);
                if (!selectedOrderStatus.equals("Select")) {
                    orderStatus = selectedOrderStatus;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initFields() {
        orderDateEdtTxt = findViewById(R.id.orderDateEdtTxt);
        itemNumberEdtTxt = findViewById(R.id.itemNumberEdtTxt);
        itemDescriptionEdtTxt = findViewById(R.id.itemDescriptionEdtTxt);
        originCountryEdtTxt = findViewById(R.id.originCountryEdtTxt);
        departureDateEdtTxt = findViewById(R.id.departureDateEdtTxt);
        destinationCountryEdtTxt = findViewById(R.id.destinationCountryEdtTxt);
        estimatedArrivalDateEdtTxt = findViewById(R.id.estimatedArrivalDateEdtTxt);
        deliveryDateEdtTxt = findViewById(R.id.deliveryDateEdtTxt);

        // Database Fields
        databaseOrders = FirebaseDatabase.getInstance().getReference("orders");

        orderDateEdtTxt.setOnClickListener(v -> showDateDialog(orderDateEdtTxt));
        departureDateEdtTxt.setOnClickListener(v -> showDateDialog(departureDateEdtTxt));
        estimatedArrivalDateEdtTxt.setOnClickListener(v -> showDateDialog(estimatedArrivalDateEdtTxt));
        deliveryDateEdtTxt.setOnClickListener(v -> showDateDialog(deliveryDateEdtTxt));

        // Check if extras are provided
        if (getIntent().hasExtra("order_date")) {
            orderDateEdtTxt.setText(getIntent().getStringExtra("order_date"));
            itemNumberEdtTxt.setText(getIntent().getStringExtra("item_number"));
            itemDescriptionEdtTxt.setText(getIntent().getStringExtra("item_description"));
            originCountryEdtTxt.setText(getIntent().getStringExtra("origin_country"));
            departureDateEdtTxt.setText(getIntent().getStringExtra("departure_date"));
            destinationCountryEdtTxt.setText(getIntent().getStringExtra("destination_country"));
            estimatedArrivalDateEdtTxt.setText(getIntent().getStringExtra("estimated_arrival_date"));
            deliveryDateEdtTxt.setText(getIntent().getStringExtra("delivery_date"));

            orderNumber = getIntent().getStringExtra("order_number");
            shipmentNumber = getIntent().getStringExtra("shipment_number");

            String orderStatus = getIntent().getStringExtra("order_status");
            if (orderStatus != null && orderStatusSpinner.getAdapter() != null) {
                int spinnerPosition = ((ArrayAdapter<String>) orderStatusSpinner.getAdapter()).getPosition(orderStatus);
                // Check if the status exists in the adapter
                if (spinnerPosition >= 0) {
                    orderStatusSpinner.setSelection(spinnerPosition);
                }
            }
        }
    }

    // Save button on click listener
    public void saveOrderClicked(View view) {
        // Checks validations for all fields
        String orderDate = orderDateEdtTxt.getText().toString();
        if (orderDate.isEmpty()) {
            Toast.makeText(this, "Enter an Order Date!", Toast.LENGTH_LONG).show();
            return;
        }
        String itemNumber = itemNumberEdtTxt.getText().toString();
        if (itemNumber.isEmpty()) {
            Toast.makeText(this, "Enter an Item Number!", Toast.LENGTH_LONG).show();
            return;
        }
        String itemDescription = itemDescriptionEdtTxt.getText().toString();
        if (itemDescription.isEmpty()) {
            Toast.makeText(this, "Enter an Item Description!", Toast.LENGTH_LONG).show();
            return;
        }
        String originCountry = originCountryEdtTxt.getText().toString();
        if (originCountry.isEmpty()) {
            Toast.makeText(this, "Enter an Origin Country", Toast.LENGTH_LONG).show();
            return;
        }
        String departureDate = departureDateEdtTxt.getText().toString();
        if (departureDate.isEmpty()) {
            Toast.makeText(this, "Enter an Departure Date!", Toast.LENGTH_LONG).show();
            return;
        }
        String destinationCountry = destinationCountryEdtTxt.getText().toString();
        if (destinationCountry.isEmpty()) {
            Toast.makeText(this, "Enter an Destination Country!", Toast.LENGTH_LONG).show();
            return;
        }
        String estimatedArrivalDate = estimatedArrivalDateEdtTxt.getText().toString();
        if (estimatedArrivalDate.isEmpty()) {
            Toast.makeText(this, "Enter an Estimated Arrival Date!", Toast.LENGTH_LONG).show();
            return;
        }
        String deliveryDate = deliveryDateEdtTxt.getText().toString();
        if (deliveryDate.isEmpty()) {
            Toast.makeText(this, "Enter an Delivery Date!", Toast.LENGTH_LONG).show();
            return;
        }

        if (orderStatus.equals("Select")) {
            Toast.makeText(this, "Select an Order Status!", Toast.LENGTH_LONG).show();
            return;
        }

        // Creates new order
        Order order = new Order(orderDate, itemNumber, itemDescription, originCountry,
                departureDate, destinationCountry, estimatedArrivalDate, deliveryDate,
                orderStatus);

        // Checks If the order is in update mode, Otherwise continue to make a shipment
        if (getIntent().hasExtra("order_date")) {
            order.setOrderNumber(orderNumber);
            order.setShipmentNumber(shipmentNumber);
            addOrderToDb(order);
            Toast.makeText(this, "Order Updated!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Creates a shipment for the specific order
        Intent intent = new Intent(AddOrderActivity.this, AddShipmentActivity.class);
        intent.putExtra("order_number", order.getOrderNumber());
        intent.putExtra("shipment_number", order.getShipmentNumber());
        startActivity(intent);

        addOrderToDb(order);
    }

    // Display the date dialog to edit text
    public void showDateDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year1, month1, day) -> {
            @SuppressLint("DefaultLocale") String selectedDate = String.format("%d/%02d/%02d", day, month1 + 1, year1);
            editText.setText(selectedDate);
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    // A function that adds an order to the database
    private void addOrderToDb(Order order) {
        String successMessage = "Order Successfully Saved\n" + "Order Number: " + order.getOrderNumber();
        String failMessage = "Failed to add order.";
        databaseOrders.child(order.getOrderNumber()).setValue(order)
                .addOnSuccessListener(aVoid -> {
                    if (!getIntent().hasExtra("order_date")) {
                        Toast.makeText(this, successMessage, Toast.LENGTH_LONG).show();
                    }
                    // Close this activity and return to the previous one
                    finish();
                }).addOnFailureListener(e -> Toast.makeText(this, failMessage, Toast.LENGTH_SHORT).show());
    }


    // Initialization of the order status spinner
    private void loadOrderStatusSpinner() {
        String[] orderStatusValues = new String[]{"Select", "Opened", "Ready To Ship", "Sent",
                "Reached The Destination", "Received"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, orderStatusValues);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        orderStatusSpinner.setAdapter(arrayAdapter);
    }
}
