package com.example.bluey.digitalsextant;

/**
 * Created by robin_000 on 8/18/2017.
 */

public class Observation {

    public String ObservationName;
    public String ObservationInfo;

    public Observation()
    {
        this.ObservationName = "";
        this.ObservationInfo = "";
    }

    public String getObservationName(){return this.ObservationName;}
    public String getObservationInfo(){return this.ObservationInfo;}
    public void setObservationName(String name){this.ObservationName = name;}
    public void setObservationInfo(String info){this.ObservationInfo = info;}


}