package com.example.bluey.digitalsextant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Bluey on 9/5/17.
 */

public class PreferenceDatabase extends SQLiteOpenHelper
{
    public static final String          DATABASE_PREFERENCE = "Preference.db";
    public static final String          TABLE_PREFERENCE = "preference_table";
    public static final String          COL_1 = "ID";
    public static final String          COL_2 = "NAME";
    public static final String          COL_3 = "INFO";
    public static final String          COL_4 = "AMOUNT";

    private static final String DATABASE_CREATE =
            "create table " + TABLE_PREFERENCE + "("
                    + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_2 + " TEXT NOT NULL, "
                    + COL_3 + " TEXT NOT NULL, "
                    + COL_4 + " INTEGER)";



    public PreferenceDatabase(Context context)
    {super(context, DATABASE_PREFERENCE, null, 1);}

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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_PREFERENCE);
        onCreate(sqLiteDatabase);
    }

}
