package com.example.bluey.digitalsextant;

import android.annotation.SuppressLint;

import java.util.Calendar;
import java.util.Date;
import android.icu.util.TimeZone;

import java.util.Date;


/**
 * Created by toddgibson Todd Gibson on 8/23/17.
 * Updated by robinluna Robin Luna on 8/24/17
 */

public class CelestialBodyObservation
{
    public String      Title;                       // Title of observation
    public String      CelestialBodyName;           // Name of the CelestialBody.
    public float       CompassHeading;              // Compass heading at time of observation.
    public String      CompassDirection;            // Compass direction = N, W, S, E
    public float       HeightObserver;              // Height Observed Degrees above the horizon.
    public int         Year;                        // Year of the observation
    public int         Month;                       // month of the observation
    public int         Day;                         // day of the observation
    public int         Hour;                        // hour of the observation
    public int         Minute;                      // minute of the observation
    public int         Second;                      // seconds of the observation

    /**
     *  Create an instance of the Celestial Body Observation.
     *
     * When the observation is constructed the following will be recorded
     * (1) the time of the observation
     * (2) the title of the observation
     * (3) the name of the star that is observed
     * (4) the compass bearing of the observation
     * (5) the compass direction of the observation
     * (6) the height of the star that was observed
     */
    CelestialBodyObservation()
    {
        @SuppressLint("SimpleDateFormat")
        Calendar date = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT"));

        //(1)
        this.Year    = date.get(Calendar.YEAR);
        this.Month   = date.get(Calendar.MONTH ) + 1;
        this.Day     = date.get(Calendar.DAY_OF_MONTH);
        this.Hour    = date.get(Calendar.HOUR_OF_DAY);
        this.Minute  = date.get(Calendar.MINUTE);
        this.Second  = date.get(Calendar.SECOND);

        this.Title = "";//(2)
        this.CelestialBodyName = ""; //(3)
        this.CompassHeading = 0;//(4)
        this.CompassDirection = "";//(5)
        this.HeightObserver = 0;//(6)
    }


    /**
     * Sets the Title for the observation such as Obsevation 1, Obsevation 2, and so on.
     * @param title String
     */
    public void setTitle(String title) {this.Title = title;}

    /**
     * Sets the Name of the Star observation.
     * @param celestialBodyName String
     */
    public void setCelestialBodyName(String celestialBodyName)
    {this.CelestialBodyName = celestialBodyName;}

    /**
     * Sets the Compass Bearing.
     * @param compassHeading float
     */
    public void setCompassHeading(float compassHeading)
    {this.CompassHeading = compassHeading;}

    /**
     * Sets the Height observed of the star, also known as the zenith.
     * @param heightObserver float
     */
    public void setHeightObserver(float heightObserver)
    {this.HeightObserver = heightObserver;}

    /**
     * Sets the Compass Direction, such as N,S,W, and E.
     * @param direction String
     */
    public void setCompassDirection(String direction)
    {this.CompassDirection = direction;}

    /**
     * Gets the Compass Bearing.
     * @return float
     */
    public float getCompassHeading()
    {
        return CompassHeading;
    }

    /**
     * Gets the Height observed of the star, also known as the zenith.
     * @return float
     */
    public float getHeightObserver()
    {
        return HeightObserver;
    }

    /**
     * Sets the year of the time we did the observation.
     * @return int
     */
    public int getYear()
    {
        return Year;
    }

    /**
     * Sets the month of the time we did the observation.
     * @return int
     */
    public int getMonth()
    {
        return Month;
    }

    /**
     * Sets the day of the time we did the observation.
     * @return int
     */
    public int getDay()
    {
        return Day;
    }

    /**
     * Sets the hour of the time we did the observation.
     * @return int
     */
    public int getHour()
    {
        return Hour;
    }

    /**
     * Sets the minute of the time we did the observation.
     * @return int
     */
    public int getMinute()
    {
        return Minute;
    }

    /**
     * Sets the seconds of the time we did the observation.
     * @return int
     */
    public int getSecond()
    {
        return Second;
    }

    /**
     * Is the 1st line of the what is shown for an CelestialBodyObservation object, which contains the following
     * (1) the Title name of the observation
     * @return String
     */
    public String line1() {return this.Title;}

    /**
     * Is the 2nd line of the what is shown for an CelestialBodyObservation object, which contains the following
     * (1) the date of the position
     * (2) the time of the position
     ** @return String
     */
    public String line2()
    {
        if(this.Minute < 10 && this.Hour < 10)
            return  "Date: " + this.Month + "/" + this.Day + "/" + this.Year + "      Time: 0" + this.Hour + "0" + this.Minute;

        else if(this.Hour < 10)
            return  "Date: " + this.Month + "/" + this.Day + "/" + this.Year + "      Time: 0" + this.Hour + this.Minute;

        else if(this.Minute < 10)
            return  "Date: " + this.Month + "/" + this.Day + "/" + this.Year + "      Time: " + this.Hour + "0" + this.Minute;

        else
            return "Date: " + this.Month + "/" + this.Day + "/" + this.Year + "      Time: " + this.Hour + this.Minute;
    }

    /**
     * Is the 3rd line of the what is shown for an CelestialBodyObservation object, which contains the following
     * (1) the star name of the star observed
     * (2) the height observed of the star
     * (3) the compass bearing
     * (4) the compass direction
     * @return String
     */
    public String line3()
    {return "Star Name: " + this.CelestialBodyName + "       OBSh: " + this.HeightObserver + "       Az: " + this.CompassHeading + " " + this.CompassDirection;}
}
