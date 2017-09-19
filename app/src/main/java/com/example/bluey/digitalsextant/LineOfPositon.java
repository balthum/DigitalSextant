package com.example.bluey.digitalsextant;

/**
 * Created by toddgibson Todd Gibson on 9/16/17.
 */

public class LineOfPositon extends GeoPosition
{
    double itc;
    double Zn;

    public LineOfPositon()
    {
        super(0.0, 0.0);
        this.itc = 0.0;
        this.Zn  = 0.0;
    }

    public LineOfPositon(Double latitude, Double longitude, double itc, double zn)
    {
        super(latitude, longitude);
        this.itc = itc;
        Zn = zn;
    }

    public double getItc()
    {
        return itc;
    }

    public void setItc(double itc)
    {
        this.itc = itc;
    }

    public double getZn()
    {
        return Zn;
    }

    public void setZn(double zn)
    {
        Zn = zn;
    }
}
