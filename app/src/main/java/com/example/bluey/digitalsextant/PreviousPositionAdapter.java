package com.example.bluey.digitalsextant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by robinluna Robin Luna on 8/25/17.
 */

public class PreviousPositionAdapter extends BaseAdapter
{
    private ArrayList<PreviousPosition>         arrayList; //is an ArrayList that stores PreviousPosition
    private LayoutInflater                      layoutInflater;//is LayoutInflater to get obtain a layout XML file

    /**
     * The Default constructor for PreviousPositionAdapter which initializes the following
     * (1)An ArrayList that passes PreviousPosition in the arrayList
     * (2)Obtains the LayoutInflater from the given context
     * @param context Context
     * @param arrayList ArrayList<PreviousPosition>
     */
    public PreviousPositionAdapter(Context context, ArrayList<PreviousPosition> arrayList)
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
     * This returns a record which is an PreviousPosition Object from a given location in the ArrayList.
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
     * (1) instantiates a layout previous_position_row_setup XML file to show the layout in the listView
     * (2) Initializes the TextView for the Title, Date and Time, and Latitude and Longitude of the PreviousPosition
     * (3) Gets the PreviousPosition Object where the Title, Date, Time, Latitude, and Longitude of the position is stored
     * (4) If PreviousPosition Object isn't null the first row displays the Name of the position,
     *     the second row displays the Date and Time of the position and the
     *     third row displays the Latitude and Longitude of the position.
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
        TextView textPositionTitle = (TextView) view.findViewById(R.id.textViewPositionName);
        TextView textPositionDateTime = (TextView) view.findViewById(R.id.textViewPositionDateTime);
        TextView textPositionLatLong = (TextView) view.findViewById(R.id.textViewPositionLatLong);

        //(3)
        PreviousPosition previousPosition = (PreviousPosition) this.getItem(i);

        //(4)
        if(previousPosition != null)
        {
            textPositionTitle.setText(previousPosition.line1());
            textPositionDateTime.setText(previousPosition.line2());
            textPositionLatLong.setText(previousPosition.line3());
        }
        return view;
    }
}
