package com.example.bluey.digitalsextant;

/**
 * Created by Tony Evans on 8/15/17.
 * Updated by Todd Gibson on 8/16/17
 */

public class CelestialBody
{
    private int         key;                         // ID for celestial body in the Database.
    private String      celestialBodyName;           // Name of the celestial body.
    private double      siderealHourAngle;           // The celestial body is Sidereal Hour Angle.
    private double      declination;                 // The declination of the celestial body.

    /**
     *  Create an instance of the Celestial Body.
     *
     * When the Celestial Body is constructed the following will be recorded
     * (1)Celestial Body's key
     * (2)Celestial Body's Name
     * (3)Celestial Body's Sidereal Hour Angle
     * (4)Celestial Body's Declination
     *
     * @param key int
     * @param celestialBodyName String
     * @param siderealHourAngle double
     * @param declination double
     */
    public CelestialBody(int key, String celestialBodyName, double siderealHourAngle, double declination)
    {
        this.key = key;//(1)
        this.celestialBodyName = celestialBodyName;//(2)
        this.siderealHourAngle = siderealHourAngle;//(3)
        this.declination = declination;//(4)
    }

    /**
     *  Create an instance of the Celestial Body with a empty constructor
     */
    public CelestialBody()
    {

    }

    /**
     *  Return the Celestial Body Key
     * @return int
     */
    public int getKey()
    {
        return key;
    }

    /**
     *  Set the Celestial Body key
     * @param key int
     */
    public void setKey(int key)
    {
        this.key = key;
    }

    /**
     *  Get the Celestial Body Name
     * @return String
     */
    public String getCelestialBodyName()
    {
        return celestialBodyName;
    }

    /**
     *  Set the Celestial Body Name
     * @param celestialBodyName String
     */
    public void setCelestialBodyName(String celestialBodyName)
    {
        this.celestialBodyName = celestialBodyName;
    }

    /**
     *  Get the Celestial Body's Sidereal Hour Angle
     * @return double
     */
    public double getSiderealHourAngle()
    {return siderealHourAngle;}

    /**
     *  Set the Celestial Body's Sidereal Hour Angle
     * @param siderealHourAngle double
     */
    public void setSiderealHourAngle(double siderealHourAngle)
    {this.siderealHourAngle = siderealHourAngle;}

    /**
     *  Get Celestial Body's Declination
     * @return double
     */
    public double getDeclination()
    {
        return declination;
    }

    /**
     *  Set the Celestial Body's Declination
     * @param declination double
     */
    public void setDeclination(double declination)
    {
        this.declination = declination;
    }


    /**
     * It puts the following Celestial Body's data in a String
     * (1)Celestial Body's Sidereal Hour Angle
     * (2)Celestial Body's Declination
     * (3)Celestial Body's Name
     * @return String
     */
    public String toSting()
    {return celestialBodyName + " SHA:" + siderealHourAngle + " Dec: " + declination;}
}
