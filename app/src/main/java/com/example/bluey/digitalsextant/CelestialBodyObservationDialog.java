package com.example.bluey.digitalsextant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Bluey on 9/22/17.
 */
@SuppressLint("ValidFragment")
public class CelestialBodyObservationDialog extends DialogFragment
{
    protected CelestialBodyObservationFragment     celestialBodyObservationFragment;//communicates with the CelestialBodyObservationFragment


    public CelestialBodyObservationDialog(CelestialBodyObservationFragment frag)
    {
        super();
        this.celestialBodyObservationFragment = frag;
    }

    //creates the dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//creates the AlertDialog

        View myDialog = getActivity().getLayoutInflater().inflate(R.layout.celestial_body_fix_dialog, null);
        builder.setView(myDialog);//adds the GUI to dialog

        builder.setTitle("INSTRUCTIONS");//sets the title for the dialog

        //sets the positive button to OK
        //Then if the OK button is pushed does the following:
        //      1. the dialog exits
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int id)
            {
                dialogInterface.cancel();
            }
        });
        return builder.create();// creates the dialog
    }
}