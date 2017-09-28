package com.example.bluey.digitalsextant;

//*
// * Created by toddgibson Todd Gibson on 9/19/17.



public class LineOfPosition extends GeoLine
{
    private GeoPosition     assumedLocationPosition;        // Assumed position as an Geographical position object.
    private GeoPosition     lopLocationPosition;            // Line of Position (LOP) location intercept location.
    private double          interceptDistanceNauticalMiles; // Distance in Nautical Miles to the LOP intercept location.
    private double          LineOfBearing;                  // Bering in Degrees to intercept location.

    public LineOfPosition()
    {

    }

    public GeoPosition getAssumedLocationPosition()
    {
        return assumedLocationPosition;
    }


    public void setAssumedLocationPosition(GeoPosition assumedLocationPosition)
    {
        this.assumedLocationPosition = assumedLocationPosition;
    }

    public GeoPosition getLopLocationPosition()
    {
        return lopLocationPosition;
    }

    public void setLopLocationPosition(GeoPosition lopLocationPosition)
    {
        this.lopLocationPosition = lopLocationPosition;
    }

    public double getInterceptDistanceNauticalMiles()
    {
        return interceptDistanceNauticalMiles;
    }

    public void setInterceptDistanceNauticalMiles(double interceptDistanceNauticalMiles)
    {
        this.interceptDistanceNauticalMiles = interceptDistanceNauticalMiles;
    }

    public double getLineOfBearing()
    {
        return LineOfBearing;
    }

    public void setLineOfBearing(double lineOfBearing)
    {
        LineOfBearing = lineOfBearing;
    }
}
