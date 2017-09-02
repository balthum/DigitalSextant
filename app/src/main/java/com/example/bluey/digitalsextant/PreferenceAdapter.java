package com.example.bluey.digitalsextant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by robinluna Robin Luna on 8/21/2017.
 */
public class PreferenceAdapter extends BaseAdapter
{
    private ArrayList<Preference>       arrayList;//is an ArrayList that stores Preferences
    private LayoutInflater              layoutInflater;//is LayoutInflater to get obtain a layout XML file

    /**
     * The Default constructor for PreferenceAdapter which initializes the following
     * (1)An ArrayList that passes Preference in the arrayList
     * (2)Obtains the LayoutInflater from the given context
     * @param context Context
     * @param arrayList ArrayList<Preference>
     */
    public PreferenceAdapter(Context context, ArrayList<Preference> arrayList)
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
     * This returns a record which is an Preference Object from a given location in the ArrayList.
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
     * @return int
     */
    @Override
    public long getItemId(int i) {return i;}

    /**
     * This displays a record at a certain position in a certain format in the View
     *
     * (1) instantiates a layout preference_row_setup XML file to show the layout in the listView
     * (2) Initializes the TextView for the Name and Info of the Preference
     * (3) Gets the Preference Object of where the Name and Info of the Preference is stored
     * (4) If Preference Object isn't null the first row displays the Name of the Preference
     *     and the second row displays the Info of the Preference
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
            view = this.layoutInflater.inflate(R.layout.preference_row_setup, null);

            if (view == null)
                return null;
        }

        //(2)
        TextView textPreferenceName = (TextView) view.findViewById(R.id.textPrefNum);
        TextView textPreferenceInfo = (TextView) view.findViewById(R.id.textViewPrefInfo);

        //(3)
        Preference preference = (Preference) this.getItem(i);

        //(4)
        if(preference != null)
        {
            textPreferenceName.setText(preference.PreferenceName);
            textPreferenceInfo.setText(preference.PreferenceInfo);
        }
        return view;
    }
}
