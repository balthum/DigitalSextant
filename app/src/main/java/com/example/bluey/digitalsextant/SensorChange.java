package com.example.bluey.digitalsextant;

/**
 * Created by robinluna Robin Luna on 8/03/17.
 */
public interface SensorChange
{
    /**
     * gets the direction and azimuth/compass bearing when their is a new reading from the sensor
     * @param direction String
     * @param azimuth float
     */
    public void compassUpdate(String direction, float azimuth);

    /**
     * gets the zenith/observed height when their is a new reading from the sensor
     * @param observedHeight float
     */
    public void observedHeightUpdate(float observedHeight);


}
