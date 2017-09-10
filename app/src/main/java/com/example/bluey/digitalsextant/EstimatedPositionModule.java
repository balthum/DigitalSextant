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
        Double sextantHeight = 0.0;

        // 3.)
        for ( int i = 0; i < celestialBodyObservations.size(); i++)
        {
            // a.) add correction sextant Height
            sextantHeight = mainCorrection(celestialBodyObservations.get(i).getHeightObserver()) + sextantHeight;
        }
    }

    private PreviousPosition caclculateEsstimatedPositionThreeCircle()
    {
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

    private PreviousPosition getLastKnowPosition()
    {
        PreviousPositionDataManager previousPositionDataManager = new PreviousPositionDataManager(context);
        ArrayList<PreviousPosition> previousPosition = previousPositionDataManager.getPositionFromDatabase();
        return previousPosition.get(0);
    }

}
