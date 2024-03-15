package com.example.evapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.room.Room;
import java.util.List;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminViewActivity extends AppCompatActivity {
    private ListView listViewStations, listViewBookings;
    private ArrayList<String> stationsList, bookingsList;
    private ArrayAdapter<String> stationsAdapter, bookingsAdapter;
    private Button removeStationsButton;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        listViewStations = findViewById(R.id.list_view_stations);
        listViewBookings = findViewById(R.id.list_view_bookings);
        removeStationsButton = findViewById(R.id.remove_stations_button);
        stationsList = new ArrayList<>();
        bookingsList = new ArrayList<>();

        // Initialize the Room database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "UserDatabase").build();

        // Load stations from the database
        new Thread(() -> {
            List<Station> stations = db.userDao().getStations();
            for (Station station : stations) {
                stationsList.add(station.getStationName() + ": " + station.getPrice() + ", " + station.getLatitude() + ", " + station.getLongitude() + ", " + station.getSlot());
            }
            runOnUiThread(() -> {
                stationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stationsList);
                listViewStations.setAdapter(stationsAdapter);
            });
        }).start();

        // Load bookings from the database
        new Thread(() -> {
            List<Booking> bookings = db.userDao().getBookings(); // Assuming you have a method to get bookings
            for (Booking booking : bookings) {
                bookingsList.add(booking.getUserName() + ", " + booking.getCarModel() + ", " + booking.getVehicleNumber() + ", " + booking.getPhoneNumber() + ", " + booking.getTime() + ", " + booking.getDate() + ", " + booking.getSlot());
            }
            runOnUiThread(() -> {
                bookingsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookingsList);
                listViewBookings.setAdapter(bookingsAdapter);
            });
        }).start();

        removeStationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminViewActivity.this, RemoveStations.class);
                startActivity(intent);
            }
        });
    }
}

