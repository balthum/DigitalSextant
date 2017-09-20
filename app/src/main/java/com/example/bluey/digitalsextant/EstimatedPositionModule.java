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

    private void calculateEstimatedPosition()
    {
        LineOfPosition[] lineOfPositions = new LineOfPosition[celestialBodyObservations.size()];
        for ( int i = 0; i < celestialBodyObservations.size(); i++)
        {
            // a.) Get the assumed position for the Previous Position Database.
            PreviousPosition                  assumedPosition = getLastKnowPosition();
            // b.) Get the observation for the Celestial Body observed.
            CelestialBodyObservation celestialBodyObservation = celestialBodyObservations.get(i);
            // c.) Get the almanac information for the Celestial Body
            CelestialBody                       celestialBody = new CelestialBodyDatabaseManager(context)
                    .getCelestialBody( celestialBodyObservation.CelestialBodyName);
            // d.) Make correction the Sextant Height
            double Ho  = observedHeight( celestialBodyObservation.getHeightObserver() );


            // e.) Calculate azimuth
            double ghaAries = ghaAries(celestialBodyObservation);
            double ghaStar  = gha(ghaAries, celestialBody.getSiderealHourAngle());

            double lhaStar  = lha( ghaStar,assumedPosition.getLongitude() );

            double hcStar   = heightCalculated(
                    celestialBody.getDeclination(), assumedPosition.getLatitude(), lhaStar );
            double azimuth  = azimuth(
                    celestialBody.getDeclination(), assumedPosition.getLatitude(),lhaStar, hcStar
                    );
            // f.) Calculate the intercept distance
            double ITC = intercept(hcStar, Ho);
            // g.) Calculate Zn
            double Zn  = Zn(assumedPosition.getLatitude(), lhaStar, azimuth);

            lineOfPositions[i].setAssumedLocationPosition(
                    new GeoPosition( assumedPosition.getLatitude(), assumedPosition.getLongitude() )
            );

            lineOfPositions[i].setBearingToLOP( Zn );
            lineOfPositions[i].setInterceptDistanceNauticalMiles( ITC );
            lineOfPositions[i].setLopLocationPosition(
                    newGeographicPosition(ITC, Zn, assumedPosition.getLatitude(), assumedPosition.getLongitude() )
            );

        } //END for Loop


    }


    private PreviousPosition caclculateEsstimatedPositionCircle()
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
    }

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
