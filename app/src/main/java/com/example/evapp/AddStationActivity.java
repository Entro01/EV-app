package com.example.evapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.os.Bundle;
import android.location.Geocoder;
import android.location.Address;
import android.util.Log;
import java.io.IOException;
import java.util.List;
import android.widget.Toast;

public class AddStationActivity extends AppCompatActivity {
    private EditText stationNameEditText;
    private EditText locationEditText;
    private EditText priceEditText;
    private Spinner slotSpinner;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_station);

        stationNameEditText = findViewById(R.id.station_name_edit_text);
        locationEditText = findViewById(R.id.location_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
        slotSpinner = findViewById(R.id.slot_spinner);
        addButton = findViewById(R.id.add_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.slots_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        slotSpinner.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stationName = stationNameEditText.getText().toString();
                String price = priceEditText.getText().toString();
                String locationDescription = locationEditText.getText().toString();
                String slot = slotSpinner.getSelectedItem().toString();

                // Use Geocoder to get the coordinates from the location description
                Geocoder geocoder = new Geocoder(AddStationActivity.this);
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(locationDescription,  1);
                    if (!addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        double latitude = address.getLatitude();
                        double longitude = address.getLongitude();
                        // Save the station data with the obtained coordinates
                        saveStationData(stationName, price, latitude, longitude, slot);
                    } else {
                        // Handle the case where no address was found
                        Toast.makeText(AddStationActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    // Handle the case where geocoding failed
                    Log.e("Geocoding Error", "Unable to get coordinates", e);
                }
            }

            private void saveStationData(String stationName, String price, double latitude, double longitude, String slot) {
                // Save the station data with the coordinates
                SharedPreferences sharedPreferences = getSharedPreferences("STATIONS", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // Format the location as "latitude,longitude"
                String location = latitude + "," + longitude;
                editor.putString(stationName, location + "," + slot + ","  + price);
                editor.apply();

                // Go back to the MemberViewActivity
                finish();
            }
        });

    }
}
