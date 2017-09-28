
package com.example.bluey.digitalsextant;

/**
 * Created by toddgibson Todd Gibson on 9/19/17.
 */


public class GeoLine
{
    GeoPosition PositionOne;
    GeoPosition PositionTwo;

    public GeoLine()
    {
    }

    public GeoPosition getPositionOne()
    {
        return PositionOne;
    }

    public void setPositionOne(GeoPosition positionOne)
    {
        PositionOne = positionOne;
    }

    public GeoPosition getPositionTwo()
    {
        return PositionTwo;
    }

    public void setPositionTwo(GeoPosition positionTwo)
    {
        PositionTwo = positionTwo;
    }
}

