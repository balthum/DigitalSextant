package com.example.bluey.digitalsextant;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by toddgibson Todd Gibson on 9/8/17.
 */

public class EstimatedPositionModule extends CelestialMath
{
    private Context                             context                   = null;           // Application Context
    private ArrayList<CelestialBodyObservation> celestialBodyObservations = null;           // Array List of Celestial Observations
    private PreviousPosition                    assumedPosition           = null;           // Assumed Position

    public EstimatedPositionModule(Context context)
    {
        this.context = context;
        celestialBodyObservations = getCelestialObservations();
        assumedPosition = getLastKnowPosition();

        //calculateEstimatedPosition();
    }

    /**
     *
     *
     *  1.) Get the currently assumed position for the Past Position Data Base <BR>
     *  2.) Create an array of the Line of Position Objects equal to the number of celestial Body Observations. <BR>
     *  3.) Loop through all the celestial bodies to create each Line Of Position <BR>
     *
     *      a. Get a celestial observation. <BR>
     *      b. Get a celestial body that the observation was taken for. <BR>
     *      c. Make atmospheric correction to the Sextant Height observed.<BR>
     *      d. Calculate GHA of Aries.<BR>
     *      e. Calculate the GHA of the celestial body. <BR>
     *      f. Calculate the LHA of the celestial body. <BR>
     *      g. Calculate the Star Height (Hc). <BR>
     *      h. Calculate azimuth (Z). <BR>
     *      i. Calculate the intercept distance (ITC). <BR>
     *      j. Calculate True azimuth (Zn). <BR>
     *      k. Set Line Of Position assumed position. <BR>
     *      l. Set Line of Bearing to the Celestial Body. <BR>
     *      m. Set Intercept in nautical miles to the Line of Position. <BR>
     *      n. Find the Line of Position intercept with the Line of Bearing using Zn, ITC and assumed position. <BR>
     *      o. Create the Line Of Position. <BR>
     *          1. Add 90 Degrees to Zn and find the latitude and longitude 50 NM of the ITC. <BR>
     *          2. Subtract 90 Degrees to Zn and find the latitude and longitude 50 NM of the ITC. <BR>
     *
     */
    public void calculateEstimatedPosition()
    {

        // 1.)
        PreviousPosition assumedPosition = getLastKnowPosition();
        // 2.)
        LineOfPosition[] lineOfPositions = new LineOfPosition[celestialBodyObservations.size()];
        // 3.)
        for ( int i = 0; i < celestialBodyObservations.size(); i++)
        {
            // a.
            CelestialBodyObservation celestialBodyObservation = celestialBodyObservations.get(i);
            // b. Get the almanac information for the Celestial Body
            CelestialBody celestialBody = new CelestialBodyDatabaseManager(context).getCelestialBody(
                    celestialBodyObservation.CelestialBodyName );
            // c.
            double Ho       = observedHeight( celestialBodyObservation.getHeightObserver() );
            // d.
            double ghaAries = ghaAries(celestialBodyObservation);
            // e.
            double ghaStar  = gha( ghaAries, celestialBody.getSiderealHourAngle() );
            // f.
            double lhaStar  = lha( ghaStar,assumedPosition.getLongitude() );
            // g.
            double Hc       = heightCalculated(
                    celestialBody.getDeclination(), assumedPosition.getLatitude(), lhaStar );
            // h.
            double Z        = azimuth(
                    celestialBody.getDeclination(), assumedPosition.getLatitude(),lhaStar, Hc
                    );
            // i.
            double ITC = intercept(Hc, Ho);
            // j.
            double Zn  = Zn(assumedPosition.getLatitude(), lhaStar, Z);
            // k.
            lineOfPositions[i].setAssumedLocationPosition(
                    new GeoPosition( assumedPosition.getLatitude(), assumedPosition.getLongitude() )
            );
            // l.
            lineOfPositions[i].setLineOfBearing( Zn );
            // m.
            lineOfPositions[i].setInterceptDistanceNauticalMiles( ITC );

            // n.
            lineOfPositions[i].setLopLocationPosition(
                    newGeographicPosition(ITC, Zn, assumedPosition.getLatitude(), assumedPosition.getLongitude() )
            );
            // o.1
            double newBearing = addDegrees(Zn, 90);
            lineOfPositions[i].setPositionOne(
                    newGeographicPosition(
                            50,
                            newBearing,
                            lineOfPositions[i].getLopLocationPosition().getLatitude(),
                            lineOfPositions[i].getLopLocationPosition().getLongitude()
                    ));
            // o.2
            newBearing = addDegrees(Zn, -90);
            lineOfPositions[i].setPositionOne(
                    newGeographicPosition(
                            50,
                            newBearing,
                            lineOfPositions[i].getLopLocationPosition().getLatitude(),
                            lineOfPositions[i].getLopLocationPosition().getLongitude()
                    ));
        } //END for Loop
        // 4.)



    }


/*    private PreviousPosition caclculateEsstimatedPositionCircle()
    {
        // 1.)
        CelestialBodyDatabaseManager        celestialBodyDatabaseManager = new CelestialBodyDatabaseManager(context);
        CelestialBody                       celestialBody                = null;
        ArrayList<CircleOfEqualAltitude>    circleOfEqualAltitudes       = new ArrayList<>();
        PreviousPosition                    assumedPosition              =  getLastKnowPosition();

        // 2.)
        for ( int i = 0; i < celestialBodyObservations.size(); i++)
        {
            // a.)
            CircleOfEqualAltitude  circleOfEqualAltitude = new CircleOfEqualAltitude();
            // b.)
            circleOfEqualAltitude.setName( celestialBodyObservations.get(i).CelestialBodyName );
            // c.)
            celestialBody = celestialBodyDatabaseManager
                    .getCelestialBody( circleOfEqualAltitude.getName() );
            // d.)
            circleOfEqualAltitude.setLatitude (  celestialBody.getDeclination() );
            circleOfEqualAltitude.setLongitude( ghaStar( celestialBody.getSiderealHourAngle(),
                    celestialBodyObservations.get(i) ));
            // e.)
            circleOfEqualAltitude.setRadius( 60 * starZenith(assumedPosition.Latitude, celestialBodyObservations.get(i), celestialBody) );
            // f.)
            circleOfEqualAltitudes.add(circleOfEqualAltitude);
        }
        // 3.)

        return new PreviousPosition();
    }*/

