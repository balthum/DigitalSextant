package com.example.bluey.digitalsextant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by robinluna Robin Luna on 8/18/2017.
 */

public class ObservationDataManager
{

    private final static String         ObservationSharedPreferencesName = "Observation";//The SharedPreference name to access the SharedPreference Database
    private final static String         TitleKeyPrefix = "Title";//where the title data is stored in the SharedPreference Database
    private final static String         MonthKeyPrefix = "Month";//where the month data is stored in the SharedPreference Database
    private final static String         DayKeyPrefix = "Day";//where the day data is stored in the SharedPreference Database
    private final static String         YearKeyPrefix = "Year";//where the year data is stored in the SharedPreference Database
    private final static String         HourKeyPrefix = "Hour";//where the hour data is stored in the SharedPreference Database
    private final static String         MinuteKeyPrefix = "Minute";//where the minute data is stored in the SharedPreference Database
    private final static String         SecondKeyPrefix = "Second";//where the seconds data is stored in the SharedPreference Database
    private final static String         CelestialBodyNameKeyPrefix = "CelestialBodyName";//where the Celestial Body Name data is stored in the SharedPreference Database
    private final static String         CompassBearingKeyPrefix = "CompassBearing";//where the compass bearing data is stored in the SharedPreference Database
    private final static String         CompassDirectionKeyPrefix = "CompassDirection";//where the compass direction data is stored in the SharedPreference Database
    private final static String         HeightObservedKeyPrefix = "HeightObserved";//where the zenith/ height observed data is stored in the SharedPreference Database
    private final static String         ObservationItemCount = "ObservationItemCount"; //The number of records stored in the SharedPreference Database
    private SharedPreferences           sharedPreferences;// is used for accessing and modifying the contents of the preferences file name
    private Context                     context;//Interface to global information about an application environment

    /**
     * The Default constructor for ObservationDataManager which initializes the following
     *
     * (1)Interface to global information about an application environment
     * (2)Where you retrieve and hold the contents of the preferences file name in. The file name is called PreferenceSharedPreferencesName
     * @param context Context
     */
    public ObservationDataManager(Context context)
    {
        this.context = context;//(1)
        this.sharedPreferences = context.getSharedPreferences(ObservationDataManager.ObservationSharedPreferencesName, Context.MODE_PRIVATE);//(2)
    }

