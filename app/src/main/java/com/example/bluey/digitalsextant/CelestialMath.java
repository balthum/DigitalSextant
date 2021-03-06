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
        double year    = celestialBodyObservation.getYear();
        double month   = celestialBodyObservation.getMonth();
        double day     = celestialBodyObservation.getDay();
        double hour    = celestialBodyObservation.getHour();
        double minute  = celestialBodyObservation.getMinute();
        double second  = celestialBodyObservation.getSecond();

        double JD = (( 367 * year - ( Math.floor( 7 * ( year + Math.floor(( month + 9) / 12 )) / 4 )
                - Math.floor( 3 * (Math.floor(( year + ( month - 9 ) / 7)/100 + 1) / 4)
                + Math.floor( 275 * month / 9 ) + day + 1721028.5 )))
                + ( hour + minute / 60 + second / 3600 ) / 24 ) - 30.5;

        return JD;
    }

    public double julianDay (double year, double month, double day, double hour, double minute, double second)
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
        double l = 360.98564736629 * (julianDay - 2451545);
        double m = 0.000387933 * julianCentury * julianCentury;
        double n = julianCentury * julianCentury * julianCentury/ 38710000;
        double o = 280.46061837 + l  + m - n;


        return o  % 360;
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
     *  Calculate Greenwich Hour Angle (GHA) Minute using the GHA of Aries.
     *
     * @param ghaAries Double
     * @return         Double
     *
     */
    public double ghaAriesMinute(double ghaAries)
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
        double a =  gha + observationLongitude;

        if (360 < a)
            return a - 360;
        else if (0 > a)
            return a + 360;
        else
            return a;
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
        latitude = Math.toRadians(latitude);
        declination = Math.toRadians(declination);
        lha = Math.toRadians(lha);

        return Math.toDegrees(Math.asin(Math.sin(latitude) * Math.sin(declination) + Math.cos(latitude) * Math.cos(declination) * Math.cos(lha)));
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
        declination = Math.toRadians(declination);
        latitude    = Math.toRadians(latitude);
        lha         = Math.toRadians(lha);
        heightCalculated = Math.toRadians(heightCalculated);
        return Math.toDegrees( Math.asin(
                (Math.sin(declination) * Math.cos(latitude)
                - Math.cos(declination) * Math.sin(latitude) * Math.cos(lha))
                / Math.cos(heightCalculated)
        ) );
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
     * @param assumedLongitude   Double Latitude in decimal notation where (-) is South and (+) is North
     * @return                  Double Compass Bearing to the Celestial Body from the assumed position.
     */
    public double starBearingFromAssumedPosition( double starDeclination, double starSHA, double assumedLongitude, double assumedLatitude)
    {
        Calendar date = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT"));

        //(1)
        double year                = date.get(Calendar.YEAR);
        double month               = date.get(Calendar.MONTH ) + 1;
        double day                 = date.get(Calendar.DAY_OF_MONTH);
        double hour                = date.get(Calendar.HOUR_OF_DAY);
        double minute              = date.get(Calendar.MINUTE);
        double second              = date.get(Calendar.SECOND);
        double julianDay        = julianDay(year, month, day, hour, minute, second);
        double julianCentury    = julianCentury(julianDay);

        double ghaAries         = ghaAries(julianDay, julianCentury);

        double gha              = gha(ghaAries, starSHA);

        double lha              = lha( gha, assumedLongitude);

        double hc               = (heightCalculated(starDeclination,assumedLatitude,lha));

        hc = Math.toRadians(hc);

        double h = Math.acos((Math.sin(Math.toRadians(starDeclination)) -
                Math.sin(Math.toRadians(assumedLatitude)) * Math.sin(hc)) / (Math.cos(Math.toRadians(assumedLatitude)) * Math.cos(hc)));

        //double h = Math.atan(Math.sin(lha) / ( Math.sin(assumedLongitude) * Math.cos(lha) - Math.tan(starDeclination) * Math.cos(assumedLongitude) ));

        return Zn(assumedLatitude,lha, Math.toDegrees(h));
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
        if ( 0 < assumedLatitude ) // North
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
        // South
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
        double HsRad = Math.toRadians( sextantHeight   );
        double HsTan = Math.tan(HsRad) ;

        return  (0.96  / HsTan )  + sextantHeight;
    }

    /**
     *   Height Observed is More Plotted Towards
     *
     *  It means if the height observed (Ho) is more (Mo), the LOP is plotted toward (To) the object = HoMoTo.
     *
     * @param heightCalculated  double Height calculated of Celestial Body
     * @param heightObserved    double Sextant Height measured with corrections.
     * @param trueAzimuth       double The True Azimuth of the Celestial Body
     * @return double Bearing to Celestial Body
     */
    public double HoMoTo(double heightCalculated, double heightObserved, double trueAzimuth)
    {
        if (heightObserved > heightCalculated) {
            return trueAzimuth;
        }
        else {
            double Zn = trueAzimuth + 180;
            if (Zn > 360) return Zn - 360;
            return Zn;
        }
    }

    /**
     *
     *  Add a number of degrees to a bearing.
     *
     * @param bearing       Double A bearing in degrees
     * @param degreesAdded  Double Degrees to be added to the bearing
     * @return              Double new Bearing
     */
    public double addDegrees(double bearing, double degreesAdded)
    {
        double newBearing = bearing + degreesAdded;

        if (360 < newBearing)
        {
            newBearing = newBearing - 360;
        }
        else if (0 > newBearing)
        {
            newBearing = 360 - newBearing;
        }
        return  newBearing;
    }

    /**
     *
     * Calculate a new geographical position (latitude and longitude) from a known geographical position
     * using a distance in Nautical Miles (NM) and bearing to the new position.
     *
     * @param bearing       Double Bearing to the new geographical position.
     * @param distance      Double Distance in Nautical Miles to the new geographical position.
     * @param latitude      Double Starting Latitude.
     * @param longitude     Double Starting Longitude.
     * @return              GeoPosition Geographical Position object.
     */
    public GeoPosition newGeographicPosition( double bearing, double distance, double latitude, double longitude)
    {
        distance = distance / 3440;
        bearing = Math.toRadians(bearing);

        double lat1 = Math.toRadians(latitude), lon1 = Math.toRadians(longitude);
        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(distance) +
                Math.cos(lat1) * Math.sin(distance) * Math.cos(bearing));
        double lon2 = lon1 + Math.atan2(Math.sin(bearing) * Math.sin(distance) *
                        Math.cos(lat1),
                Math.cos(distance) - Math.sin(lat1) *
                        Math.sin(lat2));
        if (Double.isNaN(lat2) || Double.isNaN(lon2)) {
            return null;
        }

        return new GeoPosition(Math.toDegrees(lat2), Math.toDegrees(lon2));
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
