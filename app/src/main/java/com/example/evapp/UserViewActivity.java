package com.example.evapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.view.View;
import android.widget.Button;
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
import android.view.LayoutInflater;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.view.Gravity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import androidx.room.Room;

public class UserViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SearchView searchView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private Button bookingsButton;
    private FusedLocationProviderClient fusedLocationClient;
    private AppDatabase db;


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String confirmationMessage = intent.getStringExtra("confirmation_message");
        if (confirmationMessage != null) {
            showConfirmationPopup(confirmationMessage);
        }
    }

    private void showConfirmationPopup(String confirmationMessage) {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_confirmation, null);
        TextView confirmationText = popupView.findViewById(R.id.confirmation_text);
        confirmationText.setText(confirmationMessage);

        final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        // Set a background for the popup window
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);

        // Dismiss the popup window when the user touches outside of it
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);
        searchView = findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button bookingsButton = findViewById(R.id.bookings_button);
        bookingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserViewActivity.this, ShowBookingsActivity.class);
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        // Initialize the Room database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "UserDatabase").build();
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

        // Load stations from the database
        new Thread(() -> {
            List<Station> stations = db.userDao().getStations();
            for (Station station : stations) {
                String stationName = station.getStationName();
                String location = station.getLatitude() + "," + station.getLongitude();
                String slot = station.getSlot();

                // Proceed with your existing logic for adding markers
                String[] locationCoordinates = location.split(",");
                if (locationCoordinates.length == 2) {
                    try {
                        double latitude = Double.parseDouble(locationCoordinates[0]);
                        double longitude = Double.parseDouble(locationCoordinates[1]);
                        LatLng stationLocation = new LatLng(latitude, longitude);

                        // Use runOnUiThread to ensure this runs on the main thread
                        runOnUiThread(() -> {
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(stationLocation)
                                    .title(stationName)
                                    .snippet(slot);
                            mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stationLocation, 15f));
                            Log.d("MarkerCreation", "Marker added for station: " + stationName);
                        });
                    } catch (NumberFormatException e) {
                        Log.e("Location Parsing", "Error parsing location data", e);
                    }
                } else {
                    Log.e("Location Parsing", "Unexpected location data format");
                }
            }
        }).start();
            // Set an info window adapter to handle clicks on the markers
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                // Return null to disable the default info window
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layout for the info window
                View view = getLayoutInflater().inflate(R.layout.info_window, null);

                // Get references to the views in the layout
                TextView titleTextView = view.findViewById(R.id.title_text_view);
                TextView snippetTextView = view.findViewById(R.id.snippet_text_view);

                // Set the text on the views
                titleTextView.setText(marker.getTitle());
                snippetTextView.setText(marker.getSnippet());

                // Return the view to the framework
                return view;
            }
        });

        // Set the listener for marker info window clicks
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // Launch the BookingActivity with the station name
                Intent bookingIntent = new Intent(UserViewActivity.this, BookingActivity.class);
                bookingIntent.putExtra("station_name", marker.getTitle());
                startActivity(bookingIntent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Geocoder geocoder = new Geocoder(UserViewActivity.this);
                List<Address> addresses;

                try {
                    addresses = geocoder.getFromLocationName(query, 1);
                    if (!addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    } else {
                        Toast.makeText(UserViewActivity.this, "No location found for the entered query", Toast.LENGTH_SHORT).show();
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

        // After setting up the map, request the user's last known location
        getLastKnownLocation();
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return;
        }

        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    // Set the map's camera position to the current location of the device.
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude()), 10));
                } else {
                    Log.d("UserViewActivity", "Current location is null. Using defaults.");
                    Log.e("UserViewActivity", "Exception: %s", task.getException());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4219999, -122.0840575), 10));
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                }
            }
        });
    }


}
