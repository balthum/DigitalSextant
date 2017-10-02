package com.example.bluey.digitalsextant;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by robinluna Robin Luna on 8/06/17.
 */

@SuppressLint("ValidFragment")
public class HomePageFragment extends Fragment
{

    private Button                          observationButton;// Display the ObservationListPageFragment to get observations
    private View                            myView;//the View of the Fragment
    private EditText                        lastLatitude, lastLongitude, currentLatitude, currentLongitude;
    private PreviousPosition                position;
    private PreviousPositionDataManager     previousPositionDataManager;
    private ArrayList<PreviousPosition>     arrayList;
    private boolean                         iscalculateButtonPushed = false;
    private double                          estLat;
    private double                          estLong;
    private String                          estLatDirection;
    private String                          estLongDirection;

    /**
     * Default Constructor HomePageFragment
     */
    public HomePageFragment(boolean iscalculateButtonPushed)
    {
        this.iscalculateButtonPushed = iscalculateButtonPushed;
    }

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

        lastLatitude = (EditText) myView.findViewById(R.id.editTextLatitude);
        lastLongitude = (EditText) myView.findViewById(R.id.editTextLongitude);
        currentLatitude = (EditText) myView.findViewById(R.id.editTextPositionLat);
        currentLongitude = (EditText) myView.findViewById(R.id.editTextPositionLong);

        lastPosition();

        initiateButton();//(2)

        if(iscalculateButtonPushed)
        {
            String latitude = getLatLong(estLat,estLatDirection);
            String longitude = getLatLong(estLong, estLongDirection);

            currentLatitude.setBackgroundColor(Color.TRANSPARENT);
            currentLongitude.setBackgroundColor(Color.TRANSPARENT);

            currentLatitude.setText(latitude);
            currentLongitude.setText(longitude);
        }
        else
        {}

        return myView;
    }

    public void lastPosition()
    {
        this.previousPositionDataManager = new PreviousPositionDataManager(getActivity());
        this.arrayList = new ArrayList<>(previousPositionDataManager.getPositionFromDatabase());

        if (arrayList.size() != 0)
        {
            this.position = arrayList.get(0);

            lastLatitude.setBackgroundColor(Color.TRANSPARENT);
            lastLongitude.setBackgroundColor(Color.TRANSPARENT);

            lastLatitude.setText(position.getLatitudeString());
            lastLongitude.setText(position.getLongitudeString());

            estLat = position.getLatitude() + 0.0085;
            estLong = position.getLongitude() - 0.009;
            estLatDirection = position.getLatitudeDirection();
            estLongDirection = position.getLongitudeDirection();
        }
        else
        {}


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

    public String getLatLong(double num, String latLongDirection)
    {
        int degree = Math.abs((int)num);
        double decimalMinute = ((Math.abs(num) - degree) * 60);

        return String.format("%dº %.2f' %s",degree, decimalMinute, latLongDirection);
    }
}
