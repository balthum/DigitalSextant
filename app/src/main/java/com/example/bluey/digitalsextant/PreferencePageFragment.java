package com.example.bluey.digitalsextant;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.database.DatabaseUtilsCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by robinluna Robin Luna on 8/06/17.
 */
public class PreferencePageFragment extends Fragment
{
    private View                        view;
    private ListView                    list;
    private PreferenceDataManager       preferenceDataManager;
    private PreferenceAdapter           preferenceAdapter;
    private ArrayList<Preference>       arrayList;
    public static int                   position;
    private Preference                  preference;
    private PreferenceDatabase          preferenceDatabase;
    private SQLiteDatabase              sqLiteDatabase;
    private static final String         PREFERENCE_QUERY = "SELECT * FROM " + PreferenceDatabase.TABLE_PREFERENCE + " ORDER BY ID ";


    /**
     * Default Constructor PreferencePageFragment
     */
    public PreferencePageFragment() {}

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
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.page_preference, container, false);//(1)

        //(2)
        this.preferenceDataManager = new PreferenceDataManager(getActivity());
        arrayList = new ArrayList<>(preferenceDataManager.getPreferenceFromDatabase());

        //(3)
        if(arrayList.size() == 0)
        {
            //creates the gps preference
            this.preference = new Preference();

            //sets gps data and adds it to the array list and listview
            preference.setPreferenceName("GPS");
            preference.setPreferenceInfo("30 minutes");
            preference.setPreferenceNum(30);
            this.arrayList.add(preference);

            //creates the previous positions preference
            this.preference = new Preference();

            //sets previous position data
            preference.setPreferenceName("Previous Positions");
            preference.setPreferenceInfo("30 records");
            preference.setPreferenceNum(30);

            //adds gps and position data
            this.arrayList.add(preference);// adds record to ArrayList
            this.preferenceDataManager.updatePreferenceDatabase(this.arrayList);//updates preferenceDatabase for record added
        }

        //(4)
        this.preferenceAdapter = new PreferenceAdapter(getActivity(),arrayList);

        //(5)
        this.list = (ListView) view.findViewById(R.id.listView_preference);
        this.list.setAdapter(this.preferenceAdapter);

        //(6)
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l)
            {
                position = i; //gets the position of the preferences
                showDialog();
            }
        });
        return view;
    }

    /**
     * updates the records in the database, the ArrayList, and ListView by the following
     * (1) updates record to ArrayList
     * (2) updates Database for record updated
     * (3) shows the record updated to the listView
     * @param preference Preference
     */
    public void updatePreference(Preference preference)
    {
        this.arrayList.set(this.position, preference);//(1)
        this.preferenceDataManager.updatePreferenceDatabase(this.arrayList);//(2)
        this.list.setAdapter(this.preferenceAdapter);//(3)
    }


    /**
     * shows the dialog from the selected item by the following
     * (1) gets the Preference object at a certain position
     * (2) Initalizes the PreferenceDialog which passes in the Preference object
     * (3) Display the dialog, adding the fragment to the given FragmentManager.
     */
    public void showDialog()
    {
        preference = arrayList.get(position);//(1)
        PreferenceDialog myDialog = new PreferenceDialog(this,preference);//(2)
        myDialog.show(getFragmentManager(), "update_record");//(3)
    }


}