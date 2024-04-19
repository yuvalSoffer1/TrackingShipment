package com.example.trackingshipment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addOrderBtn = findViewById(R.id.addOrderBtn);
        Button viewOrdersBtn = findViewById(R.id.viewOrdersBtn);
        Button viewShipmentsBtn = findViewById(R.id.viewShipmentsBtn);

        // Buttons on click listeners
        addOrderBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddOrderActivity.class);
            startActivity(intent);
        });

        viewOrdersBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewOrSearchOrdersActivity.class);
            startActivity(intent);
        });

        viewShipmentsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewOrSearchShipmentsActivity.class);
            startActivity(intent);
        });
    }

    // Help function for generating a random number that contains 10 digits
    public static String generateTenDigitNumber() {
        Random random = new Random();
        StringBuilder number = new StringBuilder();

        // Loop to generate each digit
        for (int i = 0; i < 10; i++) {
            int digit = random.nextInt(10);
            number.append(digit);
        }

        return number.toString();
    }
}
