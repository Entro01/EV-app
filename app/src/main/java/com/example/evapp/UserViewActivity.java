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
import android.widget.Toolbar;


public class UserViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add markers for EV stations
        // Replace the coordinates with actual EV station locations
        LatLng station1 = new LatLng(-34, 151);
        LatLng station2 = new LatLng(-35, 150);
        mMap.addMarker(new MarkerOptions().position(station1).title("Station 1").snippet("target"));
        mMap.addMarker(new MarkerOptions().position(station2).title("Station 2"));

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

                // Set an onClickListener on the info window
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start BookingActivity
                        Intent intent = new Intent(UserViewActivity.this, BookingActivity.class);
                        startActivity(intent);
                    }
                });

                return view;
            }
        });
    }
}
