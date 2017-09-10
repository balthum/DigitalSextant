package com.example.bluey.digitalsextant;

/**
 * Created by toddgibson Todd Gibson on 9/9/17.
 */

public class CircleOfEqualAltitude
{
    private String name;
    private double latitude;
    private double longitude;
    private double radius;

    public CircleOfEqualAltitude()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getRadius()
    {
        return radius;
    }

    public void setRadius(double radius)
    {
        this.radius = radius;
    }

    public double getRadiusNauticalMiles()
    {
        return radius * 60;
    }
}
