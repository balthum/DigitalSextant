package com.example.bluey.digitalsextant;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by toddgibson Todd Gibson on 9/8/17.
 */

public class EstimatedPositionModule extends CelestialMath
{
    private Context                             context                   = null;           // Application Context

    public EstimatedPositionModule(Context context)
    {
        this.context = context;
        calculateEstimatedPosition();
    }

    private void calculateEstimatedPosition()
    {
        Double sextantHeight = 0.0;

        // 1.)
        ObservationDataManager observationDataManager = new ObservationDataManager(context);
        ArrayList<CelestialBodyObservation> celestialBodyObservations = new ArrayList<>(observationDataManager.getObservationFromDatabase());

        // 2.)
        PreviousPositionDataManager previousPositionDataManager = new PreviousPositionDataManager(context);
        ArrayList<PreviousPosition> previousPosition = previousPositionDataManager.getPositionFromDatabase();

        PreviousPosition assumedPosition = previousPosition.get(0);

        // 3.)
        for ( int i = 0; i < celestialBodyObservations.size(); i++)
        {
            // a.) add correction sextant Height
            sextantHeight = mainCorrection(celestialBodyObservations.get(i).getHeightObserver()) + sextantHeight;
        }
    }
}
