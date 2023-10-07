package com.example.gpstracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements LocationListener{
    private LocationManager locationManager;
    private TextView myLatitude;
    private TextView myLongitude;
    private final int locationPermissionCode = 2;
//    private MapView mapView;
//    private GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myLatitude = findViewById(R.id.myLatitude);
        myLongitude = findViewById(R.id.myLongitude);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, locationPermissionCode);
        } else {
            // Request location updates when permission is granted
            requestLocationUpdates();
            // Get the last known location and display it
            displayLastKnownLocation();
        }
    }

    private void requestLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1f, this);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    private void displayLastKnownLocation() {
        try {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                myLatitude.setText(String.valueOf(lastKnownLocation.getLatitude()));
                myLongitude.setText(String.valueOf(lastKnownLocation.getLongitude()));
            } else {
                // Handle the case where there is no last known location available
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        myLatitude.setText(String.valueOf(location.getLatitude()));
        myLongitude.setText(String.valueOf(location.getLongitude()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == locationPermissionCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
                displayLastKnownLocation();
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
