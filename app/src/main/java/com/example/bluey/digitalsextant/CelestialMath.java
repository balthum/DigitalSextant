package com.example.bluey.digitalsextant;

import java.util.Calendar;
import java.util.Hashtable;

/**
 * Created by toddgibson Todd Gibson on 8/24/17.
 */

public class CelestialMath
{
    public CelestialMath()
    {}

    /**
     *
     * Calculate the Julian Day using a Celestial Body Observation
     *
     * @param celestialBodyObservation Celestial Body Observation Object
     * @return Double
     */
    public double julianDay (CelestialBodyObservation celestialBodyObservation)
    {
        int year    = celestialBodyObservation.getYear();
        int month   = celestialBodyObservation.getMonth();
        int day     = celestialBodyObservation.getDay();
        int hour    = celestialBodyObservation.getHour();
        int minute  = celestialBodyObservation.getMinute();
        int second  = celestialBodyObservation.getSecond();

        double JD = (( 367 * year - ( Math.floor( 7 * ( year + Math.floor(( month + 9) / 12 )) / 4 )
                - Math.floor( 3 * (Math.floor(( year + ( month - 9 ) / 7)/100 + 1) / 4)
                + Math.floor( 275 * month / 9 ) + day + 1721028.5 )))
                + ( hour + minute / 60 + second / 3600 ) / 24 ) - 30.5;

        return JD;
    }

    public double julianDay (int year, int month, int day, int hour, int minute, int second)
    {
        double JD = (( 367 * year - ( Math.floor( 7 * ( year + Math.floor(( month + 9) / 12 )) / 4 )
                - Math.floor( 3 * (Math.floor(( year + ( month - 9 ) / 7)/100 + 1) / 4)
                + Math.floor( 275 * month / 9 ) + day + 1721028.5 )))
                + ( hour + minute / 60 + second / 3600 ) / 24 ) - 30.5;

        return JD;
    }


    /**
     *
     *  Calculate Julian Century
     *
     * @param julianDay double
     * @return          double
     */
    public double julianCentury(double julianDay)
    {
        return  (julianDay - 2451545 ) / 36525;
    }

    /**
     *
     * Calculate the Greenwich Hour Angle (GHA) of Aries using the Julian Date and Julian Century
     *
     * @param julianDay     Double
     * @param julianCentury Double
     * @return              Double
     *
     */
    public double ghaAries (double julianDay, double julianCentury)
    {
        return (280.46061837 + 360.98564736629 * (julianDay - 2451545)
                + (0.000387933 * Math.pow(julianCentury, 2)) -  Math.pow(julianCentury, 3) / 38710000) % 360;
    }

    /**
     *
     * Calculate the Greenwich Hour Angle (GHA) of Aries using a Celestial Body Observation Object
     *
     * @param celestialBodyObservation Celestial Body Observation Object
     * @return                         double
     */
    public double ghaAries (CelestialBodyObservation celestialBodyObservation)
    {
        double julianDay = julianDay(celestialBodyObservation);
        double julianCentury = julianCentury(julianDay);

        return ghaAries(julianDay, julianCentury);
    }

    /**
     *
     *  Calculate the Greenwich Hour Angle (GHA) of a Celestial Body.
     *
     * @param shaStar   double Sidereal Hour Angle (SHA) of the Celestial Body
     * @param ghaAries  double Greenwich Hour Angle (GHA) of Aeries
     * @return          double Greenwich Hour Angle (GHA) of the Celestial Body
     */
    public double ghaStar (double shaStar, double ghaAries )
    {
        return shaStar + ghaAries;
    }

    /**
     *
     * Calculate the Greenwich Hour Angle (GHA) of a Celestial Body.
     *
     * @param shaStar                   double Sidereal Hour Angle (SHA) of the Celestial Body
     * @param celestialBodyObservation  Celestial Body Observation Object of the Celestial Body
     * @return                          double Greenwich Hour Angle (GHA) of the Celestial Body
     */
    public double ghaStar (double shaStar, CelestialBodyObservation celestialBodyObservation)
    {
        return shaStar + ghaAries(celestialBodyObservation);
    }

    /**
     *
     *  Calculate Greenwich Hour Angle (GHA) Minute using the GHA of Aries.
     *
     * @param ghaAries Double
     * @return         Double
     *
     */
    public double ghaMinute(double ghaAries)
    {
        return ghaAries % 1;
    }

    /**
     *  Calculate the Greenwich Hour Angle (GHA) Degree using the GHA of Aries.
     *
     * @param ghaAries Double
     * @return         Integer
     *
     */
    public int ghaDegree(double ghaAries)
    {
        return (int) ghaAries;
    }

    /**
     *  Calculate the Greenwich Hour Angle (GHA) using the GHA of Aries and Sidereal Hour Angle (SHA)
     * @param ghaAries  Double Greenwich Hour Angle of Aries
     * @param sha       Double Sidereal Hour Angle (Can be found in the Celestial Database
     * @return          Double
     */
    public double gha(double ghaAries, double sha)
    {
        return ghaAries + sha;
    }

