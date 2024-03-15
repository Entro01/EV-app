package com.example.evapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Map;


public class RemoveStations extends AppCompatActivity {
    private ListView listViewStations;
    private ArrayList<String> stationsList;
    private ArrayAdapter<String> stationsAdapter;
    private AppDatabase db;
    private UserDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_stations);

        db = Room.databaseBuilder(this, AppDatabase.class, "UserDatabase").allowMainThreadQueries().build();
        dao = db.userDao();

        listViewStations = findViewById(R.id.list_view_stations);
        stationsList = new ArrayList<>();

        // Load stations from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("STATIONS", MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            stationsList.add(entry.getKey()); // Assuming the station name is the key
        }

        // Set up the adapter for the ListView
        stationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stationsList);
        listViewStations.setAdapter(stationsAdapter);

        // Set up the click listener for the ListView items
        listViewStations.setOnItemClickListener((parent, view, position, id) -> {
            String selectedStation = stationsList.get(position);
            dao.deleteStation(selectedStation);
        });
    }
}
