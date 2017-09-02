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

/**
 * Created by Bluey on 8/23/17.
 */

public class GPSModule extends Service implements LocationListener
{
    private Context context;
    private boolean isGPSEnabled = false; //flag for GPS status
    private boolean isNetworkEnabled = false; //flag for network status
    private boolean canGetLocation = false; //sensor can get Location
    private Location location; //location
    private double latitude; //latitude
    private double longitude; //longitude
    private long min_time_bw_updates = 1000 * 60; //The minimum time between updates in milliseconds (1 minute)
    protected LocationManager locationManager; //declaring location manager


    public GPSModule(CelestialBodyObservationFragmentActivity celestialBodyObservationFragmentActivity)
    {
        this.context = celestialBodyObservationFragmentActivity.getActivity();

    }

    public GPSModule(HomePageFragment homePageFragment)
    {
        this.context = homePageFragment.getActivity();
    }

//    public Preference getGPSUpdates()
//    {
//
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public Location getLocation()
    {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        //getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        //getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        //network provider is enabled
        if(isNetworkEnabled)
        {
            //int d = 0;
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,min_time_bw_updates,d);

        }

        return getLocation();

        //go to https://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
    }
}
