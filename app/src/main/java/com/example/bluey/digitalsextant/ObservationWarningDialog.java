package com.example.bluey.digitalsextant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import static com.example.bluey.digitalsextant.PreferencePageFragment.position;

/**
 * Created by Bluey on 9/4/17.
 */

@SuppressLint("ValidFragment")
public class ObservationWarningDialog extends DialogFragment
{
    private int                             num; //the num of the preference num
    protected ObservationListPageFragment     obsFragment;//communicates with the ObservationListPageFragment
    private String                            obsInfo;

    /**
     * The Default constructor for PreferenceDialog which initializes the following
     *
     * (1) adds the obsFragment so it can communicate where the info is going
     * @param obsFragment ObservationListPageFragment
     */
    public ObservationWarningDialog(ObservationListPageFragment obsFragment, String info)
    {
        this.obsInfo = info;
        this.obsFragment = obsFragment;//(1)
    }



    //creates the dialog

    /**
     * Creates the dialog for the PreferencePageFragment
     *
     * (1) Initializes the AlertDialog with a Builder
     * (2) sets the title for the dialog
     * (3) sets the message of the dialog
     * (4) Sets the OK button
     * (5) Sets the CANCEL button
     * (6)Creates the dialog
     * @param savedInstanceState Bundle
     * @return Dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        //(1)
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//creates the AlertDialog

        builder.setTitle("WARNING");//(2)
        //builder.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
        builder.setIcon(R.drawable.ic_warning);


        if(obsInfo == "firstTime")
            builder.setMessage("Push the ADD ICON to add an observation to the list.");//(3)

        else if (obsInfo == "add")
            builder.setMessage("NOT allowed to ADD any more Observations.");//(3)

        else if (obsInfo == "delete")
            builder.setMessage("NO observations left to DELETE or NO observations were selected to DELETE.");//(3)


        //(4)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        return builder.create();//(6)
    }
}
