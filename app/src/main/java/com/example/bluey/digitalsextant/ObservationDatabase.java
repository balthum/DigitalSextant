package com.example.bluey.digitalsextant;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Bluey on 9/7/17.
 */

public class ObservationDatabase extends SQLiteOpenHelper
{
    public static final String          DATABASE_OBSERVATION = "Observation.db";
    public static final String          TABLE_OBSERVATION = "observation_table";
    public static final String          COL1_ID = "ID";
    public static final String          COL2_TITLE = "TITLE";
    public static final String          COL3_CELESTIAL_BODY_NAME = "CELESTIAL_BODY_NAME";
    public static final String          COL4_COMPASS_HEADING = "COMPASS_HEADING";
    public static final String          COL5_COMPASS_DIRECTION = "COMPASS_DIRECTION";
    public static final String          COL6_HEIGHT_OBSERVED = "HEIGHT_OBSERVED";
    public static final String          COL7_YEAR = "YEAR";
    public static final String          COL8_MONTH = "MONTH";
    public static final String          COL9_DAY = "DAY";
    public static final String          COL10_HOUR = "HOUR";
    public static final String          COL11_MINUTE = "MINUTE";
    public static final String          COL12_SECOND = "SECOND";

    private static final String DATABASE_CREATE =
            "create table " + TABLE_OBSERVATION + " ("
                    + COL1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL2_TITLE + " TEXT NOT NULL, "
                    + COL3_CELESTIAL_BODY_NAME + " TEXT NOT NULL, "
                    + COL4_COMPASS_HEADING + " REAL, "
                    + COL5_COMPASS_DIRECTION + " TEXT NOT NULL, "
                    + COL6_HEIGHT_OBSERVED + " REAL, "
                    + COL7_YEAR + " INTEGER, "
                    + COL8_MONTH + " INTEGER, "
                    + COL9_DAY + " INTEGER, "
                    + COL10_HOUR + " INTEGER, "
                    + COL11_MINUTE + " INTEGER, "
                    + COL12_SECOND + " INTEGER)";

    public ObservationDatabase(Context context)
    {super(context, DATABASE_OBSERVATION, null, 1);}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        try {
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        } catch (SQLException e)
        {
            Log.e("ON Create", e.getMessage() );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_OBSERVATION);
        onCreate(sqLiteDatabase);
    }
}

