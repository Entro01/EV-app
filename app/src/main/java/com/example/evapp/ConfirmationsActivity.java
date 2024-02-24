package com.example.evapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.content.SharedPreferences;

public class ConfirmationsActivity extends AppCompatActivity {
    private TextView priceTextView;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmations);

        priceTextView = findViewById(R.id.price_text_view);
        confirmButton = findViewById(R.id.confirm_button);

        // Assuming the station name is passed as an extra from the previous activity
        String stationName = getIntent().getStringExtra("station_name");

        // Fetch the station details from Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences("STATIONS", MODE_PRIVATE);
        String stationDetails = sharedPreferences.getString(stationName, "");

        // Parse the station details to extract the price
        String[] details = stationDetails.split(",");
        String priceString = details[3];
        double price = Double.parseDouble(priceString);

        // Calculate the final price with a  15% tax
        double finalPrice = price + (price *  0.15);

        // Display the final price
        priceTextView.setText("Final Price: $" + String.format("%.2f", finalPrice));

        // Set up the confirm button click listener
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to UserViewActivity
                Intent userViewIntent = new Intent(ConfirmationsActivity.this, UserViewActivity.class);
                startActivity(userViewIntent);
                finish(); // Close ConfirmationsActivity
            }
        });
    }
}
