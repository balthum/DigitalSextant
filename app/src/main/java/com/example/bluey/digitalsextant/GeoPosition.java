package com.example.bluey.digitalsextant;

/**
 * Created by toddgibson Todd Gibson on 9/18/17.
 */

public class GeoPosition
{
    Double Latitude;
    Double Longitude;

    public GeoPosition(Double latitude, Double longitude)
    {
        Latitude = latitude;
        Longitude = longitude;
    }

    public Double getLatitude()
    {
        return Latitude;
    }

    public void setLatitude(Double latitude)
    {
        Latitude = latitude;
    }

    public Double getLongitude()
    {
        return Longitude;
    }

    public void setLongitude(Double longitude)
    {
        Longitude = longitude;
    }
}