    /**
     * Updates the SharedPreference Database by the following
     *
     * (1) Initializes the keyName for the SharedPreference database and the CelestialBodyObservation object
     * (2) Initializes Editor to modifying values in a SharedPreference object and remove all values from the preferences
     * (3) Put the PreferenceItemCount equal to the size of the ArrayList
     * (4) Puts the data from the ArrayList into the SharedPreference database for all the CelestialBodyObservation objects in the ArrayList
     *      (a) Gets the CelestialBodyObservation object from a certain position in the ArrayList
     *      (b) Sets the key, then puts the title observation in the key from the CelestialBodyObservation object to the SharedPreference database
     *      (c) Sets the key, then puts the month observation in the key from the CelestialBodyObservation object to the SharedPreference database
     *      (d) Sets the key, then puts the day observation in the key from the CelestialBodyObservation object to the SharedPreference database
     *      (e) Sets the key, then puts the year observation in the key from the CelestialBodyObservation object to the SharedPreference database
     *      (f) Sets the key, then puts the hour observation in the key from the CelestialBodyObservation object to the SharedPreference database
     *      (g) Sets the key, then puts the minutes observation in the key from the CelestialBodyObservation object to the SharedPreference database
     *      (h) Sets the key, then puts the seconds observation in the key from the CelestialBodyObservation object to the SharedPreference database
     *      (i) Sets the key, then puts the CelestialBodyName observation in the key from the CelestialBodyObservation object to the SharedPreference database
     *      (j) Sets the key, then puts the compass bearing observation in the key from the CelestialBodyObservation object to the SharedPreference database
     *      (k) Sets the key, then puts the compass direction observation in the key from the CelestialBodyObservation object to the SharedPreference database
     *      (l) Sets the key, then puts the height observation in the key from the CelestialBodyObservation object to the SharedPreference database
     * (5) Commit your preferences and changes back from this Editor to the SharedPreferences object it is editing
     * @param dataArrayList ArrayList<CelestialBodyObservation>
     */
    @SuppressLint({"DefaultLocale", "ApplySharedPref"})
    public void updateObservationDatabase(ArrayList<CelestialBodyObservation> dataArrayList)
    {
        //(1)
        String keyName;
        CelestialBodyObservation celestialBodyObservation;

        //(2)
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.clear();

        //(3)
        editor.putInt(ObservationDataManager.ObservationItemCount, dataArrayList.size());

        //(4)
        for (int i = 0; i < dataArrayList.size(); i++)
        {
            //(a)
            celestialBodyObservation = dataArrayList.get(i);

            //(b)
            keyName = String.format("%s%d", ObservationDataManager.TitleKeyPrefix, i);
            editor.putString(keyName, celestialBodyObservation.Title);

            //(c)
            keyName = String.format("%s%d", ObservationDataManager.MonthKeyPrefix, i);
            editor.putInt(keyName, celestialBodyObservation.Month);

            //(d)
            keyName = String.format("%s%d", ObservationDataManager.DayKeyPrefix, i);
            editor.putInt(keyName, celestialBodyObservation.Day);

            //(e)
            keyName = String.format("%s%d", ObservationDataManager.YearKeyPrefix, i);
            editor.putInt(keyName, celestialBodyObservation.Year);

            //(f)
            keyName = String.format("%s%d", ObservationDataManager.HourKeyPrefix, i);
            editor.putInt(keyName, celestialBodyObservation.Hour);

            //(g)
            keyName = String.format("%s%d", ObservationDataManager.MinuteKeyPrefix, i);
            editor.putInt(keyName, celestialBodyObservation.Minute);

            //(h)
            keyName = String.format("%s%d", ObservationDataManager.SecondKeyPrefix, i);
            editor.putInt(keyName, celestialBodyObservation.Second);

            //(i)
            keyName = String.format("%s%d", ObservationDataManager.CelestialBodyNameKeyPrefix, i);
            editor.putString(keyName, celestialBodyObservation.CelestialBodyName);

            //(j)
            keyName = String.format("%s%d", ObservationDataManager.CompassBearingKeyPrefix, i);
            editor.putFloat(keyName, celestialBodyObservation.CompassHeading);

            //(k)
            keyName = String.format("%s%d", ObservationDataManager.CompassDirectionKeyPrefix, i);
            editor.putString(keyName, celestialBodyObservation.CompassDirection);

            //(l)
            keyName = String.format("%s%d", ObservationDataManager.HeightObservedKeyPrefix, i);
            editor.putFloat(keyName, celestialBodyObservation.HeightObserver);
        }
        //(5)
        editor.commit();
    }

