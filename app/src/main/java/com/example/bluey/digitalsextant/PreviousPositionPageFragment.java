package com.example.bluey.digitalsextant;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by robinluna RobinLuna on 8/26/17.
 */

public class PreviousPositionPageFragment extends Fragment
{
    private View                                view;
    private ListView                            list;
    private PreviousPositionDataManager         previousPositionDataManager;
    private PreviousPositionAdapter             previousPositionAdapter;
    private ArrayList<PreviousPosition>         arrayList;
    public static int                           position;
    private PreviousPosition                    previousPosition;



    /**
     * Default Constructor PreviousPositionPageFragment
     */
    public PreviousPositionPageFragment() {}


    /**
     * Gets the View of the Fragment of the ListView of Preferences which includes the following
     * (1) Inflate the layout for this fragment
     * (2) Initializes the PreferenceDataManager and the ArrayList to where all the current Preference Objects
     * (3) If the ArrayList size is 0 it creates the GPS and Previous Position Preference Objects
     *     that is shown on the ListView and put into the ArrayList and Database.
     * (4) Creates the PreferenceAdapter so it displays all preferences with the name in the first row and the info in the second column
     * (5) Initializes the ListView and displays the records in the ListView from the PreferenceAdapter
     * (6) when you select an observation from the ListView it gets the current position of the preferences and gets the dialog
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
        view = inflater.inflate(R.layout.page_preference, container, false);//(1)

//        //(2)
//        this.previousPositionDataManager = new PreviousPositionDataManager(getActivity());
//        arrayList = new ArrayList<>(previousPositionDataManager.getPositionFromDatabase());
//
//
//        //(4)
//        this.previousPositionAdapter = new PreviousPositionAdapter(getActivity(), arrayList);
//
//        //(5)
//        this.list = (ListView) view.findViewById(R.id.listView_previous_position);
//        this.list.setAdapter(this.previousPositionAdapter);
//
//        //(6)
//        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
//                position = i; //gets the position of the preferences
//            }
//        });
        return view;
    }
}