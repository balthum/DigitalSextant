package com.example.bluey.digitalsextant;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by toddgibson Todd Gibson on 9/9/17.
 */

public class CelestialBodyDatabaseManager
{
    private ArrayList<CelestialBody> celestialBodiesSet = null;          //Array List of Celestial Body Objects.
    private Context                  context            = null;          // Application context.

    public CelestialBodyDatabaseManager(Context context)
    {
        this.context            = context;
        populateCelestialBodies();
    }

    /**
     *
     *  Populate the Celestial Bodies Database
     *
     *  1.) Substantiate the Array List of Celestial Bodies, fileLine and key counter (i).
     *  2.) Open the celestial bodies test list file as read-only.
     *  3.) Loop through each line of the file
     *      a.) Split each lines comma separated values (CSV) in to a String Array.
     *      b.) Substantiate a celestial body object and populate it with data from the String Array.
     *          String Array [0] = Celestial Body Name
     *          String Array [1] = Celestial Body Sidereal Hour Angle
     *          String Array [2] = Celestial Body Declination
     *      c.) Add the Celestial Body to the Celestial Bodies List Array.
     *
     *   Note: A Toast Message will be show if no data can be added to the List Array.
     *
     */
    public void populateCelestialBodies()
    {
        // 1.)
        celestialBodiesSet = new ArrayList<>();
        String fileLine;
        int i = 0;

        try {
            // 2.)
            InputStream inputStream = context.getResources().openRawResource(R.raw.celestial_bodies_text_list);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // 3.)
            while ((fileLine = bufferedReader.readLine()) != null)
            {
                // a.)
                String[] celestialBodyList = fileLine.split(",");

                // b.)
                CelestialBody celestialBody = new CelestialBody();

                celestialBody.setKey(i++);
                celestialBody.setCelestialBodyName(celestialBodyList[0]);
                celestialBody.setSiderealHourAngle(Double.parseDouble(celestialBodyList[1]));
                celestialBody.setDeclination(Double.parseDouble(celestialBodyList[2]));

                // c.)
                celestialBodiesSet.add(celestialBody);
            }

        } catch (IOException e) {
            Toast.makeText(context.getApplicationContext(), "Unable to load Celestial Bodies", Toast.LENGTH_LONG).show();
        }
    }


    /**
     *  Get an Array of Celestial Body Names as Strings.
     *
     * @return String Array of Celestial Body Names.
     *
     *  1.) Create the String Array
     *  2.) Iterate the Celestial Bodies List Array
     *  3.) Add the Name of the Celestial Body to the String Array.
     *  4.) Return the String Array.
     */
    public String[] getCelestialBodyNames()
    {
        // 1.)
        String[] celestialBodyNames = new String[celestialBodiesSet.size()];
        int                       i = 0;

        // 2.)
        for (CelestialBody aCelestialBody : celestialBodiesSet)
        {
            // 3.)
            celestialBodyNames[i++] = aCelestialBody.getCelestialBodyName();
        }

        // 4.)
        return celestialBodyNames;
    }

    public double getCelestialBodyDeclination(int position)
    {
        CelestialBody celestialBody = celestialBodiesSet.get(position);
        return celestialBody.getDeclination();
    }

    /**
     *
     *  Get a Celestial Body Object by searching for it name.
     *
     * @param  celestialBodyName String Celestial Body Name.
     * @return CelestialBody Object.
     */
    public CelestialBody getCelestialBody(String celestialBodyName)
    {
        for (CelestialBody aCelestialBody : celestialBodiesSet)
        {
            if ( aCelestialBody.getCelestialBodyName().equals(celestialBodyName) )
            {
                return aCelestialBody;
            }
        }
        return null;
    }
}
