package com.example.bluey.digitalsextant;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;
import java.util.TimerTask;

/**
 * Created by Bluey on 9/4/17.
 */

public class GPSTimer extends TimerTask
{


    private Context context;
    private GPSModule gpsModule;


    public GPSTimer(Context context)
    {
        this.context = context;
    }
    @Override
    public void run()
    {
        Log.d("timer",  String.valueOf(System.currentTimeMillis()));

    }


}
