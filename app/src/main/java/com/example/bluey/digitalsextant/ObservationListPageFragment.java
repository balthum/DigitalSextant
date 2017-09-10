package com.example.bluey.digitalsextant;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
    private Button                                  calculateButton;
    private int                                     position = -1; // position of observation

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

        ////when item is selected the item is highlighted
        this.listView.setSelector(R.color.selection_color);

        this.calculateButton = (Button) view.findViewById(R.id.calculate_button);

        //When you select an item from the ListView it sets the item as
        //selected and gets the current position of the item
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                view.setSelected(true);//tells item is selected
                position = i;//gets position of item
            }
        });

        if(arrayList.size() >= 2)
        {
            this.calculateButton.setEnabled(true);
        }
        else
        {
            this.calculateButton.setEnabled(false);
        }

        this.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //getContext().deleteDatabase(ObservationDatabase.DATABASE_OBSERVATION);
            }
        });

        //creates a warning dialog
        if(arrayList.size() == 0)
        {
            ObservationWarningDialog myDialog = new ObservationWarningDialog(this,"firstTime");
            myDialog.show(getFragmentManager(), "warning");
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
            if(arrayList.size() == 3)
            {
                ObservationWarningDialog myDialog = new ObservationWarningDialog(this, "add");
                myDialog.show(getFragmentManager(), "warning");
            }
            else
            {
                CelestialBodyObservationFragment celestialBodyObservationFragment = new CelestialBodyObservationFragment();
                android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, celestialBodyObservationFragment);
                fragmentTransaction.commit();
            }
        }

        if(item.getItemId() == R.id.delete)
        {
            if (position != -1)
            {
                ObservationDeleteDialog deleteDialog = new ObservationDeleteDialog(this);
                deleteDialog.show(getFragmentManager(), "delete observation");
            }
            else
            {
                ObservationWarningDialog myDialog = new ObservationWarningDialog(this, "delete");
                myDialog.show(getFragmentManager(), "warning");
            }
        }

        if(item.getItemId() == R.id.clear_all)
        {

        }

        return super.onOptionsItemSelected(item);
    }

    //delet observation
    public void deleteObservation()
    {
        int deletePosition = this.position;
        int oldArraySize = this.arrayList.size();
        arrayList.remove(this.position);// removes record from arrayList
        this.observationDataManager.updateObservationDatabase(this.arrayList);//deletes record from Database

        if(deletePosition != (oldArraySize -1))
        {
            while(deletePosition < this.arrayList.size())
            {
                CelestialBodyObservation celestialBodyObservation = this.arrayList.get(deletePosition);
                celestialBodyObservation.setTitle("Observation " + (deletePosition + 1));
                this.arrayList.set(deletePosition, celestialBodyObservation);

                 deletePosition++;
            }
            this.observationDataManager.updateObservationDatabase(this.arrayList);
        }

        this.listView.setAdapter(this.observationAdapter);//shows the record deleted in the listView

        if(arrayList.size() < 2)
        {
            this.calculateButton.setEnabled(false);
        }
         this.position = -1;

    }
}