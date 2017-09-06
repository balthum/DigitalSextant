package com.example.bluey.digitalsextant;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by robinluna Robin Luna on 8/21/2017.
 */

public class PreferenceDataManager
{

    private Context                 context;//Interface to global information about an application environment
    private SQLiteDatabase          sqLiteDatabase;
    private PreferenceDatabase      preferenceDatabase;
    private static final String         PREFERENCE_QUERY = "SELECT * FROM " + PreferenceDatabase.TABLE_PREFERENCE + " ORDER BY ID ";


    /**
     * The Default constructor for PreferenceDataManager which initializes the following
     * (1)Interface to global information about an application environment
     * @param context Context
     */
    public PreferenceDataManager(Context context)
    {
        this.context = context;//(1)
        this.preferenceDatabase = new PreferenceDatabase(context);
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
     * @param dataArrayList ArrayList<Preference>
     */
    public void updatePreferenceDatabase(ArrayList<Preference> dataArrayList)
    {
        if (null == this.sqLiteDatabase)
            this.sqLiteDatabase = this.preferenceDatabase.getWritableDatabase();
        try {
            this.sqLiteDatabase.execSQL(String.format("DELETE FROM %s", preferenceDatabase.TABLE_PREFERENCE));
        } catch (SQLException e) {
            Log.e("updateDatabase", e.getMessage());
        }

        for (int i = 0; i < dataArrayList.size(); i++) {
            Preference preference = dataArrayList.get(i);
            ContentValues values = new ContentValues();
            values.put(PreferenceDatabase.COL_2, preference.PreferenceName);
            values.put(PreferenceDatabase.COL_3, preference.PreferenceInfo);
            values.put(PreferenceDatabase.COL_4, preference.PreferenceNum);
            long insertId = this.sqLiteDatabase.insert(preferenceDatabase.TABLE_PREFERENCE,
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
     * @return ArrayList<Preference>
     */
    public ArrayList<Preference> getPreferenceFromDatabase()
    {
        ArrayList<Preference> dataArrayList = new ArrayList<Preference>();

        if (null == this.sqLiteDatabase)
            this.sqLiteDatabase = this.preferenceDatabase.getWritableDatabase();

        Cursor cursor = null;

        try {
            cursor = this.sqLiteDatabase.rawQuery(PREFERENCE_QUERY, null);
        } catch (Exception e)
        {
            Log.e("getPreferenceDatabase", e.getMessage());
            return null;
        }

        int colIndex;
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Preference preference = new Preference();

            colIndex = cursor.getColumnIndex(PreferenceDatabase.COL_2);
            preference.PreferenceName = cursor.getString(colIndex);

            colIndex = cursor.getColumnIndex(PreferenceDatabase.COL_3);
            preference.PreferenceInfo = cursor.getString(colIndex);

            colIndex = cursor.getColumnIndex(PreferenceDatabase.COL_4);
            preference.PreferenceNum = cursor.getInt(colIndex);

            dataArrayList.add(preference);

            cursor.moveToNext();
        }

        cursor.close();

        return dataArrayList;
    }
}
