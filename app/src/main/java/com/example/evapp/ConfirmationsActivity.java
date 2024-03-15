package com.example.evapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import androidx.room.Room;
import java.util.List;

public class ConfirmationsActivity extends AppCompatActivity {
    private TextView priceTextView;
    private TextView userNameTextView;
    private TextView carModelTextView;
    private TextView hoursTextView;
    private TextView dateTextView;
    private TextView ratePerHourTextView;
    private TextView taxTextView;
    private Button confirmButton;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmations);

        // Initialize the Room database and DAO
        db = Room.databaseBuilder(this, AppDatabase.class, "UserDatabase").allowMainThreadQueries().build();

        // Fetch the TextViews
        priceTextView = findViewById(R.id.price_text_view);
        userNameTextView = findViewById(R.id.user_name_text_view);
        carModelTextView = findViewById(R.id.car_model_text_view);
        hoursTextView = findViewById(R.id.hours_text_view);
        dateTextView = findViewById(R.id.date_text_view);
        ratePerHourTextView = findViewById(R.id.rate_per_hour_text_view);
        taxTextView = findViewById(R.id.tax_text_view);

        // Retrieve the extras passed from BookingActivity
        Intent intent = getIntent();
        String userName = intent.getStringExtra("user_name");
        String carModel = intent.getStringExtra("car_model");
        int hours = intent.getIntExtra("hours", 0); // Default to 0 if not provided
        String date = intent.getStringExtra("date");
        String stationName = intent.getStringExtra("station_name");

        // Display the passed information
        userNameTextView.setText("User Name: " + userName);
        carModelTextView.setText("Car Model: " + carModel);
        hoursTextView.setText("Hours: " + hours);
        dateTextView.setText("Date: " + date);

        // Fetch all stations from the database
        List<Station> stations = db.userDao().getStations();
        Station station = null;
        for (Station s : stations) {
            if (s.getStationName().equals(stationName)) {
                station = s;
                break;
            }
        }

        if (station != null) {
            // Assuming the station object has a method to get the rate per hour
            double ratePerHour = Double.parseDouble(station.getPrice());
            ratePerHourTextView.setText("Rate per hour: " + ratePerHour);

            taxTextView.setText("Tax: " + "15%");

            // Calculate the final price with a 15% tax
            double finalPrice = (ratePerHour + (ratePerHour * 0.15)) * hours;

            // Display the rate per hour and the final price
            priceTextView.setText("Final Price: $" + String.format("%.2f", finalPrice));
        } else {
            // Handle the case where the station is not found
            Log.e("ConfirmationsActivity", "Station not found: " + stationName);
            // Optionally, display an error message to the user
        }

        // Set up the confirm button click listener
        confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to UserViewActivity
                Intent userViewIntent = new Intent(ConfirmationsActivity.this, UserViewActivity.class);
                intent.putExtra("confirmation_message", "Confirmation successful " + userName);
                startActivity(userViewIntent);
                finish(); // Close ConfirmationsActivity
            }
        });
    }
}