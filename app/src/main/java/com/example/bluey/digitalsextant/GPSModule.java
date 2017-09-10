package com.example.bluey.digitalsextant;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Bluey on 8/23/17.
 */

public class GPSModule extends Service implements LocationListener
{
    private final Context context;

    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;

    private Location location;

    private double latitude;
    private double longitude;
    private GPSChange gpsChange;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 1000;

    private LocationManager locationManager;



    public GPSModule(Context context)
    {
        this.gpsChange = (GPSChange)context;
        this.context = context;
        getLocation();
    }


    public Location getLocation()
    {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled)
            {
                stopUsingGPS();

            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                }

                if(isGPSEnabled) {
                    if(location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if(locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    public void stopUsingGPS() {
        if(locationManager != null) {
            locationManager.removeUpdates(GPSModule.this);
        }
    }

    public double getLatitude() {
        if(location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if(location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    @Override
    public void onLocationChanged(Location arg0)
    {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();

        //gpsChange.locationUpdate(latitude,longitude);

    }

    @Override
    public void onProviderDisabled(String arg0)
    {
        stopUsingGPS();

    }

    @Override
    public void onProviderEnabled(String arg0)
    {
        getLocation();

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2)
    {
        this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(isGPSEnabled || isNetworkEnabled)
            getLocation();
        else
        {
            this.canGetLocation = false;
            stopUsingGPS();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}