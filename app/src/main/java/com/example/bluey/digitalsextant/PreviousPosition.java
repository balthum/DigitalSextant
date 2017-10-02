package com.example.bluey.digitalsextant;

import android.annotation.SuppressLint;

import java.util.Calendar;

/**
 * Created by robinluna Robin Luna on 8/25/17.
 */

public class PreviousPosition
{
    public int       Title;
    public String       DateTime;
    public int          Year;//Year of the previous position taken
    public int          Month;//Month of the previous position taken
    public int          Date;//Date of the previous position taken
    public int          Hour;//Hour of the previous position taken
    public int          Minute;//Minute of the previous position taken
    public int          Second;//Second of the previous position taken
    public double       Latitude;//Latitude of the previous position taken
    public double       Longitude;//Longitude of the previous position taken
    public String       LatitudeDirection;
    public String       LongitudeDirection;

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

        this.DateTime = this.Year + "-" + this.Month + "-" + this.Date
                + " " + this.Hour + ":" + this.Minute + ":" + this.Second;

    }

    public void setTitle(int title){this.Title = title;}

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

    public String getDateTime(){return this.DateTime;}

    public String getLatitudeDirection(){return this.LatitudeDirection;}

    public String getLongitudeDirection(){return this.LongitudeDirection;}

    public int getTitle(){return this.Title;}


    public String line1()
    {
        return "Position " + Title;
    }

    public void setLatitudeDirection()
    {
        if(this.Latitude < 0)
            this.LatitudeDirection = "S";
        else if(this.Latitude > 0)
            this.LatitudeDirection = "N";
        else
            this.LatitudeDirection = " ";
    }

    public void setLongitudeDirection()
    {
        if(this.Longitude < 0)
            this.LongitudeDirection = "W";
        else if(this.Longitude > 0)
            this.LongitudeDirection = "E";
        else
            this.LongitudeDirection = " ";
    }



    public String getLatitudeString()
    {
        int degree = Math.abs((int)this.Latitude);
        double decimalMinute = ((Math.abs(this.Latitude) - degree) * 60);

        return String.format("%dº %.2f' %s",degree, decimalMinute, this.LatitudeDirection);
    }

    public String getLongitudeString()
    {
        int degree = Math.abs((int)this.Longitude);
        double decimalMinute = ((Math.abs(this.Longitude) - degree) * 60);

        return String.format("%dº %.2f' %s",degree, decimalMinute, this.LongitudeDirection);
    }


    /**
     * Is the 2nd line of the what is shown for an PreviousPosition object, which contains the following
     * (1) the date of the position
     * (2) the time of the position
     ** @return String
     */
    public String line2()
    {
       return String.format("Date: %02d/%02d/%d %10s Time: %02d%02d:%02d",  this.Month, this.Date, this.Year, " ", this.Hour, this.Minute, this.Second);
    }

    /**
     * Is the 3rd line of the what is shown for an PreviousPosition object, which contains the following
     * (1) the latitude of the position
     * (2) the longitude of the position
     * @return String
     */
    public String line3()
    {return "Latitude: " + this.Latitude + "       Longitude: " + this.Longitude;}
}