    /*  Supporting Methods */

    /**
     *  Get an Array List of the Celestial Body Observation Objects
     * @return ArrayList of CelestialBodyObservation
     */
    private ArrayList<CelestialBodyObservation> getCelestialObservations()
    {
        ObservationDataManager observationDataManager = new ObservationDataManager(context);
        return observationDataManager.getObservationFromDatabase();
    }

    /**
     *
     *  Get the last know position from Past Position Database
     *
     * @return PreviousPosition Object
     */
    private PreviousPosition getLastKnowPosition()
    {
        PreviousPositionDataManager previousPositionDataManager = new PreviousPositionDataManager(context);
        ArrayList<PreviousPosition> previousPosition = previousPositionDataManager.getPositionFromDatabase();
        return previousPosition.get(0);
    }

    private GeoPosition newGeographicPosition(double nauticalMiles, double bearing, double latitude, double longitude)
    {
        GeoPosition geoPosition = new GeoPosition(0.0, 0.0);

        double dist         = nauticalMiles / 3440;
        double brng         = Math.toRadians(bearing);
        double lat1         = Math.toRadians(latitude);
        double lon1         = Math.toRadians(longitude);

        double lat2 = Math.asin( Math.sin(lat1) * Math.cos(dist) + Math.cos(lat1) * Math.sin(dist) * Math.cos(brng) );
        double a = Math.atan2(Math.sin(brng) * Math.sin(dist) * Math.cos(lat1), Math.cos(dist) - Math.sin(lat1) * Math.sin(lat2));

        System.out.println("a = " +  a);
        double lon2 = lon1 + a;

        lon2 = (lon2 + 3 * Math.PI ) % ( 2 * Math.PI ) - Math.PI;

        geoPosition.setLatitude(lat2);
        geoPosition.setLongitude(lon2);

        return geoPosition;
    }


}
