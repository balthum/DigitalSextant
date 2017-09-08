package com.example.bluey.digitalsextant;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Bluey on 8/23/17.
 */

public class GPSModule extends Service implements LocationListener
{
    private Context                     context;
    private boolean                     isGPSEnabled = false; //flag for GPS status
    private boolean                     isNetworkEnabled = false; //flag for network status
    private boolean                     canGetLocation = false; //sensor can get Location
    private Location                    location; //location
    private double                      latitude; //latitude
    private double                      longitude; //longitude
    private long                        min_time_bw_updates = 1000 * 60; //The minimum time between updates in milliseconds (1 minute)
    private static final long           MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    protected LocationManager           locationManager; //declaring location manager


    public GPSModule(Context context)
    {
        this.context = context;
        getLocation();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        location.getLongitude();
        location.getLatitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(GPSModule.this,
                "Provider enabled by the user. GPS turned on",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(GPSModule.this,
                "Provider disabled by the user. GPS turned off",
                Toast.LENGTH_LONG).show();


    }

    public double getLatitude()
    {
        if(location != null)
            latitude = location.getLatitude();

        return latitude;
    }

    public double getLongitude()
    {
        if(location != null)
            longitude = location.getLongitude();

        return longitude;
    }

    public void stopUsingGPS()
    {
        if(locationManager != null)
            locationManager.removeUpdates(GPSModule.this);
    }

    public boolean canGetLocation() {return this.canGetLocation;}


    public Location getLocation()
    {

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        //getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        //getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isGPSEnabled && !isNetworkEnabled)
        {}
        else
        {
            this.canGetLocation = true;

            //network provider is enabled
            if(isNetworkEnabled)
            {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        min_time_bw_updates,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }

            if(isGPSEnabled)
            {
                if(location == null)
                {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            min_time_bw_updates,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if(locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if(location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }

        }
        return location;
    }

}
