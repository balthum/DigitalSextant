package com.example.bluey.digitalsextant;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.TimerTask;

/**
 * Created by Bluey on 9/4/17.
 */

public class GPSTimer extends TimerTask implements GPSChange
{
    private double      latitude;
    private double      longitude;
    private Context     context;



    public GPSTimer(Context context)
    {
        GPSModule gpsModule = new GPSModule(context);
        this.context = context;

    }
    @Override
    public void run()
    {
        Log.d("timer",  String.valueOf(System.currentTimeMillis()));
        PreviousPosition previousPosition = new PreviousPosition();

        PreviousPositionDataManager previousPositionDataManager = new PreviousPositionDataManager(context);
        ArrayList<PreviousPosition> arrayList = new ArrayList<>(previousPositionDataManager.getPositionFromDatabase());

        previousPosition.setLatitude(latitude);
        previousPosition.setLongitude(longitude);

        arrayList.add(previousPosition);// adds record to ArrayList
        previousPositionDataManager.updatePositionDatabase(arrayList);//updates Previous position database for record added

    }

    @Override
    public void locationUpdate(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;

    }
}