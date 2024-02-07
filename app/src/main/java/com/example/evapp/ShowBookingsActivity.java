package com.example.evapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import java.util.ArrayList;
import java.util.Map;
import android.widget.ListView;



public class ShowBookingsActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> bookingsList;
    private ArrayAdapter<String> bookingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bookings);

        listView = findViewById(R.id.list_view);
        bookingsList = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("BOOKINGS", MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String bookingDetails = entry.getKey() + ": " + entry.getValue().toString();
            bookingsList.add(bookingDetails);
        }

        // Use a custom adapter for the ListView
        bookingsAdapter = new ArrayAdapter<String>(this, R.layout.list_item_booking, R.id.station_name_text_view, bookingsList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_item_booking, parent, false);
                }

                TextView stationNameTextView = convertView.findViewById(R.id.station_name_text_view);
                TextView locationTextView = convertView.findViewById(R.id.location_text_view);
                TextView timeSlotTextView = convertView.findViewById(R.id.time_slot_text_view);

                String bookingDetail = getItem(position);
                String[] bookingInfo = bookingDetail.split(": ");
                String stationName = bookingInfo[0];
                String[] stationInfo = bookingInfo[1].split(",");

                stationNameTextView.setText(stationName);
                locationTextView.setText(stationInfo[0]);
                timeSlotTextView.setText(stationInfo[1]);

                return convertView;
            }
        };
        listView.setAdapter(bookingsAdapter);
    }

}
