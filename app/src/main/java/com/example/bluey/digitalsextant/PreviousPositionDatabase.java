package com.example.bluey.digitalsextant;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Bluey on 9/7/17.
 */

public class PreviousPositionDatabase extends SQLiteOpenHelper
{
    public static final String          DATABASE_POSITION = "Position.db";
    public static final String          TABLE_POSITION = "position_table";
    public static final String          COL1_DATE_TIME = "DATE_TIME";
    public static final String          COL2_YEAR = "YEAR";
    public static final String          COL3_MONTH = "MONTH";
    public static final String          COL4_DATE = "DATE";
    public static final String          COL5_HOUR = "HOUR";
    public static final String          COL6_MINUTE = "MINUTE";
    public static final String          COL7_SECOND = "SECOND";
    public static final String          COL8_LATITUDE = "LATITUDE";
    public static final String          COL9_LONGITUDE = "LONGITUDE";
    public static final String          COL10_TITLE = "TITLE";
    public static final String          COL11_LONGITUDE_DIRECTION = "LONGITUDE_DIRECTION";
    public static final String          COL12_LATITUDE_DIRECTION = "LATITUDE_DIRECTION";


    private static final String DATABASE_CREATE =
            "create table " + TABLE_POSITION + "("
                    + COL1_DATE_TIME + " TEXT PRIMARY KEY, "
                    + COL2_YEAR + " INTEGER, "
                    + COL3_MONTH + " INTEGER, "
                    + COL4_DATE + " INTEGER, "
                    + COL5_HOUR + " INTEGER, "
                    + COL6_MINUTE + " INTEGER, "
                    + COL7_SECOND + " INTEGER, "
                    + COL8_LATITUDE + " REAL, "
                    + COL9_LONGITUDE + " REAL, "
                    + COL10_TITLE + " INTEGER, "
                    + COL11_LONGITUDE_DIRECTION + " TEXT, "
                    + COL12_LATITUDE_DIRECTION + " TEXT)";



    public PreviousPositionDatabase(Context context)
    {super(context, DATABASE_POSITION, null, 1);}

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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_POSITION);
        onCreate(sqLiteDatabase);
    }

}
