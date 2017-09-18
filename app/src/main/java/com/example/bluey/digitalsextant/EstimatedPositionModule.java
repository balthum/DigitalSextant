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
            double Ho  = heighObserved( celestialBodyObservation.getHeightObserver() );


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

            }
        }  //END for Loop


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



}
