package com.example.bluey.digitalsextant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;


/**
 * Created by robinluna Robin Luna on 8/18/2017.
 */

public class ObservationDataManager
{

    private Context                     context;//Interface to global information about an application environment
    private SQLiteDatabase              sqLiteDatabase;
    private ObservationDatabase         observationDatabase;
    private static final String         OBSERVATION_QUERY = "SELECT * FROM " + ObservationDatabase.TABLE_OBSERVATION + " ORDER BY ID ";



    /**
     * The Default constructor for PreferenceDataManager which initializes the following
     * (1)Interface to global information about an application environment
     * @param context Context
     */
    public ObservationDataManager(Context context)
    {
        this.context = context;//(1)
        this.observationDatabase = new ObservationDatabase(context);
        this.sqLiteDatabase = null;
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
     * @param dataArrayList ArrayList<CelestialBodyObservation>
     */
    public void updateObservationDatabase(ArrayList<CelestialBodyObservation> dataArrayList)
    {
        if (null == this.sqLiteDatabase)
            this.sqLiteDatabase = this.observationDatabase.getWritableDatabase();
        try {
            this.sqLiteDatabase.execSQL(String.format("DELETE FROM %s", observationDatabase.TABLE_OBSERVATION));
        } catch (SQLException e) {
            Log.e("updateDatabase", e.getMessage());
        }

        for (int i = 0; i < dataArrayList.size(); i++) {
            CelestialBodyObservation celestialBodyObservation = dataArrayList.get(i);
            ContentValues values = new ContentValues();
            values.put(ObservationDatabase.COL2_TITLE, celestialBodyObservation.Title);
            values.put(ObservationDatabase.COL3_CELESTIAL_BODY_NAME, celestialBodyObservation.CelestialBodyName);
            values.put(ObservationDatabase.COL4_COMPASS_HEADING, celestialBodyObservation.CompassHeading);
            values.put(ObservationDatabase.COL5_COMPASS_DIRECTION, celestialBodyObservation.CompassDirection);
            values.put(ObservationDatabase.COL6_HEIGHT_OBSERVED, celestialBodyObservation.HeightObserver);
            values.put(ObservationDatabase.COL7_YEAR, celestialBodyObservation.Year);
            values.put(ObservationDatabase.COL8_MONTH, celestialBodyObservation.Month);
            values.put(ObservationDatabase.COL9_DAY, celestialBodyObservation.Day);
            values.put(ObservationDatabase.COL10_HOUR, celestialBodyObservation.Hour);
            values.put(ObservationDatabase.COL11_MINUTE, celestialBodyObservation.Minute);
            values.put(ObservationDatabase.COL12_SECOND, celestialBodyObservation.Second);
            this.sqLiteDatabase.insert(observationDatabase.TABLE_OBSERVATION,
                    null,
                    values);
        }
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
     * @return ArrayList<CelestialBodyObservation>
     */
    public ArrayList<CelestialBodyObservation> getObservationFromDatabase()
    {
        ArrayList<CelestialBodyObservation> dataArrayList = new ArrayList<CelestialBodyObservation>();

        if (null == this.sqLiteDatabase)
            this.sqLiteDatabase = this.observationDatabase.getWritableDatabase();

        Cursor cursor = null;

        try {
            cursor = this.sqLiteDatabase.rawQuery(OBSERVATION_QUERY, null);
        } catch (Exception e)
        {
            Log.e("getObservationDatabase", e.getMessage());
            return null;
        }

        int colIndex;
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            CelestialBodyObservation celestialBodyObservation = new CelestialBodyObservation();

            colIndex = cursor.getColumnIndex(ObservationDatabase.COL2_TITLE);
            celestialBodyObservation.Title = cursor.getString(colIndex);

            colIndex = cursor.getColumnIndex(ObservationDatabase.COL3_CELESTIAL_BODY_NAME);
            celestialBodyObservation.CelestialBodyName = cursor.getString(colIndex);

            colIndex = cursor.getColumnIndex(ObservationDatabase.COL4_COMPASS_HEADING);
            celestialBodyObservation.CompassHeading = cursor.getFloat(colIndex);

            colIndex = cursor.getColumnIndex(ObservationDatabase.COL5_COMPASS_DIRECTION);
            celestialBodyObservation.CompassDirection = cursor.getString(colIndex);

            colIndex = cursor.getColumnIndex(ObservationDatabase.COL6_HEIGHT_OBSERVED);
            celestialBodyObservation.HeightObserver = cursor.getFloat(colIndex);

            colIndex = cursor.getColumnIndex(ObservationDatabase.COL7_YEAR);
            celestialBodyObservation.Year= cursor.getInt(colIndex);

            colIndex = cursor.getColumnIndex(ObservationDatabase.COL8_MONTH);
            celestialBodyObservation.Month = cursor.getInt(colIndex);

            colIndex = cursor.getColumnIndex(ObservationDatabase.COL9_DAY);
            celestialBodyObservation.Day = cursor.getInt(colIndex);

            colIndex = cursor.getColumnIndex(ObservationDatabase.COL10_HOUR);
            celestialBodyObservation.Hour= cursor.getInt(colIndex);

            colIndex = cursor.getColumnIndex(ObservationDatabase.COL11_MINUTE);
            celestialBodyObservation.Minute = cursor.getInt(colIndex);

            colIndex = cursor.getColumnIndex(ObservationDatabase.COL12_SECOND);
            celestialBodyObservation.Second = cursor.getInt(colIndex);


            dataArrayList.add(celestialBodyObservation);

            cursor.moveToNext();
        }

        cursor.close();

        return dataArrayList;
    }
}
