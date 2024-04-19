package com.example.trackingshipment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddShipmentActivity extends AppCompatActivity {
    private DatabaseReference databaseShipments;
    private EditText shipmentDateEdtTxt;
    private EditText shipmentTimeEdtTxt;
    private Spinner shipmentStatusSpinner;
    private String orderNumber, shipmentNumber;
    private String shipmentStatus = "Select";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipment);

        orderNumber = getIntent().getStringExtra("order_number");
        shipmentNumber = getIntent().getStringExtra("shipment_number");

        shipmentStatusSpinner = findViewById(R.id.shipmentStatusSpinner);
        loadShipmentStatusSpinner();
        initFields();

        // Change selected item listener
        shipmentStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedShipmentStatus = (String) parent.getItemAtPosition(position);
                if (!selectedShipmentStatus.equals("Select")) {
                    shipmentStatus = selectedShipmentStatus;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Toast.makeText(AddShipmentActivity.this,
                        "Please finish processing the shipment", Toast.LENGTH_LONG).show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void initFields() {
        databaseShipments = FirebaseDatabase.getInstance().getReference("shipments");

        TextView orderNumberTextView = findViewById(R.id.orderNumberTextView);
        TextView shipmentNumberTextView = findViewById(R.id.shipmentNumberTextView);
        shipmentDateEdtTxt = findViewById(R.id.shipmentDateEdtTxt);
        shipmentTimeEdtTxt = findViewById(R.id.shipmentTimeEdtTxt);

        shipmentDateEdtTxt.setOnClickListener(v -> showDateDialog(shipmentDateEdtTxt));

        // Check if extras are provided
        if (getIntent().hasExtra("shipment_date")) {
            orderNumber = getIntent().getStringExtra("order_number");
            shipmentNumber = getIntent().getStringExtra("shipment_number");
            shipmentDateEdtTxt.setText(getIntent().getStringExtra("shipment_date"));
            shipmentTimeEdtTxt.setText(getIntent().getStringExtra("shipment_time"));

            String shipmentStatus = getIntent().getStringExtra("shipment_status");
            if (shipmentStatus != null && shipmentStatusSpinner.getAdapter() != null) {
                int spinnerPosition = ((ArrayAdapter<String>) shipmentStatusSpinner.getAdapter()).getPosition(shipmentStatus);
                // Check if the status exists in the adapter
                if (spinnerPosition >= 0) {
                    shipmentStatusSpinner.setSelection(spinnerPosition);
                }
            }
        }

        orderNumberTextView.setText("Order Number: " + orderNumber);
        shipmentNumberTextView.setText("Shipment Number: " + shipmentNumber);
    }

    // Display the date dialog to edit text
    private void showDateDialog(EditText editText) {
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

    // Save button on click listener
    public void saveShipmentClicked(View view) {
        // Checks validations for all fields
        String shipmentDate = shipmentDateEdtTxt.getText().toString();
        if (shipmentDate.isEmpty()) {
            Toast.makeText(this, "Enter an Shipment Date!", Toast.LENGTH_LONG).show();
            return;
        }
        String shipmentTime = shipmentTimeEdtTxt.getText().toString();
        if (shipmentTime.isEmpty()) {
            Toast.makeText(this, "Enter an Shipment Time!", Toast.LENGTH_LONG).show();
            return;
        }

        if (shipmentStatus.equals("Select")) {
            Toast.makeText(this, "Select an Shipment Status!", Toast.LENGTH_LONG).show();
            return;
        }


        Shipment shipment = new Shipment(orderNumber, shipmentNumber, shipmentDate, shipmentTime, shipmentStatus);

        // Checks If the shipment is in update mode
        if (getIntent().hasExtra("shipment_date")) {
            shipment.setOrderNumber(orderNumber);
            shipment.setShipmentNumber(shipmentNumber);
            addShipmentToDb(shipment);
            Toast.makeText(this, "Shipment Updated!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        addShipmentToDb(shipment);
    }

    // A function that adds an shipment to the database
    private void addShipmentToDb(Shipment shipment) {
        String successMessage = "Shipment Successfully Saved\n" + "Order Number: " + shipment.getShipmentNumber();
        String failMessage = "Failed to add order.";
        databaseShipments.child(shipment.getShipmentNumber()).setValue(shipment)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, successMessage, Toast.LENGTH_LONG).show();
                    // Close this activity and return to the previous one
                    finish();
                }).addOnFailureListener(e -> Toast.makeText(this, failMessage, Toast.LENGTH_SHORT).show());
    }

    // Initialization of the shipment status spinner
    private void loadShipmentStatusSpinner() {
        String[] shipmentStatusValues = new String[]{"Select", "Shipping with the local shipping company",
                "Left the country of origin", "Received by the airline",
                "Found in the warehouse of the country of origin",
                "Arrived at the post office of the destination country"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, shipmentStatusValues);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        shipmentStatusSpinner.setAdapter(arrayAdapter);
    }
}
