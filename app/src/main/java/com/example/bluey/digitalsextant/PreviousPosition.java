package com.example.bluey.digitalsextant;

/**
 * Created by robinluna Robin Luna on 8/25/17.
 */

public class PreviousPosition
{
    public int      PreviousPositionTitle;//Title of the previous position taken
    public int      PreviousPositionYear;//Year of the previous position taken
    public int      PreviousPositionMonth;//Month of the previous position taken
    public int      PreviousPositionDate;//Date of the previous position taken
    public int      PreviousPositionHour;//Hour of the previous position taken
    public int      PreviousPositionMinute;//Minute of the previous position taken
    public int      PreviousPositionSecond;//Second of the previous position taken
    public float   PreviousPositionLatitude;//Latitude of the previous position taken
    public float   PreviousPositionLongitude;//Longitude of the previous position taken

    /**
     * The Default constructor for PreviousPosition
     */
    public PreviousPosition() {}

    /**
     * Sets the Title of the position such as 1, 2, 3, and so on.
     * @param name int
     */
    public void setName(int name) {this.PreviousPositionTitle = name;}

    /**
     * Sets the year of when we got the position
     * @param year int
     */
    public void setYear(int year) {this.PreviousPositionYear = year;}

    /**
     * Sets the month of when we got the position.
     * @param month int
     */
    public void setMonth(int month) {this.PreviousPositionMonth = month;}

    /**
     * Sets the day of when we got the position.
     * @param date int
     */
    public void setDate(int date) {this.PreviousPositionDate = date;}

    /**
     * Sets the hour of when we got the position.
     * @param hour int
     */
    public void setHour(int hour) {this.PreviousPositionHour = hour;}

    /**
     * Sets the minute of when we got the position.
     * @param minute int
     */
    public void setMinute(int minute) {this.PreviousPositionMinute = minute;}

    /**
     * Sets the seconds of when we got the position.
     * @param second int
     */
    public void setSecond(int second) {this.PreviousPositionSecond = second;}

    /**
     * Sets the latitude of the position (neg = S and pos = N)
     * @param latitude double
     */
    public void setLatitude(float latitude) {this.PreviousPositionLatitude = latitude;}

    /**
     * Sets the longitude of the position (neg = W and pos = E)
     * @param longitude double
     */
    public void setLongitude(float longitude) {this.PreviousPositionLongitude = longitude;}

    /**
     * Gets the Title of the position.
     * @return int
     */
    public int getName() {return this.PreviousPositionTitle;}

    /**
     * Gets the year of when we got the position
     * @return int
     */
    public int getYear() {return this.PreviousPositionYear;}

    /**
     * Gets the month of when we got the position.
     * @return int
     */
    public int getMonth() {return this.PreviousPositionMonth;}

    /**
     * Gets the day of when we got the position.
     * @return int
     */
    public int getDate() {return this.PreviousPositionDate;}

    /**
     * Gets the hour of when we got the position.
     * @return int
     */
    public int getHour() {return this.PreviousPositionHour;}

    /**
     * Gets the minute of when we got the position.
     * @return int
     */
    public int getMinute() {return this.PreviousPositionMinute;}

    /**
     * Gets the seconds of when we got the position.
     * @return int
     */
    public int getSecond() {return this.PreviousPositionSecond;}

    /**
     * Gets the latitude of the position (neg = S and pos = N)
     * @return double
     */
    public float getLatitude() {return this.PreviousPositionLatitude;}

    /**
     * Gets the longitude of the position (neg = W and pos = E)
     * @return double
     */
    public float getLongitude() {return this.PreviousPositionLongitude;}

    /**
     * Is the 1st line of the what is shown for an PreviousPosition object, which contains the following
     * (1) the title of the position
     * @return String
     */
    public String line1() {return "Position " + this.PreviousPositionTitle;}

    /**
     * Is the 2nd line of the what is shown for an PreviousPosition object, which contains the following
     * (1) the date of the position
     * (2) the time of the position
     ** @return String
     */
    public String line2() {return "Date: " + this.PreviousPositionMonth + "/" + this.PreviousPositionDate + "/" + this.PreviousPositionYear +
            "      Time: " + this.PreviousPositionHour + this.PreviousPositionMinute;}

    /**
     * Is the 3rd line of the what is shown for an PreviousPosition object, which contains the following
     * (1) the latitude of the position
     * (2) the longitude of the position
     * @return String
     */
    public String line3()
    {return "Star Latitude: " + this.PreviousPositionLatitude + "       Longitude: " + this.PreviousPositionLongitude;}
}
