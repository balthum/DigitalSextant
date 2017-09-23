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
 * Created by robinluna Robin Luna on 8/22/17.
 */
@SuppressLint("ValidFragment")
public class PreferenceDialog extends DialogFragment
{
    private String                      info;//the info of the preference info
    private int                         num; //the num of the preference num
    private int                         oldPosition;
    private Preference                  preference;//So the records can be updated
    private PreferencePageFragment      prefFragment;//communicates with the PreferencePageFragment

    /**
     * The Default constructor for PreferenceDialog which initializes the following
     *
     * (1) adds the Preference Object so the record info can be edited
     * (2) adds the prefFrag so it can communicate where the info is going
     * @param prefFragment PreferencePageFragment
     * @param preference Preference
     */
    public PreferenceDialog(PreferencePageFragment prefFragment, Preference preference)
    {
        this.preference = preference;//(1)
        this.prefFragment = prefFragment;//(2)
    }

    //creates the dialog

    /**
     * Creates the dialog for the PreferencePageFragment
     *
     * (1) Initializes the AlertDialog with a Builder
     * (2) When GPS preference is selected
     *      (a) Sets the title message as GPS SENSOR UPDATES
     *      (b) Sets the radio buttons with array of the GPS multiple choice and when item clicked sets the item
     * (3) When previous position preference is selected
     *      (a) Sets the title message as TOTAL SAVED POSITIONS
     *      (b) Sets the radio buttons with array of the Previous Position multiple choice and when item clicked sets the item
     * (4) When the ok button is selected assigns info to the Preference object,updates the record by
     *    sending the record to be updated in PreferencePageFragment, and shows a toast message for updating the record
     * (5) When the cancle button is selected sends a toast message saying no record updated
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

        //(2)
        if(position == 0)
        {
            //(a)
            builder.setTitle("GPS SENSOR UPDATES");//sets title to dialog

            if(preference.PreferenceNum == 30)
                oldPosition = 0;
            if(preference.PreferenceNum == 45)
                oldPosition = 1;
            if(preference.PreferenceNum == 60)
                oldPosition = 2;
            if(preference.PreferenceNum == 75)
                oldPosition = 3;
            if(preference.PreferenceNum == 90)
                oldPosition = 4;


            //(b)
            final String[] gps =getResources().getStringArray(R.array.gps_radioButton);
            builder.setSingleChoiceItems(R.array.gps_radioButton, oldPosition, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    info = gps[i];
                    if(info.equals( "30 minutes"))
                        num = 30;
                    else if(info.equals( "45 minutes"))
                        num = 45;
                    else if (info.equals( "60 minutes"))
                        num = 60;
                    else if (info.equals( "75 minutes"))
                        num = 75;
                    else if (info.equals( "90 minutes"))
                        num = 90;
                }
            });
        }

        //(3)
        if(position == 1)
        {
            //(a)
            builder.setTitle("TOTAL PREVIOUS POSITIONS");

            if(preference.PreferenceNum == 5)
                oldPosition = 0;
            if(preference.PreferenceNum == 10)
                oldPosition = 1;
            if(preference.PreferenceNum == 15)
                oldPosition = 2;
            if(preference.PreferenceNum == 20)
                oldPosition = 3;
            if(preference.PreferenceNum == 25)
                oldPosition = 4;

            //(b)
            final String[] savedPosition =getResources().getStringArray(R.array.position_raidoButton);
            builder.setSingleChoiceItems(R.array.position_raidoButton, oldPosition, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    info = savedPosition[i];
                    if (info.equals( "5 positions"))
                        num = 5;
                    else if(info.equals( "10 positions"))
                        num = 10;
                    else if(info.equals( "15 positions"))
                        num = 15;
                    else if (info.equals( "20 positions"))
                        num = 20;
                    else if(info.equals( "25 positions"))
                        num = 25;
                }
            });
        }

        //(4)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(preference.PreferenceNum != num)
                {


                    preference.PreferenceInfo = info;
                    preference.PreferenceNum = num;

                    PreferenceDialog.this.prefFragment.updatePreference(preference);

                    Toast.makeText(getActivity(), "PREFERENCES UPDATED",Toast.LENGTH_SHORT).show();

                    if(position == 0)
                        ((MainActivity)prefFragment.getActivity()).resetTimer();
                }

            }
        });

        //(5)
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Toast.makeText(getActivity(), "NO PREFERENCES UPDATED", Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();//(6)
    }
}
