package com.example.evapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Address;
import java.io.IOException;
import java.util.List;
import com.google.android.gms.maps.CameraUpdateFactory;
import android.widget.Toast;
import android.util.Log;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.SharedPreferences;
import java.util.Map;
import android.widget.Button;


public class MemberViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SearchView searchView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private Button addStationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_view);
        searchView = findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        addStationButton = findViewById(R.id.add_station_button);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        addStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberViewActivity.this, AddStationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Location permission was granted. Now you can use location services.
                } else {
                    // Location permission was denied. Disable the functionality that depends on location services.
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        SharedPreferences sharedPreferences = getSharedPreferences("STATIONS", MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String stationName = entry.getKey();
            String[] stationData = ((String) entry.getValue()).split(",");
            String location = stationData[0];
            String slot = stationData[1];

            // Convert the location to a LatLng object
            // This assumes that the location is stored as a comma-separated pair of latitude and longitude
            String[] locationCoordinates = location.split(",");
            if (locationCoordinates.length == 2) {
                try {
                    double latitude = Double.parseDouble(locationCoordinates[0]);
                    double longitude = Double.parseDouble(locationCoordinates[1]);
                    LatLng stationLocation = new LatLng(latitude, longitude);

                    // Add the station to the map as a marker
                    mMap.addMarker(new MarkerOptions().position(stationLocation).title(stationName).snippet(slot));
                } catch (NumberFormatException e) {
                    // Handle the case where the location data cannot be parsed into a double
                    Log.e("Location Parsing", "Error parsing location data", e);
                }
            } else {
                // Handle the case where the location data is not in the expected format
                Log.e("Location Parsing", "Unexpected location data format");
            }
        }
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = getLayoutInflater().inflate(R.layout.info_window, null);
                TextView titleTextView = view.findViewById(R.id.title_text_view);
                TextView snippetTextView = view.findViewById(R.id.snippet_text_view);
                titleTextView.setText(marker.getTitle());
                snippetTextView.setText(marker.getSnippet());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MemberViewActivity.this, BookingActivity.class);
                        startActivity(intent);
                    }
                });

                return view;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Geocoder geocoder = new Geocoder(MemberViewActivity.this);
                List<Address> addresses;

                try {
                    addresses = geocoder.getFromLocationName(query, 1);
                    if (!addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    } else {
                        Toast.makeText(MemberViewActivity.this, "No location found for the entered query", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Log.e("Geocoder", "Exception: ", e);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}


