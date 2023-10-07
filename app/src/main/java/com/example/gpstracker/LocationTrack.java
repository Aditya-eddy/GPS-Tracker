package com.example.gpstracker;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class LocationTrack extends Service implements LocationListener {
    private final Context mContext;
    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;
    Location loc;
    double latitude;
    double longitude;
    private final static long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private final static long MIN_TIME_BW_UPDATES = 1000 * 60;
    protected LocationManager locationManager;

    public LocationTrack(Context mContext) {
        this.mContext = mContext;
    }

    private Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
            checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkNetwork && !checkGPS) {
                Toast.makeText(mContext, "No service provider is enabled", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;
                if (checkGPS) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void ssionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this
                    );
                    if (locationManager!=null){
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc!=null){
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    }
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return loc;
    }
    public double getLatitude(){
        if (loc!=null){
            latitude = loc.getLatitude();
        }
        return latitude;
    }
    public double getLongitude(){
        if (loc!=null){
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation(){
        return this.canGetLocation();
    }

    public void showSettingAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("GPS is not enabled");
        alertDialog.setMessage("Do you want to turn on the GPS");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void stopListener(){
        if(locationManager!=null){
            locationManager.removeUpdates(LocationTrack.this);
        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
