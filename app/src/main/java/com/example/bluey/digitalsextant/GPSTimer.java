package com.example.bluey.digitalsextant;

import android.util.Log;

import java.util.TimerTask;

/**
 * Created by Bluey on 9/4/17.
 */

public class GPSTimer extends TimerTask
{



    public GPSTimer()
    {
    }
    @Override
    public void run()
    {
        Log.d("timer",  String.valueOf(System.currentTimeMillis()));
    }
}