    /**
     *  Calculate the Local Hour Angle (LHA) using Greenwich Hour Angle and longitude.
     *  Note: Longitude should be in decimal notation.
     *
     * @param gha                   Double Greenwich Hour Angle (GHA)
     * @param observationLongitude  Double Longitude in decimal notation where (-) is West and (+) is East
     * @return                      Double
     */
    public double lha(double gha, double observationLongitude)
    {
        return (observationLongitude > 0 ? ( gha + observationLongitude ) : (gha - observationLongitude ) );
    }




    /**
     *  Calculate the height using declination, latitude, and Local Hour Angle
     *  Note: Latitude should be in decimal notation.
     *
     * @param declination   Double star's declination ( Celestial Database ).
     * @param latitude      Double Double Longitude in decimal notation where (-) is West and (+) is East
     * @param lha           Double Local Hour Angle (LHA)
     * @return              Double
     *
     */
    public double heightCalculated(double declination, double latitude, double lha)
    {
        return Math.asin(
                Math.sin(declination) * Math.sin(latitude)
                + Math.cos(declination) * Math.cos(latitude) * Math.cos(lha)
        );
    }

    /**
     *
     * Calculate the zenith using declination, latitude, Local Hour Angle (LHA), and Calculated Height (Hc).
     * @param declination       Double star's declination ( Celestial Database ).
     * @param latitude          Double Latitude in decimal notation where (-) is South and (+) is North
     * @param lha               Double Local Hour Angle (LHA)
     * @param heightCalculated  Double calculated Height (Hc)
     * @return                  Double
     *
     */

    public double azimuth(double declination, double latitude, double lha, double heightCalculated)
    {
        return Math.asin(
                (Math.sin(declination) * Math.cos(latitude)
                - Math.cos(declination) * Math.sin(latitude) * Math.cos(lha))
                / Math.cos(heightCalculated)
        );
    }

    /**
     *
     *  Calculate the Celestial Body Zenith
     *
     * @param assumedLatitude    Double Latitude in decimal notation where (-) is South and (+) is North
     * @param starDeclination    Double Declination of the Celestial BOdy
     * @param localHourAngle     Double Local Hour Angle (LHA)
     * @return                   Double
     */
    public double starZenith( double assumedLatitude, double starDeclination, double localHourAngle )
    {
        return Math.acos(assumedLatitude) * Math.sin(starDeclination) * Math.cos(assumedLatitude)
                * Math.cos(assumedLatitude) * Math.cos(localHourAngle);
    }

    /**
     *
     * Calculate the Celestial Body Zenith
     *
     * @param assumedLatitude           Double Latitude in decimal notation where (-) is South and (+) is North
     * @param celestialBodyObservation  CelestialBodyObservation Object
     * @param celestialBody             CelestialBody Object
     * @return
     */
    public double starZenith(double assumedLatitude, CelestialBodyObservation celestialBodyObservation, CelestialBody celestialBody)
    {
        double ghaAries = ghaAries( celestialBodyObservation );
        double gha      = gha(ghaAries, celestialBody.getSiderealHourAngle());
        double lha      = lha(gha, assumedLatitude);

        return Math.acos(assumedLatitude) * Math.sin(celestialBody.getDeclination()) * Math.cos(assumedLatitude)
                * Math.cos(assumedLatitude) * Math.cos(lha);
    }

    /**
     *
     * Calculate the ITC
     *
     * @param heightCalculated  Double Calculated height (Hc)
     * @param heightObserved    Double Observed height (Ho)
     * @return                  Double
     */
    public double intercept(double heightCalculated, double heightObserved)
    {
        return Math.abs(heightCalculated - heightObserved);
    }

    /**
     *
     *  Calculate A celestial bodies compass bearing from your assumed position.
     *
     * @param starDeclination   Double Celestial Bodies declination.
     * @param starSHA           Double Celestial Bodies Sidereal Hour Angle (SHA).
     * @param assumedLatitude   Double Latitude in decimal notation where (-) is South and (+) is North
     * @return                  Double Compass Bearing to the Celestial Body from the assumed position.
     */
    public double starBearingFromAssumedPosition( double starDeclination, double starSHA, double assumedLatitude)
    {
        Calendar date = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT"));

        //(1)
        int year                = date.get(Calendar.YEAR);
        int month               = date.get(Calendar.MONTH ) + 1;
        int day                 = date.get(Calendar.DAY_OF_MONTH);
        int hour                = date.get(Calendar.HOUR_OF_DAY);
        int minute              = date.get(Calendar.MINUTE);
        int second              = date.get(Calendar.SECOND);
        double julianDay        = julianDay(year, month, day, hour, minute, second);
        double julianCentury    = julianCentury(julianDay);

        double ghaAries         = ghaAries(julianDay, julianCentury);

        double lha              = lha( gha(ghaAries, starSHA), assumedLatitude);

        return Math.atan(

                Math.sin(lha) /
                        ( Math.sin(assumedLatitude) * Math.cos(lha) - Math.tan(starDeclination) * Math.cos(assumedLatitude) )

                        );
    }

