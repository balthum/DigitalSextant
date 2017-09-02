package com.example.bluey.digitalsextant;

/**
 * Created by robinluna Robin Luna on 8/03/17.
 */
public interface SensorChange
{
    /**
     * gets the direction and azimuth/compass bearing when their is a new reading from the sensor
     * @param direction String
     * @param azmiuth float
     */
    public void compassUpdate(String direction, float azmiuth);

    /**
     * gets the zenith/observed height when their is a new reading from the sensor
     * @param zenith float
     */
    public void zenithUpdate(float zenith);

}
