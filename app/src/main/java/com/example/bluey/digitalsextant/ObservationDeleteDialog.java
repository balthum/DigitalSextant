package com.example.bluey.digitalsextant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by Bluey on 9/8/17.
 */

@SuppressLint("ValidFragment")
public class ObservationDeleteDialog extends DialogFragment
{
    protected ObservationListPageFragment     obsFragment;//communicates with the ObservationListPageFragment


    public ObservationDeleteDialog(ObservationListPageFragment frag)
    {
        super();
        this.obsFragment = frag;
    }

    //creates the dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//creates the AlertDialog
        builder.setTitle("CONFIRMATION");//sets the title for the dialog
        builder.setMessage("Confirm deletion of the selected Observation item");//sets the message of the dialog

        //sets the positive button to YES
        //Then if the YES button is pushed does the following:
        //      1. deletes the Record
        //      2. message is displayed saying record is deleted
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int id)
            {

                //deletes the record by sending the record to be deleted in the MainFragment
                ObservationDeleteDialog.this.obsFragment.deleteObservation();
                //shows a message saying Record was deleted
                Toast.makeText(getActivity(), "OBSERVATION DELETED", Toast.LENGTH_LONG).show();

            }
        });

        //Sets the negative button to NO
        //Then if the NO button is pushed does the following:
        //      1. message is displayed saying no record has been deleted
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int id)
            {

                //shows a message saying Record was't deleted
                Toast.makeText(getActivity(), "NO OBSERVATION DELETED", Toast.LENGTH_LONG).show();
            }
        });

        return builder.create();// creates the dialog
    }



}