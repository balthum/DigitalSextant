package com.example.bluey.digitalsextant;

/**
 * Created by robinluna Robin Luna on 8/06/17.
 */
public class Preference
{
    public String       PreferenceName;//Name of the Preference
    public String       PreferenceInfo;//The Info in that specific Preference
    public int          PreferenceNum; // how many records is stored or when gps is updated

    /**
     * The Default constructor for Preference which initializes the following
     * (1) The Name of the Preference to null
     * (2) The Info of the Preference to null
     * (3) The Num of Preferences for records stored or when gps is updated, is set to 0
     */
    public Preference()
    {
        this.PreferenceName = "";//(1)
        this.PreferenceInfo = "";//(2)
        this.PreferenceNum = 0; //(3)
    }

    /**
     * Gets the Name of the Preference
     * @return String
     */
    public String getPreferenceName(){return this.PreferenceName;}

    /**
     * Gets the Info of the Preference
     * @return String
     */
    public String getPreferenceInfo(){return this.PreferenceInfo;}

    /**
     * Gets the Num of Preferences for records stored or when gps is updated
     * @return int
     */
    public int getPreferenceNum(){return this.PreferenceNum;}

    /**
     * Sets the Name of the Preference
     * @param name String
     */
    public void setPreferenceName(String name){this.PreferenceName = name;}

    /**
     * Sets the Info of the Preference
     * @param info String
     */
    public void setPreferenceInfo(String info){this.PreferenceInfo = info;}

    /**
     * Sets the Num of the Preference for records stored or when gps is updated
     * @param num int
     */
    public void setPreferenceNum(int num){this.PreferenceNum = num;}
}
