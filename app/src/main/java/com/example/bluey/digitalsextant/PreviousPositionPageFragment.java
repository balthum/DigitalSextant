package com.example.bluey.digitalsextant;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by robinluna RobinLuna on 8/26/17.
 */

public class PreviousPositionPageFragment extends Fragment
{
    private View                                view;//Occupies a rectangular area on the screen and is responsible for drawing and event handling
    private ListView                            list;//The list of the Positions
    private PreviousPositionDataManager         previousPositionDataManager;//The DataManger that stores and updates the Positions in the Database
    private PreviousPositionAdapter             previousPositionAdapter;// The Adapter for the list of the Positions
    private ArrayList<PreviousPosition>         positionArrayList;// The arrayList that has all the positions in it



    /**
     * Default Constructor PreviousPositionPageFragment.
     */
    public PreviousPositionPageFragment() {}


    /**
     * Gets the View of the Fragment of the ListView of Preferences which includes the following:<br/><br/>
     * (1) Inflate the layout for this fragment.<br/>
     * (2) Initializes the PreferenceDataManager and the ArrayList to where all the current Preference Objects.<br/>
     * (3) If the ArrayList size is 0 it creates the GPS and Previous Position Preference Objects.<br/>
     *     that is shown on the ListView and put into the ArrayList and Database.<br/>
     * (4) Creates the PreferenceAdapter so it displays all preferences with the name in the first row and the info in the second column.<br/>
     * (5) Initializes the ListView and displays the records in the ListView from the PreferenceAdapter.<br/>
     * (6) The Position List deletes old positions to have only the preference amount of positions.<br/>
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page_previous_position_list, container, false);//(1)

        //(2)
        this.previousPositionDataManager = new PreviousPositionDataManager(getActivity());
        positionArrayList = new ArrayList<>(previousPositionDataManager.getPositionFromDatabase());

        //(4)
        this.previousPositionAdapter = new PreviousPositionAdapter(getActivity(), positionArrayList);

        //(5)
        this.list = (ListView) view.findViewById(R.id.listView_previous_position);
        this.list.setAdapter(this.previousPositionAdapter);

        //(6)
        updatePositions();

        return view;
    }

    /**
     * Shows the amount of positions in the arraylist based on the preferences<br/><br/>
     *
     * (1) Initializes the PreferenceDataManager and the ArrayList to where all the current Preference Objects.<br/>
     * (2) Declares the preference object.<br/>
     * (3) Gets the array of how many positions are suppose to be in the Previous Position List and initializes it to numPreference.<br/>
     * (4) Sets the number of positions are in the arrayList to numPosition.<br/>
     * (5) If there are more positions in the position arrayList, than what is said to be by the preferences. The oldest positions are deleted until
     *  the preference amount equal the amount of positions in the position arrayList.
     */
    public void updatePositions()
    {
        //(1)
        PreferenceDataManager preferenceDataManager = new PreferenceDataManager(getActivity());
        ArrayList<Preference> preferenceArrayList = new ArrayList<>(preferenceDataManager.getPreferenceFromDatabase());

        //(2)
        Preference preference;

        //(3)
        preference = preferenceArrayList.get(1);
        int numPreference = preference.getPreferenceNum();

        //(4)
        int numPosition = positionArrayList.size();

        //(5)
        if(numPosition > numPreference)
        {
            while(numPreference <= numPosition)
            {
                positionArrayList.remove((positionArrayList.size()) - 1);
                numPosition--;
            }

            previousPositionDataManager.updatePositionDatabase(positionArrayList);
            list.setAdapter(previousPositionAdapter);
        }
    }
}