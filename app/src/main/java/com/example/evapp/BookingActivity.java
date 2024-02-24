package com.example.evapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BookingActivity extends AppCompatActivity {
    private EditText userNameEditText;
    private EditText carModelEditText;
    private EditText vehicleNumberEditText;
    private EditText phoneNumberEditText;
    private Button bookButton;
    private String stationName; // Assume this is passed from the previous activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        userNameEditText = findViewById(R.id.user_name_edit_text);
        carModelEditText = findViewById(R.id.car_model_edit_text);
        vehicleNumberEditText = findViewById(R.id.vehicle_number_edit_text);
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        bookButton = findViewById(R.id.book_button);

        // Get the station name from the intent if passed
        Intent intent = getIntent();
        if (intent.hasExtra("station_name")) {
            stationName = intent.getStringExtra("station_name");
        }

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString();
                String carModel = carModelEditText.getText().toString();
                String vehicleNumber = vehicleNumberEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();

                // Save the booking information to SharedPreferences
                saveBookingData(userName, carModel, vehicleNumber, phoneNumber);

                // Redirect to ConfirmationsActivity
                Intent confirmationsIntent = new Intent(BookingActivity.this, ConfirmationsActivity.class);
                confirmationsIntent.putExtra("station_name", stationName); // Pass the station name
                startActivity(confirmationsIntent);
                finish(); // Close BookingActivity
            }
        });
    }

    private void saveBookingData(String userName, String carModel, String vehicleNumber, String phoneNumber) {
        SharedPreferences sharedPreferences = getSharedPreferences("BOOKINGS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save the booking data with the station name as the key
        editor.putString(stationName, userName + "," + carModel + "," + vehicleNumber + "," + phoneNumber);
        editor.apply();
    }
}
