package com.example.bluey.digitalsextant;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by robinluna Robin Luna on 8/21/2017.
 */

public class PreferenceDataManager
{
    private final static String         PreferenceSharedPreferencesName = "Preference"; //The SharedPreference name to access the SharedPreference Database
    private final static String         PreferenceNameKeyPrefix = "PreferenceName"; //where the name data is stored in the SharedPreference Database
    private final static String         PreferenceInfoKeyPrefix = "PreferenceInfo"; //where the Info data is stored in the SharedPreference Database
    private final static String         PreferenceNumKeyPrefix = "PreferenceNum"; //where the Number data is stored in the SharedPreference Database
    private final static String         PreferenceItemCount = "PreferenceItemCount"; //The number of records stored in the SharedPreference Database
    private SharedPreferences           sharedPreferences;// is used for accessing and modifying the contents of the preferences file name
    private Context                     context;//Interface to global information about an application environment

    /**
     * The Default constructor for PreferenceDataManager which initializes the following
     * (1)Interface to global information about an application environment
     * (2)Where you retrieve and hold the contents of the preferences file name in. The file name is called PreferenceSharedPreferencesName
     * @param context Context
     */
    public PreferenceDataManager(Context context)
    {
        this.context = context;//(1)
        this.sharedPreferences = context.getSharedPreferences(PreferenceDataManager.PreferenceSharedPreferencesName, Context.MODE_PRIVATE);//(2)
    }

    /**
     * Updates the SharedPreference Database by the following
     *
     * (1) Initializes the keyName for the SharedPreference database and the Preference object
     * (2) Initializes Editor to modifying values in a SharedPreference object and remove all values from the preferences
     * (3) Put the PreferenceItemCount equal to the size of the ArrayList
     * (4) Puts the data from the ArrayList into the SharedPreference database for all the Preference objects in the ArrayList
     *      (a) Gets the Preference object from a certain position in the ArrayList
     *      (b) Sets the key, then puts the preference name in the key from the Preference object to the SharedPreference database
     *      (c) Sets the key, then puts the preference info in the key from the Preference object to the SharedPreference database
     *      (d) Sets the key, then puts the preference num in the key from the Preference object to the SharedPreference database
     * (5) Commit your preferences and changes back from this Editor to the SharedPreferences object it is editing
     * @param dataArrayList ArrayList<Preference>
     */
    public void updatePreferenceDatabase(ArrayList<Preference> dataArrayList)
    {
        //(1)
        String keyName;
        Preference preference;

        //(2)
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.clear();

        //(3)
        editor.putInt(PreferenceDataManager.PreferenceItemCount, dataArrayList.size());

        //(4)
        for (int i = 0; i < dataArrayList.size(); i++)
        {
            //(a)
            preference = dataArrayList.get(i);

            //(b)
            keyName = String.format("%s%d", PreferenceDataManager.PreferenceNameKeyPrefix, i);
            editor.putString(keyName, preference.PreferenceName);

            //(c)
            keyName = String.format("%s%d", PreferenceDataManager.PreferenceInfoKeyPrefix, i);
            editor.putString(keyName, preference.PreferenceInfo);

            //(d)
            keyName = String.format("%s%d", PreferenceDataManager.PreferenceNumKeyPrefix, i);
            editor.putInt(keyName, preference.PreferenceNum);
        }
        //(5)
        editor.commit();
    }

    /**
     * Gets the SharedPreference Database by the following
     *
     * (1) Initializes the ArrayList that has a Preference object, the itemCount for the ArrayList,
     *     the Preference Object, and the keyName for the SharedPreference database.
     * (2) If their is a PreferenceItemCount in the SharedPreference you get the Preference objects in the database
     *     otherwise, you return a null ArrayList.
     *      (a) Sets the itemCount to how many items are in the database by what is stored in PreferenceItemCount
     *          and then gets each PreviousPosition Object in the database
     *          (a1) If the keyName exist set the Preference Object name from the keyName in the SharedPreference database
     *          (a2) If the keyName exist set the Preference Object info from the keyName in the SharedPreference database
     *          (a3) If the keyName exist set the Preference Object num from the keyName in the SharedPreference database
     *          (a4) Adds the Preference Object to the ArrayList
     * @return ArrayList<Preference>
     */
    public ArrayList<Preference> getPreferenceFromDatabase()
    {
        //(1)
        ArrayList<Preference> dataArrayList = new ArrayList<Preference>();
        int itemCount = 0;
        Preference preference = new Preference();
        String keyName;

        //(2)
        if (this.sharedPreferences.contains(PreferenceDataManager.PreferenceItemCount))
        {
            //(a)
            itemCount = this.sharedPreferences.getInt(PreferenceDataManager.PreferenceItemCount, 0);
            for (int i = 0; i < itemCount; i++)
            {
                //(a1)
                keyName = String.format("%s%d", PreferenceDataManager.PreferenceNameKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    preference.setPreferenceName(this.sharedPreferences.getString(keyName, ""));

                //(a2)
                keyName = String.format("%s%d", PreferenceDataManager.PreferenceInfoKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    preference.setPreferenceInfo(this.sharedPreferences.getString(keyName, ""));

                //(a3)
                keyName = String.format("%s%d", PreferenceDataManager.PreferenceNumKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    preference.setPreferenceNum(this.sharedPreferences.getInt(keyName, 0));

                //(a4)
                dataArrayList.add(preference);
            }
        }
        return dataArrayList;
    }
}
