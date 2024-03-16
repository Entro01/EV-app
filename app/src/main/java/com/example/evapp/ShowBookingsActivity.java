package com.example.evapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class ShowBookingsActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> bookingsList;
    private ArrayAdapter<String> bookingsAdapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bookings);

        listView = findViewById(R.id.list_view);
        bookingsList = new ArrayList<>();

        // Initialize the Room database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "UserDatabase").build();

        // Inflate the header layout
        View header = getLayoutInflater().inflate(R.layout.header, null);
        // Add the header to the ListView
        listView.addHeaderView(header);

        // Load bookings from the database
        new Thread(() -> {
            List<Booking> bookings = db.userDao().getBookings(); // Assuming you have a method to get bookings
            for (Booking booking : bookings) {
                bookingsList.add(booking.getUserName() + ", " + booking.getStationName() + ", " + booking.getCarModel() + ", " + booking.getVehicleNumber() + ", " + booking.getPhoneNumber() + ", " + booking.getTime() + ", " + booking.getDate() + ", " + booking.getSlot());
            }
            runOnUiThread(() -> {
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
                        TextView mobileNumberTextView = convertView.findViewById(R.id.mobile_number_text_view);
                        TextView dateTextView = convertView.findViewById(R.id.date_text_view);

                        String bookingDetail = getItem(position);
                        String[] bookingInfo = bookingDetail.split(", ");
                        if (bookingInfo.length >= 2) {
                            stationNameTextView.setText(bookingInfo[1]);
                            locationTextView.setText(bookingInfo[0]);
                            timeSlotTextView.setText(bookingInfo[2]);
                            mobileNumberTextView.setText(bookingInfo[4]);
                            dateTextView.setText(bookingInfo[6]);
                        }

                        return convertView;
                    }
                };
                listView.setAdapter(bookingsAdapter);
            });
        }).start();
    }
}
