package com.example.bluey.digitalsextant;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by robinluna Robin Luna on 8/26/17.
 */

public class PreviousPositionDataManager
{
    private final static String         PositionSharedPreferencesName = "PreviousPosition";//The SharedPreference name to access the SharedPreference Database
    private final static String         PositionTitleKeyPrefix = "Title";//where the title data is stored in the SharedPreference Database
    private final static String         PositionMonthKeyPrefix = "Month";//where the month data is stored in the SharedPreference Database
    private final static String         PositionDayKeyPrefix = "Date";//where the date data is stored in the SharedPreference Database
    private final static String         PositionYearKeyPrefix = "Year";//where the year data is stored in the SharedPreference Database
    private final static String         PositionHourKeyPrefix = "Hour";//where the hour data is stored in the SharedPreference Database
    private final static String         PositionMinuteKeyPrefix = "Minute";//where the minute data is stored in the SharedPreference Database
    private final static String         PositionSecondKeyPrefix = "Second";//where the seconds data is stored in the SharedPreference Database
    private final static String         PositionLatitudeKeyPrefix = "Latitude";//where the latitude data is stored in the SharedPreference Database
    private final static String         PositionLongitudeKeyPrefix = "Longitude";//where the longitude data is stored in the SharedPreference Database
    private final static String         PositionItemCount = "PreviousPositionItemCount"; //The number of records stored in the SharedPreference Database
    private SharedPreferences           sharedPreferences;// is used for accessing and modifying the contents of the preferences file name
    private Context                     context;//Interface to global information about an application environment

    /**
     * The Default constructor for PreviousPositionDataManager which initializes the following
     *
     * (1)Interface to global information about an application environment
     * (2)Where you retrieve and hold the contents of the preferences file name in. The file name is called PreferenceSharedPreferencesName
     * @param context Context
     */
    public PreviousPositionDataManager(Context context)
    {
        this.context = context;//(1)
        this.sharedPreferences = context.getSharedPreferences(PreviousPositionDataManager.PositionSharedPreferencesName, Context.MODE_PRIVATE);//(2)
    }

    /**
     * Updates the SharedPreference Database by the following
     *
     * (1) Initializes the keyName for the SharedPreference database and the PreviousPosition object
     * (2) Initializes Editor to modifying values in a SharedPreference object and remove all values from the preferences
     * (3) Put the PositionItemCount equal to the size of the ArrayList
     * (4) Puts the data from the ArrayList into the SharedPreference database for all the PreviousPosition objects in the ArrayList
     *      (a) Gets the PreviousPosition object from a certain position in the ArrayList
     *      (b) Sets the key, then puts the title position in the key from the PreviousPosition object to the SharedPreference database
     *      (c) Sets the key, then puts the month position in the key from the PreviousPosition object to the SharedPreference database
     *      (d) Sets the key, then puts the date position in the key from the PreviousPosition object to the SharedPreference database
     *      (e) Sets the key, then puts the year position in the key from the PreviousPosition object to the SharedPreference database
     *      (f) Sets the key, then puts the hour position in the key from the PreviousPosition object to the SharedPreference database
     *      (g) Sets the key, then puts the minutes position in the key from the PreviousPosition object to the SharedPreference database
     *      (h) Sets the key, then puts the seconds position in the key from the PreviousPosition object to the SharedPreference database
     *      (i) Sets the key, then puts the latitude position in the key from the PreviousPosition object to the SharedPreference database
     *      (j) Sets the key, then puts the longitude position in the key from the PreviousPosition object to the SharedPreference database
     * (5) Commit your preferences and changes back from this Editor to the SharedPreferences object it is editing
     * @param dataArrayList ArrayList<PreviousPostion>
     */
    public void updateObservationDatabase(ArrayList<PreviousPosition> dataArrayList)
    {
        //(1)
        String keyName;
        PreviousPosition previousPosition;

        //(2)
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.clear();

        //(3)
        editor.putInt(PreviousPositionDataManager.PositionItemCount, dataArrayList.size());

        //(4)
        for (int i = 0; i < dataArrayList.size(); i++)
        {
            //(a)
            previousPosition = dataArrayList.get(i);

            //(b)
            keyName = String.format("%s%d", PreviousPositionDataManager.PositionTitleKeyPrefix, i);
            editor.putInt(keyName, previousPosition.PreviousPositionTitle);

            //(c)
            keyName = String.format("%s%d", PreviousPositionDataManager.PositionMonthKeyPrefix, i);
            editor.putInt(keyName, previousPosition.PreviousPositionMonth);

            //(d)
            keyName = String.format("%s%d", PreviousPositionDataManager.PositionDayKeyPrefix, i);
            editor.putInt(keyName, previousPosition.PreviousPositionDate);

            //(e)
            keyName = String.format("%s%d", PreviousPositionDataManager.PositionYearKeyPrefix, i);
            editor.putInt(keyName, previousPosition.PreviousPositionYear);

            //(f)
            keyName = String.format("%s%d", PreviousPositionDataManager.PositionHourKeyPrefix, i);
            editor.putInt(keyName, previousPosition.PreviousPositionHour);

            //(g)
            keyName = String.format("%s%d", PreviousPositionDataManager.PositionMinuteKeyPrefix, i);
            editor.putInt(keyName, previousPosition.PreviousPositionMinute);

            //(h)
            keyName = String.format("%s%d", PreviousPositionDataManager.PositionSecondKeyPrefix, i);
            editor.putInt(keyName, previousPosition.PreviousPositionSecond);

            //(i)
            keyName = String.format("%s%d", PreviousPositionDataManager.PositionLatitudeKeyPrefix, i);
            editor.putFloat(keyName, previousPosition.PreviousPositionLatitude);

            //(j)
            keyName = String.format("%s%d", PreviousPositionDataManager.PositionLongitudeKeyPrefix, i);
            editor.putFloat(keyName, previousPosition.PreviousPositionLongitude);
        }
        //(5)
        editor.commit();
    }