    /**
     * Gets the SharedPreference Database by the following
     *
     * (1) Initializes the ArrayList that has a CelestialBodyObservation object, the itemCount for the ArrayList,
     *     the CelestialBodyObservation Object, and the keyName for the SharedPreference database.
     * (2) If their is a PreferenceItemCount in the SharedPreference you get the CelestialBodyObservation
     *     objects in the database otherwise, you return a null ArrayList.
     *      (a) Sets the itemCount to how many items are in the database by what is stored in ObservationItemCount
     *           and then gets each CelestialBodyObservation Object in the database.
     *          (a1) If the keyName exist set the CelestialBodyObservation Object title from the keyName in the SharedPreference database
     *          (a2) If the keyName exist set the CelestialBodyObservation Object month from the keyName in the SharedPreference database
     *          (a3) If the keyName exist set the CelestialBodyObservation Object day from the keyName in the SharedPreference database
     *          (a4) If the keyName exist set the CelestialBodyObservation Object year from the keyName in the SharedPreference database
     *          (a5) If the keyName exist set the CelestialBodyObservation Object hours from the keyName in the SharedPreference database
     *          (a6) If the keyName exist set the CelestialBodyObservation Object minutes from the keyName in the SharedPreference database
     *          (a7) If the keyName exist set the CelestialBodyObservation Object seconds from the keyName in the SharedPreference database
     *          (a8) If the keyName exist set the CelestialBodyObservation Object Celestial Body Name from the keyName in the SharedPreference database
     *          (a9) If the keyName exist set the CelestialBodyObservation Object compass bearing from the keyName in the SharedPreference database
     *          (a10) If the keyName exist set the CelestialBodyObservation Object compass direction from the keyName in the SharedPreference database
     *          (a11) If the keyName exist set the CelestialBodyObservation Object observed height info from the keyName in the SharedPreference database
     *          (a12) Adds the CelestialBodyObservation Object to the ArrayList
     * @return ArrayList<CelestialBodyObservation>
     */
    @SuppressLint("DefaultLocale")
    public ArrayList<CelestialBodyObservation> getObservationFromDatabase()
    {
        //(1)
        ArrayList<CelestialBodyObservation> dataArrayList = new ArrayList<CelestialBodyObservation>();
        int itemCount;
        CelestialBodyObservation celestialBodyObservation = new CelestialBodyObservation();
        String keyName;

        //(2)
        if (this.sharedPreferences.contains(ObservationDataManager.ObservationItemCount))
        {
            //(a)
            itemCount = this.sharedPreferences.getInt(ObservationDataManager.ObservationItemCount, 0);
            for (int i = 0; i < itemCount; i++)
            {
                //(a1)
                keyName = String.format("%s%d", ObservationDataManager.TitleKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    celestialBodyObservation.Title = (this.sharedPreferences.getString(keyName, ""));

                //(a2)
                keyName = String.format("%s%d", ObservationDataManager.MonthKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    celestialBodyObservation.Month = (this.sharedPreferences.getInt(keyName, 0));

                //(a3)
                keyName = String.format("%s%d", ObservationDataManager.DayKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    celestialBodyObservation.Day = (this.sharedPreferences.getInt(keyName, 0));

                //(a4)
                keyName = String.format("%s%d", ObservationDataManager.YearKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    celestialBodyObservation.Year = (this.sharedPreferences.getInt(keyName, 0));

                //(a5)
                keyName = String.format("%s%d", ObservationDataManager.HourKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    celestialBodyObservation.Hour = (this.sharedPreferences.getInt(keyName, 0));

                //(a6)
                keyName = String.format("%s%d", ObservationDataManager.MinuteKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    celestialBodyObservation.Minute = (this.sharedPreferences.getInt(keyName, 0));

                //(a7)
                keyName = String.format("%s%d", ObservationDataManager.SecondKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    celestialBodyObservation.Second = (this.sharedPreferences.getInt(keyName, 0));

                //(a8)
                keyName = String.format("%s%d", ObservationDataManager.CelestialBodyNameKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    celestialBodyObservation.CelestialBodyName = (this.sharedPreferences.getString(keyName, ""));

                //(a9)
                keyName = String.format("%s%d", ObservationDataManager.CompassBearingKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    celestialBodyObservation.CompassHeading = (this.sharedPreferences.getFloat(keyName, 0));

                //(a10)
                keyName = String.format("%s%d", ObservationDataManager.CompassDirectionKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    celestialBodyObservation.CompassDirection = (this.sharedPreferences.getString(keyName, ""));

                //(a11)
                keyName = String.format("%s%d", ObservationDataManager.HeightObservedKeyPrefix, i);
                if (this.sharedPreferences.contains(keyName))
                    celestialBodyObservation.HeightObserver = (this.sharedPreferences.getFloat(keyName, 0));

                //(a12)
                dataArrayList.add(celestialBodyObservation);
            }
        }
        return dataArrayList;
    }
}
