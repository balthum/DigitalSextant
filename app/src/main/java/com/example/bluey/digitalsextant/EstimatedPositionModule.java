
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

        calculateEstimatedPosition();
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
     *      i. Calculate the intercept distance (ITC) in nautical miles (Degrees * 60). <BR>
     *      j. Calculate True azimuth (Zn). <BR>
     *      k. Calculate HoMoTo (ZnHoMoTo). <BR>
     *      l. Set Line Of Position assumed position. <BR>
     *      m. Set Line of Bearing to the Celestial Body. <BR>
     *      n. Set Intercept in nautical miles to the Line of Position. <BR>
     *      o. Find the Line of Position intercept with the Line of Bearing using ZnHoMoTo, ITC and assumed position. <BR>
     *      p. Create the Line Of Position. <BR>
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
            double ITC      = intercept(Hc, Ho) * 60; // Times 60 to convert to Nautical Miles.
            // j.
            double Zn       = Zn(assumedPosition.getLatitude(), lhaStar, Z);
            // k.
            double ZnHoMoTo = HoMoTo(Hc, Ho, Zn);
            // l.
            lineOfPositions[i].setAssumedLocationPosition(
                    new GeoPosition( assumedPosition.getLatitude(), assumedPosition.getLongitude() )
            );
            // m.
            lineOfPositions[i].setLineOfBearing( ZnHoMoTo );
            // n.
            lineOfPositions[i].setInterceptDistanceNauticalMiles( ITC );

            // o.
            lineOfPositions[i].setLopLocationPosition(
                    newGeographicPosition(
                            ZnHoMoTo, ITC, assumedPosition.getLatitude(), assumedPosition.getLongitude()
                    ));
            // p.1
            double newBearing = addDegrees(ZnHoMoTo, 90);
            lineOfPositions[i].setPositionOne(
                    newGeographicPosition(
                            newBearing,
                            50,
                            lineOfPositions[i].getLopLocationPosition().getLatitude(),
                            lineOfPositions[i].getLopLocationPosition().getLongitude()
                    ));
            // p.2
            newBearing = addDegrees(ZnHoMoTo, -90);
            lineOfPositions[i].setPositionTwo(
                    newGeographicPosition(
                            newBearing,
                            50,
                            lineOfPositions[i].getLopLocationPosition().getLatitude(),
                            lineOfPositions[i].getLopLocationPosition().getLongitude()
                    ));
        } //END for Loop
        // 4.) TODO Intercept Calculation



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



}

