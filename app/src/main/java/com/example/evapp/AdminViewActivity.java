package com.example.evapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class AdminViewActivity extends AppCompatActivity {
    private ListView listViewStations, listViewBookings;
    private ArrayList<String> stationsList, bookingsList;
    private ArrayAdapter<String> stationsAdapter, bookingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        listViewStations = findViewById(R.id.list_view_stations);
        listViewBookings = findViewById(R.id.list_view_bookings);
        stationsList = new ArrayList<>();
        bookingsList = new ArrayList<>();

        // Load stations from shared preferences
        SharedPreferences stationsPref = getSharedPreferences("STATIONS", MODE_PRIVATE);
        Map<String, ?> allStations = stationsPref.getAll();
        for (Map.Entry<String, ?> entry : allStations.entrySet()) {
            stationsList.add(entry.getKey() + ": " + entry.getValue().toString());
        }

        // Load bookings from shared preferences
        SharedPreferences bookingsPref = getSharedPreferences("BOOKINGS", MODE_PRIVATE);
        Map<String, ?> allBookings = bookingsPref.getAll();
        for (Map.Entry<String, ?> entry : allBookings.entrySet()) {
            bookingsList.add(entry.getKey() + ": " + entry.getValue().toString());
        }

        // Set up the adapters for the ListViews
        stationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stationsList);
        listViewStations.setAdapter(stationsAdapter);

        bookingsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookingsList);
        listViewBookings.setAdapter(bookingsAdapter);
    }
}

