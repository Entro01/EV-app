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
import java.util.List;
import java.util.Map;


public class RemoveStations extends AppCompatActivity {
    private ListView listViewStations;
    private ArrayAdapter<Station> stationsAdapter;
    private AppDatabase db;
    private UserDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_stations);

        db = Room.databaseBuilder(this, AppDatabase.class, "UserDatabase").allowMainThreadQueries().build();
        dao = db.userDao();

        listViewStations = findViewById(R.id.list_view_stations);

        // Load stations from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("STATIONS", MODE_PRIVATE);
        List<Station> allEntries = dao.getStations();

        // Set up the adapter for the ListView
        stationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allEntries);
        listViewStations.setAdapter(stationsAdapter);

        // Set up the click listener for the ListView items
        listViewStations.setOnItemClickListener((parent, view, position, id) -> {
            Station selectedStation = allEntries.get(position);
            dao.deleteStation(selectedStation);
        });
    }
}
