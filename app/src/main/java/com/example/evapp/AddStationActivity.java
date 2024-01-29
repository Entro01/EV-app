package com.example.evapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.os.Bundle;


import android.os.Bundle;

public class AddStationActivity extends AppCompatActivity {
    private EditText stationNameEditText;
    private EditText locationEditText;
    private Spinner slotSpinner;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_station);

        stationNameEditText = findViewById(R.id.station_name_edit_text);
        locationEditText = findViewById(R.id.location_edit_text);
        slotSpinner = findViewById(R.id.slot_spinner);
        addButton = findViewById(R.id.add_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.slots_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        slotSpinner.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stationName = stationNameEditText.getText().toString();
                String location = locationEditText.getText().toString();
                String slot = slotSpinner.getSelectedItem().toString();

                // Save the station data
                SharedPreferences sharedPreferences = getSharedPreferences("STATIONS", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(stationName, location + "," + slot);
                editor.apply();

                // Go back to the MemberViewActivity
                finish();
            }
        });
    }
}
