package com.example.bluey.digitalsextant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by robinluna Robin Luna on 8/18/2017.
 */

public class ObservationAdapter extends BaseAdapter
{
    private ArrayList<CelestialBodyObservation>         arrayList; //is an ArrayList that stores CelestialBodyObservation
    private LayoutInflater                              layoutInflater;//is LayoutInflater to get obtain a layout XML file

    /**
     * The Default constructor for ObservationAdapter which initializes the following
     * (1)An ArrayList that passes CelestialBodyObservation in the arrayList
     * (2)Obtains the LayoutInflater from the given context
     * @param context Context
     * @param arrayList ArrayList<CelestialBodyObservation>
     */
    public ObservationAdapter(Context context, ArrayList<CelestialBodyObservation> arrayList)
    {
        this.arrayList = arrayList;//(1)
        this.layoutInflater = LayoutInflater.from(context);//(2)
    }

    /**
     * //Gets the size of the ArrayList
     * @return int
     */
    @Override
    public int getCount()
    {
        if(this.arrayList != null)
            return this.arrayList.size();
        else
            return 0;
    }

    /**
     * This returns a record which is an CelestialBodyObservation Object from a given location in the ArrayList.
     * If the ArrayList is empty or null it returns null.
     * @param i int
     * @return Object
     */
    @Override
    public Object getItem(int i)
    {
        if(!this.arrayList.isEmpty() && this.arrayList != null)
        {
            if ((i >= 0) && (i < this.arrayList.size()))
                return this.arrayList.get(i);
        }

        return null;
    }

    /**
     * Gets the row id of the item at a specified position in the ArrayList.
     * @param i int (The position of the item within the adapter's data set)
     * @return
     */
    @Override
    public long getItemId(int i) {return i;}

    /**
     * This displays a record at a certain position in a certain format in the View
     *
     * (1) instantiates a layout observation_row_setup XML file to show the layout in the listView
     * (2) Initializes the TextView for the Name, DateTime, and Info of the CelestialBodyObservation
     * (3) Gets the CelestialBodyObservation Object where the Name, Date, Time, and Info of the Observation is stored
     * (4) If CelestialBodyObservation Object isn't null the first row displays the Name of the Observation,
     *     the second row displays the Date and Time of the Observation and the
     *     third row displays the Info of the Observation.
     * @param i int
     * @param view View
     * @param viewGroup ViewGroup
     * @return View
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        //(1)
        if(view == null)
        {
            view = this.layoutInflater.inflate(R.layout.observation_row_setup, null);

            if (view == null)
                return null;
        }

        //(2)
        TextView textObservationName = (TextView) view.findViewById(R.id.textViewObsTitle);
        TextView textObservationDateTime = (TextView) view.findViewById(R.id.textViewObsDateTime);
        TextView textObservationInfo = (TextView) view.findViewById(R.id.textViewObsInfo);

        //(3)
        CelestialBodyObservation celestialBodyObservation = (CelestialBodyObservation) this.getItem(i);

        //(4)
        if(celestialBodyObservation != null)
        {
            textObservationName.setText(celestialBodyObservation.line1());
            textObservationDateTime.setText(celestialBodyObservation.line2());
            textObservationInfo.setText(celestialBodyObservation.line3());
        }
        return view;
    }
}
