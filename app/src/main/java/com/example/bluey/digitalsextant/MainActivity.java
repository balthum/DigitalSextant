package com.example.bluey.digitalsextant;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by robinluna Robin Luna on 8/06/17.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GPSChange
{
    public static Toolbar               toolbar = null; //gets the Toolbar with generalization of action bars
    public static NavigationView        navigationView;//gets the standard navigation menu
    private DrawerLayout                drawerLayout; //lets you be able pull window out from the left side of the activity
    private ActionBarDrawerToggle       drawerToggle;//ties the functionality of DrawerLayout and ActionBar
    private Timer                       timer;
    private double                      latitude;
    private double                      longitude;
    private GPSModule                   gpsModule;




    /**
     * When the MainActivity is created it does the following
     * (1) Initializes the MainActivity and sets the activity content layout
     * (2) Initialize and toolbar and sets the actionBar to the toolbar
     * (3) Sets the title of the actionBar to Home
     * (4) Creates the Navigation View menu
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //(1)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //(2)
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Home");//(3)
        initiateNavigationView();//(4)

        getPreferenceData();

        gpsModule = new GPSModule(this);

        resetTimer();
    }

    public void resetTimer() {
        PreferenceDataManager preferenceDataManager = new PreferenceDataManager(getApplicationContext());
        ArrayList<Preference> arrayList = new ArrayList<>(preferenceDataManager.getPreferenceFromDatabase());
        //gets the gps information on how often to get the gps updates
        Preference preference;

        preference = arrayList.get(0);
        int update_time =  preference.getPreferenceNum();

        if (timer != null) {
            timer.cancel();
        }

        this.timer  = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                try {
                    timerOff();
                } catch (Exception e) {
                    Log.d("shutup", e.getMessage());
                }

            }
        }, 20000, (update_time * 1l * 1000l));
    }

    public void timerOff()
    {
        //This method is called directly by the timer and runs in the same thread as the timer thread.

        //We call the method that will work with the UI through the runOnUiThread method
        this.runOnUiThread(runnable);
    }

    public Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            //this method runs in the same thread as the UI.
            //Do something to the UI thread here

            if(gpsModule.canGetLocation())
            {
                PreviousPosition previousPosition = new PreviousPosition();

                PreviousPositionDataManager previousPositionDataManager = new PreviousPositionDataManager(getApplication());
                ArrayList<PreviousPosition> arrayList = new ArrayList<>(previousPositionDataManager.getPositionFromDatabase());

                previousPosition.setLatitude(latitude);
                previousPosition.setLongitude(longitude);
                previousPosition.setTitle(1);
                arrayList.add(previousPosition);// adds record to ArrayList


                if(arrayList.size() > 1)
                {
                    int size = 1;
                    while(size < arrayList.size())
                    {
                        previousPosition = arrayList.get(size - 1);
                        previousPosition.setTitle(size + 1);
                        arrayList.set(size - 1,previousPosition);
                        size++;
                    }
                }

                previousPositionDataManager.updatePositionDatabase(arrayList);//updates Previous position database for record added

                Toast.makeText(MainActivity.this,previousPosition.line2() + "\n" + previousPosition.line3(), Toast.LENGTH_LONG).show();
            }

        }
    };


    public void getPreferenceData()
    {
        PreferenceDataManager preferenceDataManager = new PreferenceDataManager(getApplicationContext());
        ArrayList <Preference> arrayList = new ArrayList<>(preferenceDataManager.getPreferenceFromDatabase());


        //creates the gps preference
        if(arrayList.size() == 0)
        {
            Preference preference = new Preference();

            //sets gps data and adds it to the array list and listview
            preference.setPreferenceName("GPS");
            preference.setPreferenceInfo("30 minutes");
            preference.setPreferenceNum(30);
            arrayList.add(preference);

            //creates the previous positions preference
            preference = new Preference();

            //sets previous position data
            preference.setPreferenceName("Previous Positions");
            preference.setPreferenceInfo("30 records");
            preference.setPreferenceNum(30);

            //adds gps and position data
            arrayList.add(preference);// adds record to ArrayList
            preferenceDataManager.updatePreferenceDatabase(arrayList);//updates preferenceDatabase for record added
        }
    }

    /**
     * Creates the Navigation View menu by the following
     * (1)Initializes the DrawerLayout
     * (2)The drawable will move on screen when opened  is pressed and off the screen when closed is pressed.
     * (3)Adds listener that will be notified of drawer events
     * (4)Syncronizes the state of the drawer indicator with the DrawerLayout
     * (5) Initializes the NavigationView with all the menu items and navigation header and sets the
     *     Navigation View Listener, where it sets the item selected
     * (6) Sets th initial fragment to the HomePageFragment
     * (7) Sets home as highlited in the Navigation View
     */
    public void initiateNavigationView()
    {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//(1)
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open, R.string.close);//(2)

        drawerLayout.addDrawerListener(drawerToggle);//(3)
        drawerToggle.syncState();//(4)

        //(5)
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //(6)
        HomePageFragment homePageFragment = new HomePageFragment();
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,homePageFragment);
        fragmentTransaction.commit();

        //(7)
        navigationView.setCheckedItem(R.id.home);
    }

    /**
     * When an item is selected in the Navigation View it sets the
     * toolbar title to the name of the item and goes to the following fragments
     * (1) the home page fragment
     * (2) Manual page fragment
     * (3) Observation List page fragment
     * (4) Previous Position List page fragment
     * (5) Preference page fragment
     *
     * (6)When close item selected it closes the activity
     * (7)After an item is selected the navigation View menu closes
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        //(1)
        if(item.getItemId() == R.id.home)
        {
            //goes to the home page fragment
            HomePageFragment homePageFragment = new HomePageFragment();
            android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,homePageFragment);
            fragmentTransaction.commit();

            //set the title of the toolbar to Manual
            toolbar.setTitle(R.string.app_home);
        }
        //(2)
        else if(item.getItemId() == R.id.manual)
        {
            //goes to the manual page fragment
            ManualPageFragment manualPageFragment = new ManualPageFragment();
            android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,manualPageFragment);
            fragmentTransaction.commit();

            //set the title of the toolbar to Manual
            toolbar.setTitle("Manual");
        }
        //(3)
        else if(item.getItemId() == R.id.observation_list)
        {
            //goes to the Observation List page fragment
            ObservationListPageFragment observationListFragment = new ObservationListPageFragment();
            android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,observationListFragment);
            fragmentTransaction.commit();

            //set the title of the toolbar to Observation List
            toolbar.setTitle("Observation List");
        }
        //(4)
        else if(item.getItemId() == R.id.previous_position_list)
        {
            //goes to the Previous Position List page fragment
            PreviousPositionPageFragment previousPositionFragment = new PreviousPositionPageFragment();
            android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,previousPositionFragment);
            fragmentTransaction.commit();

            //set the title of the toolbar to Observation List
            toolbar.setTitle("Previous Position List");
        }
        //(5)
        else if(item.getItemId() == R.id.preferences)
        {
            //goes to the Preference page fragment
            PreferencePageFragment preferenceFragment = new PreferencePageFragment();
            android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,preferenceFragment);
            fragmentTransaction.commit();

            //set the title of the toolbar to Preferences
            toolbar.setTitle("Preferences");
        }
        //(6)
        else if(item.getItemId() == R.id.quit)
        {
            finish();
            System.exit(0);
        }
        //(7)
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void locationUpdate(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}