    /**
     * Gets the SharedPreference Database by the following
     *
     * (1) Initializes the ArrayList that has a PreviousPosition object, the itemCount for the ArrayList,
     *     the PreviousPosition Object, and the keyName for the SharedPreference database.
     * (2) If their is a PositionItemCount in the SharedPreference you get the PreviousPosition
     *     objects in the database otherwise, you return a null ArrayList.
     *      (a) Sets the itemCount to how many items are in the database by what is stored in PositionItemCount
     *          then gets each PreviousPosition Object in the database.
     *          (a1) If the keyName exist set the PreviousPosition Object title from the keyName in the SharedPreference database
     *          (a2) If the keyName exist set the PreviousPosition Object month from the keyName in the SharedPreference database
     *          (a3) If the keyName exist set the PreviousPosition Object date from the keyName in the SharedPreference database
     *          (a4) If the keyName exist set the PreviousPosition Object year from the keyName in the SharedPreference database
     *          (a5) If the keyName exist set the PreviousPosition Object hours from the keyName in the SharedPreference database
     *          (a6) If the keyName exist set the PreviousPosition Object minutes from the keyName in the SharedPreference database
     *          (a7) If the keyName exist set the PreviousPosition Object seconds from the keyName in the SharedPreference database
     *          (a8) If the keyName exist set the PreviousPosition Object latitude from the keyName in the SharedPreference database
     *          (a9) If the keyName exist set the PreviousPosition Object longitude from the keyName in the SharedPreference database
     *          (a10) Adds the PreviousPosition Object to the ArrayList
     * @return ArrayList<PreviousPosition>
     */
    public ArrayList<PreviousPosition> getObservationFromDatabase()
    {
        //(1)
        ArrayList<PreviousPosition> dataArrayList = new ArrayList<PreviousPosition>();
        int itemCount = 0;
        PreviousPosition previousPosition = new PreviousPosition();
        String keyName;

        //(2)
        if (this.sharedPreferences.contains(PreviousPositionDataManager.PositionItemCount))
        {
            //(a)
            itemCount = this.sharedPreferences.getInt(PreviousPositionDataManager.PositionItemCount, 0);
            for (int i = 0; i < itemCount; i++)
            {
                //(a1)
                keyName = String.format("%s%d", PreviousPositionDataManager.PositionTitleKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    previousPosition.PreviousPositionTitle = (this.sharedPreferences.getInt(keyName, 0));

                //(a2)
                keyName = String.format("%s%d", PreviousPositionDataManager.PositionMonthKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    previousPosition.PreviousPositionMonth = (this.sharedPreferences.getInt(keyName, 0));

                //(a3)
                keyName = String.format("%s%d", PreviousPositionDataManager.PositionDayKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    previousPosition.PreviousPositionDate = (this.sharedPreferences.getInt(keyName, 0));

                //(a4)
                keyName = String.format("%s%d", PreviousPositionDataManager.PositionYearKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    previousPosition.PreviousPositionYear = (this.sharedPreferences.getInt(keyName, 0));

                //(a5)
                keyName = String.format("%s%d", PreviousPositionDataManager.PositionHourKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    previousPosition.PreviousPositionHour = (this.sharedPreferences.getInt(keyName, 0));

                //(a6)
                keyName = String.format("%s%d", PreviousPositionDataManager.PositionMinuteKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    previousPosition.PreviousPositionMinute = (this.sharedPreferences.getInt(keyName, 0));

                //(a7)
                keyName = String.format("%s%d", PreviousPositionDataManager.PositionSecondKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    previousPosition.PreviousPositionSecond = (this.sharedPreferences.getInt(keyName, 0));

                //(a8)
                keyName = String.format("%s%d", PreviousPositionDataManager.PositionLatitudeKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    previousPosition.PreviousPositionLatitude = (this.sharedPreferences.getFloat(keyName, 0));

                //(a9)
                keyName = String.format("%s%d", PreviousPositionDataManager.PositionLongitudeKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    previousPosition.PreviousPositionLongitude = (this.sharedPreferences.getFloat(keyName, 0));

                //(a10)
                dataArrayList.add(previousPosition);
            }
        }
        return dataArrayList;
    }
}