    /**
     *
     *  Calculate the distance between to latitude and longitude points in Nautical Miles.
     *
     * @param latitudePointA    Double Latitude in decimal notation where (-) is South and (+) is North
     * @param longitudePointA   Double Longitude in decimal notation where (-) is West and (+) is South
     * @param latitudePointB    Double Latitude in decimal notation where (-) is South and (+) is North
     * @param longitudePointB   Double Longitude in decimal notation where (-) is West and (+) is South
     * @return                  Double Nautical Miles
     */
    public double distanceBetweenTwoLatitudeAndLongitude(double latitudePointA, double longitudePointA,double latitudePointB, double longitudePointB )
    {
        return Math.acos(
                            60 * Math.sin(latitudePointA) * Math.sin(latitudePointB)
                               + Math.cos(latitudePointA) * Math.cos(latitudePointB)
                               * Math.cos(longitudePointB - longitudePointA        )
                        );
    }


    public double Zn(double assumedLatitude, double lhaStar, double azimuth )
    {
        if ( 0 < assumedLatitude )
        {
            if (180 < lhaStar )
            {
                return azimuth;
            }

            else
            {
                return 360 - azimuth;
            }
        }
        else {
            if (180 < lhaStar)
            {
                return 180 - azimuth;
            }
            else
            {
                return 180 + azimuth;
            }
        }
    }

    /**
     *
     *  Calculate the Observed Height
     *
     * @param sextantHeight double height of the sextant observation
     * @return              double
     */
    public double observedHeight(double sextantHeight)
    {
        return ( 0.96  / Math.tan(sextantHeight ) + sextantHeight );
    }

    /**
     *
     *  Main correction for Sextant Observation
     *
     * @param heightObserved    Double Sextant Height
     * @return                  Double
     */
    public double mainCorrection(double heightObserved)
    {
        Hashtable<Integer, Double> correction = new Hashtable<>();

        if ( 8 < heightObserved && 91 > heightObserved )
        {
            correction.put(9,-5.9);
            correction.put(10,-5.3);
            correction.put(11,-4.8);
            correction.put(12,-4.5);
            correction.put(13,-4.1);
            correction.put(14,-3.8);
            correction.put(15,-3.6);
            correction.put(16,-3.3);
            correction.put(17,-3.1);
            correction.put(18,-3.0);
            correction.put(19,-2.8);
            correction.put(20,-2.6);
            correction.put(21,-2.5);
            correction.put(22,-2.4);
            correction.put(23,-2.3);
            correction.put(24,-2.2);
            correction.put(25,-2.1);
            correction.put(26,-2.0);
            correction.put(27,-1.9);
            correction.put(28,-1.8);
            correction.put(30,-1.7);
            correction.put(32,-1.6);
            correction.put(34,-1.4);
            correction.put(36,-1.3);
            correction.put(38,-1.2);
            correction.put(40,-1.1);
            correction.put(41,-1.1);
            correction.put(42,-1.1);
            correction.put(43,-1.1);
            correction.put(44,-1.0);
            correction.put(45,-1.0);
            correction.put(46,-0.9);
            correction.put(47,-0.9);
            correction.put(48,-0.9);
            correction.put(49,-9.0);
            correction.put(50,-0.8);
            correction.put(51,-0.8);
            correction.put(52,-0.8);
            correction.put(53,-0.8);
            correction.put(54,-0.7);
            correction.put(55,-0.7);
            correction.put(56,-0.7);
            correction.put(57,-0.7);
            correction.put(58,-0.6);
            correction.put(59,-0.6);
            correction.put(60,-0.6);
            correction.put(61,-0.6);
            correction.put(62,-0.5);
            correction.put(63,-0.5);
            correction.put(64,-0.5);
            correction.put(65,-0.5);
            correction.put(66,-0.4);
            correction.put(67,-0.4);
            correction.put(68,-0.4);
            correction.put(69,-0.4);
            correction.put(70,-0.3);
            correction.put(71,-0.3);
            correction.put(72,-0.3);
            correction.put(73,-0.3);
            correction.put(74,-0.3);
            correction.put(75,-0.3);
            correction.put(76,-0.3);
            correction.put(77,-0.3);
            correction.put(78,-0.3);
            correction.put(79,-0.3);
            correction.put(80,-0.2);
            correction.put(81,-0.2);
            correction.put(82,-0.2);
            correction.put(83,-0.2);
            correction.put(84,-0.2);
            correction.put(85,-0.2);
            correction.put(86,-0.2);
            correction.put(87,-0.2);
            correction.put(88,-0.2);
            correction.put(89,-0.2);
            correction.put(90,0.0);

            return correction.get( (int) heightObserved);
        }
        else if (9 > heightObserved)
        {
            return -6.0;
        }
        else
        {
            return  0.0;
        }
    }
}
