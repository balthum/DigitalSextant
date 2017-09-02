package com.example.bluey.digitalsextant;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by robinluna Robin Luna on 8/06/17.
 */
public class ObservationListPageFragment extends Fragment
{
    private View                                    view;
    private ListView                                listView;
    private ObservationAdapter                      observationAdapter;
    private ObservationDataManager                  observationDataManager;
    private ArrayList<CelestialBodyObservation>     arrayList;
    private int                                     position; // position of observation
    static final String                             PREFS_NAME = "MyPrefsFile1";

    /**
     *
     */
    public ObservationListPageFragment() {}

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.page_observation_list, container, false);

        //1.Creates a ObservationDataManager
        //2.Creates an arrayList where all the observations are stored and gets the current observations
        this.observationDataManager = new ObservationDataManager(getActivity());
        arrayList = new ArrayList<>(observationDataManager.getObservationFromDatabase());

        //Creates the observationAdapter so it displays all observation with the
        //name in the first row and the info in the second column
        this.observationAdapter = new ObservationAdapter(getActivity(),arrayList);

        //1. creates the listView and attach it to the ListView id
        //2. display the record in the ObservationAdapter to the ListView
        this.listView = (ListView) view.findViewById(R.id.listView_observation);
        this.listView.setAdapter(this.observationAdapter);

        //creates a warning dialog
        if(arrayList.size() == 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());//creates the AlertDialog
            builder.setTitle("WARNING");//sets the title for the dialog
            builder.setMessage("Push the ADD ICON to add an observation to the list.");//sets the message of the dialog
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {

                }
            });
            builder.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setView(view);
            builder.create();

                //builder.show();// creates the dialog
        }

        //when you select an observation from the ListView it sets the observation
        //as selected and gets the current position of the observation
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l)
            {
                v.setSelected(true);// tells the observation is selected
                position = i; //gets the position of the observation
            }
        });

        //there is a option menu
        setHasOptionsMenu(true);

        return view;
    }

    /**
     *
     * @param menu Menu
     * @param menuInflater MenuInflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
    {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.observation_list_menu,menu);
    }

    /**
     * When the add option icon is selected it replaces the current fragment
     * with the CelestialBodyObservationFragmentActivity, which goes to the camera
     * @param item MenuItem
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.add)
        {
            CelestialBodyObservationFragmentActivity celestialBodyObservationFragmentActivity =
                    new CelestialBodyObservationFragmentActivity();
            android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,celestialBodyObservationFragmentActivity);
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }
}