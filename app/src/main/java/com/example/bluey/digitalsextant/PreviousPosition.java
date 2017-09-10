package com.example.bluey.digitalsextant;

import android.annotation.SuppressLint;

import java.util.Calendar;

/**
 * Created by robinluna Robin Luna on 8/25/17.
 */

public class PreviousPosition
{
    public int          Year;//Year of the previous position taken
    public int          Month;//Month of the previous position taken
    public int          Date;//Date of the previous position taken
    public int          Hour;//Hour of the previous position taken
    public int          Minute;//Minute of the previous position taken
    public int          Second;//Second of the previous position taken
    public double       Latitude;//Latitude of the previous position taken
    public double       Longitude;//Longitude of the previous position taken

    /**
     * The Default constructor for PreviousPosition
     */
    public PreviousPosition()
    {
        @SuppressLint("SimpleDateFormat")
        Calendar date = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT"));

        //(1)
        this.Year    = date.get(Calendar.YEAR);
        this.Month   = date.get(Calendar.MONTH ) + 1;
        this.Date     = date.get(Calendar.DAY_OF_MONTH);
        this.Hour    = date.get(Calendar.HOUR_OF_DAY);
        this.Minute  = date.get(Calendar.MINUTE);
        this.Second  = date.get(Calendar.SECOND);

    }


    /**
     * Sets the latitude of the position (neg = S and pos = N)
     * @param latitude double
     */
    public void setLatitude(double latitude) {this.Latitude = latitude;}

    /**
     * Sets the longitude of the position (neg = W and pos = E)
     * @param longitude double
     */
    public void setLongitude(double longitude) {this.Longitude = longitude;}

    /**
     * Gets the latitude of the position (neg = S and pos = N)
     * @return double
     */
    public double getLatitude() {return this.Latitude;}

    /**
     * Gets the longitude of the position (neg = W and pos = E)
     * @return double
     */
    public double getLongitude() {return this.Longitude;}


    /**
     * Is the 2nd line of the what is shown for an PreviousPosition object, which contains the following
     * (1) the date of the position
     * (2) the time of the position
     ** @return String
     */
    public String line1()
    {
        if(this.Minute < 10 && this.Hour < 10 && this.Second < 10)
            return  "Date: " + this.Month + "/" + this.Date + "/" + this.Year + "      Time: 0" + this.Hour + "0" + this.Minute + ".0" + this.Second;

        else if(this.Minute < 10 && this.Hour < 10)
            return  "Date: " + this.Month + "/" + this.Date + "/" + this.Year + "      Time: 0" + this.Hour + "0" + this.Minute + "." + this.Second;

        else if(this.Hour < 10 && this.Second < 10)
            return  "Date: " + this.Month + "/" + this.Date + "/" + this.Year + "      Time: 0" + this.Hour + this.Minute + ".0" + this.Second;

        else if(this.Minute < 10 && this.Second < 10)
            return  "Date: " + this.Month + "/" + this.Date + "/" + this.Year + "      Time: " + this.Hour + "0" + this.Minute + ".0" + this.Second;

        else if(this.Hour < 10)
            return  "Date: " + this.Month + "/" + this.Date + "/" + this.Year + "      Time: 0" + this.Hour + this.Minute + "." + this.Second;

        else if(this.Minute < 10)
            return  "Date: " + this.Month + "/" + this.Date + "/" + this.Year + "      Time: " + this.Hour + "0" + this.Minute + "." + this.Second;

        else if(this.Second < 10)
            return  "Date: " + this.Month + "/" + this.Date + "/" + this.Year + "      Time: " + this.Hour  + this.Minute + ".0" + this.Second;

        return "Date: " + this.Month + "/" + this.Date + "/" + this.Year + "      Time: " + this.Hour + this.Minute+ "." + this.Second;
    }

    /**
     * Is the 3rd line of the what is shown for an PreviousPosition object, which contains the following
     * (1) the latitude of the position
     * (2) the longitude of the position
     * @return String
     */
    public String line2()
    {return "Latitude: " + this.Latitude + "       Longitude: " + this.Longitude;}
}
