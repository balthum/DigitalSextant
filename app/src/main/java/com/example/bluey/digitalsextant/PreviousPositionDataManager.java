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
    private Context                         context;//Interface to global information about an application environment.
    private SQLiteDatabase                  sqLiteDatabase;//SQLiteDatabase which has methods to create, delete, execute SQL commands, and perform other common database management tasks.
    private PreviousPositionDatabase        positionDatabase;//The Database where all the Positions are stored.
    private static final String             POSITION_QUERY = "SELECT * FROM " + PreviousPositionDatabase.TABLE_POSITION + " ORDER BY DATE_TIME DESC";//How the database is in order.



    /**
     * The Default constructor for PreferenceDataManager which initializes the following:<br/><br/>
     *
     * (1)Interface to global information about an application environment.<br/>
     * (2)Creates a new Position Database.<br/>
     * (3)Sets the SQLiteDatabase null which has methods to create, delete, execute SQL commands, and perform other common database management tasks.<br/>
     * @param context Context
     */
    public PreviousPositionDataManager(Context context)
    {
        this.context = context;//(1)
        this.positionDatabase = new PreviousPositionDatabase(context);//(2)
        this.sqLiteDatabase = null;//(3)
    }

    /**
     * Updates the SQLite Position Database by the following:<br/><br/>
     *
     * (1) If the SQLiteDatabase is null it create or open a database that will be used for reading and writing info to database.<br/>
     * (2) Deletes the table of the position from the database.<br/>
     * (3) Puts each Position from the arraylist into the Position Database.<br/>
     *
     * @param dataArrayList ArrayList<PreviousPosition>
     */
    public void updatePositionDatabase(ArrayList<PreviousPosition> dataArrayList)
    {
        //(1)
        if (null == this.sqLiteDatabase)
            this.sqLiteDatabase = this.positionDatabase.getWritableDatabase();

        //(2)
        this.sqLiteDatabase.execSQL(String.format("DELETE FROM %s", positionDatabase.TABLE_POSITION));

        //(3)
        for (int i = 0; i < dataArrayList.size(); i++)
        {
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
            values.put(PreviousPositionDatabase.COL10_TITLE, position.Title);
            this.sqLiteDatabase.insert(positionDatabase.TABLE_POSITION,
                    null,
                    values);
        }
    }

    /**
     * Gets the SQLite Position Database by the following:<br><br>
     *
     * (1) Initializes the ArrayList for a Position object.<br>
     * (2) If the SQLiteDatabase is null it create or open a database that will be used for reading and writing info to database.<br/>
     * (3) Initializes the Cursor to null.<br/>
     * (4) Runs the provided SQL and returns a Cursor over the result set in the order of Position_QUERY .<br/>
     * (5) Initializes the column index and determines if the cursor can move to the first row.<br/>
     * (6) If cursor is not pointing to the position after the last row then the cursor gets all the information in the current position and put it
     * into the arrayList and do the same thing until no more positions.<br/>
     *
     * @return ArrayList<PreviousPosition>
     */
    public ArrayList<PreviousPosition> getPositionFromDatabase()
    {
        //(1)
        ArrayList<PreviousPosition> dataArrayList = new ArrayList<PreviousPosition>();

        //(2)
        if (null == this.sqLiteDatabase)
            this.sqLiteDatabase = this.positionDatabase.getWritableDatabase();

        //(3)
        Cursor cursor = null;

        //(4)
        try {
            cursor = this.sqLiteDatabase.rawQuery(POSITION_QUERY, null);
        } catch (Exception e)
        {
            Log.e("getPositionDatabase", e.getMessage());
            return null;
        }

        //(5)
        int colIndex;
        cursor.moveToFirst();

        //(6)
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

            colIndex = cursor.getColumnIndex(PreviousPositionDatabase.COL10_TITLE);
            position.Title = cursor.getInt(colIndex);

            dataArrayList.add(position);

            cursor.moveToNext();
        }

        cursor.close();

        return dataArrayList;
    }
}