package com.example.evapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Toast;

public class BookingActivity extends AppCompatActivity {
    private EditText userNameEditText;
    private EditText carModelEditText;
    private EditText vehicleNumberEditText;
    private EditText phoneNumberEditText;
    private EditText timeEditText;
    private EditText dateEditText;
    private Spinner slotSpinner;

    private Button bookButton;
    private String stationName; // Assume this is passed from the previous activity

    private AppDatabase db;
    private UserDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);


        db = Room.databaseBuilder(this, AppDatabase.class, "UserDatabase").allowMainThreadQueries().build();
        dao = db.userDao();


        userNameEditText = findViewById(R.id.user_name_edit_text);
        carModelEditText = findViewById(R.id.car_model_edit_text);
        vehicleNumberEditText = findViewById(R.id.vehicle_number_edit_text);
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        timeEditText = findViewById(R.id.time_edit_text);
        dateEditText = findViewById(R.id.date_edit_text);
        slotSpinner = findViewById(R.id.slot_spinner);
        bookButton = findViewById(R.id.book_button);

        // Get the station name from the intent if passed
        Intent intent = getIntent();
        if (intent.hasExtra("station_name")) {
            stationName = intent.getStringExtra("station_name");
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.chargers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        slotSpinner.setAdapter(adapter);

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString();
                String carModel = carModelEditText.getText().toString();
                String vehicleNumber = vehicleNumberEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String time = timeEditText.getText().toString();
                String date = dateEditText.getText().toString();
                String slot = slotSpinner.getSelectedItem().toString();

                // Save the booking information to SharedPreferences
                saveBookingData(userName, carModel, vehicleNumber, phoneNumber, time, date, slot);

                // Redirect to ConfirmationsActivity
                Intent confirmationsIntent = new Intent(BookingActivity.this, ConfirmationsActivity.class);
                confirmationsIntent.putExtra("station_name", stationName); // Pass the station name

                // Assuming time is entered in hours, parse it to an integer and add it as an extra
                try {
                    int hours = Integer.parseInt(time);
                    confirmationsIntent.putExtra("hours", hours); // Pass the number of hours
                } catch (NumberFormatException e) {
                    Toast.makeText(BookingActivity.this, "Please enter a valid number of hours", Toast.LENGTH_SHORT).show();
                    return; // Don't proceed if time is not a valid number
                }

                // Add the user's name, car model, and date as extras
                confirmationsIntent.putExtra("user_name", userName);
                confirmationsIntent.putExtra("car_model", carModel);
                confirmationsIntent.putExtra("date", date);

                startActivity(confirmationsIntent);
                finish(); // Close BookingActivity
            }
        });
    }

    private void saveBookingData(String userName, String carModel, String vehicleNumber, String phoneNumber, String time, String date, String slot) {
        dao.insertBooking(userName, carModel, vehicleNumber, phoneNumber, time, date, slot);
    }
}
