package com.example.evapp;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Map;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



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

        // Inflate the header layout
        View header = getLayoutInflater().inflate(R.layout.header, null);
        // Add the header to the ListView
        listView.addHeaderView(header);

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
                TextView mobileNumberTextView = convertView.findViewById(R.id.mobile_number_text_view); // Assuming you have this TextView in your list_item_booking layout
                TextView dateTextView = convertView.findViewById(R.id.date_text_view); // Assuming you have this TextView in your list_item_booking layout

                String bookingDetail = getItem(position);
                String[] bookingInfo = bookingDetail.split(": ");
                // Check if bookingInfo has at least two elements
                if (bookingInfo.length >= 2) {
                    String stationName = bookingInfo[0];
                    String[] stationInfo = bookingInfo[1].split(",");
                    // Check if stationInfo has at least two elements
                    if (stationInfo.length >= 2) {
                        stationNameTextView.setText(stationName);
                        locationTextView.setText(stationInfo[0]);
                        timeSlotTextView.setText(stationInfo[1]);
                        mobileNumberTextView.setText(stationInfo[3]); // Set mobile number
                        dateTextView.setText(stationInfo[5]); // Set date
                    } else {
                        // Handle the case where stationInfo does not have the expected number of elements
                        Log.e("ShowBookingsActivity", "Unexpected station info format for booking detail: " + bookingDetail);
                    }
                } else {
                    // Handle the case where bookingInfo does not have the expected number of elements
                    Log.e("ShowBookingsActivity", "Unexpected booking detail format: " + bookingDetail);
                }

                return convertView;
            }

        };
        listView.setAdapter(bookingsAdapter);
    }

}
