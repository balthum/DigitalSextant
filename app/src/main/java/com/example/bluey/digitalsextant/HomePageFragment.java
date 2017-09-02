package com.example.bluey.digitalsextant;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by robinluna Robin Luna on 8/06/17.
 */

public class HomePageFragment extends Fragment
{

    private Button              observationButton;// Display the ObservationListPageFragment to get observations
    private View                myView;//the View of the Fragment

    /**
     * Default Constructor HomePageFragment
     */
    public HomePageFragment() {}

    /**
     * Gets the View of the Fragment
     *
     * (1)Inflate the layout for the HomePageFragment
     * (2)when button is pushed goes to the ObservationListPageFragment
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.page_home, container, false);//(1)
        initiateButton();//(2)
        return myView;
    }

    /**
     * Initializes a button and when clicked it goes to the ObservationListPageFragment by the following
     *
     * (1) Initializes the ObservationListPageFragment object
     * (2)Starts the FragmentTransaction by returning the FragmentManager for interaction with
     *    fragments associated with the current fragment's activity.
     * (3)replaces the current fragment with the ObservationListFragment
     * (4)processes the FragmentTransaction
     * (5) sets the Observation List highlighted on the Navigation View
     * (6) sets the toolbar Title with Observation List
     */
    public void initiateButton()
    {
        observationButton = (Button) myView.findViewById(R.id.cesBodyCamViewButton);
        observationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ObservationListPageFragment observationListFragment = new ObservationListPageFragment();//(1)
                android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();//(2)
                fragmentTransaction.replace(R.id.fragment_container,observationListFragment);//(3)
                fragmentTransaction.commit();//(4)

                //(5)
                MainActivity.navigationView.setCheckedItem(R.id.observation_list);

                //(6)
                MainActivity.toolbar.setTitle("Observation List");
            }
        });
    }
}
