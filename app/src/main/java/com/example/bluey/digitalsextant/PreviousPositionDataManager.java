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
 * Created by robinluna Robin Luna on 8/26/17.
 */

public class PreviousPositionDataManager
{
    private Context                         context;//Interface to global information about an application environment
    private SQLiteDatabase                  sqLiteDatabase;
    private PreviousPositionDatabase        positionDatabase;
    private static final String             POSITION_QUERY = "SELECT * FROM " + PreviousPositionDatabase.TABLE_POSITION + " ORDER BY DATE_TIME";



    /**
     * The Default constructor for PreferenceDataManager which initializes the following
     * (1)Interface to global information about an application environment
     * @param context Context
     */
    public PreviousPositionDataManager(Context context)
    {
        this.context = context;//(1)
        this.positionDatabase = new PreviousPositionDatabase(context);
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
     * @param dataArrayList ArrayList<PreviousPosition>
     */
    public void updatePositionDatabase(ArrayList<PreviousPosition> dataArrayList)
    {
        if (null == this.sqLiteDatabase)
            this.sqLiteDatabase = this.positionDatabase.getWritableDatabase();
        try {
            this.sqLiteDatabase.execSQL(String.format("DELETE FROM %s", positionDatabase.TABLE_POSITION));
        } catch (SQLException e) {
            Log.e("updatePositionDatabase", e.getMessage());
        }

        for (int i = 0; i < dataArrayList.size(); i++) {
            PreviousPosition position = dataArrayList.get(i);
            ContentValues values = new ContentValues();
            values.put(PreviousPositionDatabase.COL1_DATE_TIME, position.DateTime);
            values.put(PreviousPositionDatabase.COL2_YEAR, position.Year);
            values.put(PreviousPositionDatabase.COL3_MONTH, position.Month);
            values.put(PreviousPositionDatabase.COL4_DATE, position.Date);
            values.put(PreviousPositionDatabase.COL5_HOUR, position.Hour);
            values.put(PreviousPositionDatabase.COL6_MINUTE, position.Minute);
            values.put(PreviousPositionDatabase.COL7_SECOND, position.Second);
            values.put(PreviousPositionDatabase.COL8_LATITUDE, position.Latitude);
            values.put(PreviousPositionDatabase.COL9_LONGITUDE, position.Longitude);
            this.sqLiteDatabase.insert(positionDatabase.TABLE_POSITION,
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
     * @return ArrayList<PreviousPosition>
     */
    public ArrayList<PreviousPosition> getPositionFromDatabase()
    {
        ArrayList<PreviousPosition> dataArrayList = new ArrayList<PreviousPosition>();

        if (null == this.sqLiteDatabase)
            this.sqLiteDatabase = this.positionDatabase.getWritableDatabase();

        Cursor cursor = null;

        try {
            cursor = this.sqLiteDatabase.rawQuery(POSITION_QUERY, null);
        } catch (Exception e)
        {
            Log.e("getPositionDatabase", e.getMessage());
            return null;
        }

        int colIndex;
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            PreviousPosition position = new PreviousPosition();

            colIndex = cursor.getColumnIndex(PreviousPositionDatabase.COL1_DATE_TIME);
            position.DateTime = cursor.getString(colIndex);

            colIndex = cursor.getColumnIndex(PreviousPositionDatabase.COL2_YEAR);
            position.Year = cursor.getInt(colIndex);

            colIndex = cursor.getColumnIndex(PreviousPositionDatabase.COL3_MONTH);
            position.Month = cursor.getInt(colIndex);

            colIndex = cursor.getColumnIndex(PreviousPositionDatabase.COL4_DATE);
            position.Date = cursor.getInt(colIndex);

            colIndex = cursor.getColumnIndex(PreviousPositionDatabase.COL5_HOUR);
            position.Hour = cursor.getInt(colIndex);

            colIndex = cursor.getColumnIndex(PreviousPositionDatabase.COL6_MINUTE);
            position.Minute = cursor.getInt(colIndex);

            colIndex = cursor.getColumnIndex(PreviousPositionDatabase.COL7_SECOND);
            position.Second = cursor.getInt(colIndex);

            colIndex = cursor.getColumnIndex(PreviousPositionDatabase.COL8_LATITUDE);
            position.Latitude = cursor.getDouble(colIndex);

            colIndex = cursor.getColumnIndex(PreviousPositionDatabase.COL9_LONGITUDE);
            position.Longitude = cursor.getDouble(colIndex);

            dataArrayList.add(position);

            cursor.moveToNext();
        }

        cursor.close();

        return dataArrayList;
    }
}