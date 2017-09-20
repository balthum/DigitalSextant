package com.example.bluey.digitalsextant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;

/**
 * Created by robinluna Robin Luna on 7/25/17.
 */
public class SensorModule implements SensorEventListener
{
    private SensorManager       sensorManager;//device sensor manager
    private String              direction = "N";//gets the compass direction
    private float               azimuth;// get your azimuth/height value
    private float               observedHeight; //get your accelerometer/compass bearing value
    private Sensor              magnetometerSensor, accelerometerSensor, rotationSensor;//the sensor types in your device
    private float               acceleration[] = new float[3]; //gravity rotational data
    private float               magnetic[] = new float[3]; //magnetic rotational data
    private float               rotation[] = new float[9];//rotational data
    private float               orientation[] = new float[9];// gets the azmiuth, pitch, and roll data
    private SensorChange        sensorChange;//sets the interface
    private boolean             haveSensor = false;
    private boolean             haveSensor2 = false;
    private Context             context;
    private boolean             mLastAccelerometerSet = false;
    private boolean             mLastMagnetometerSet = false;

    /**
     * Create an instance of the SensorModule.<br/>
     * When the SensorModule is constructed the following will be recorded:<br/><br/>
     *
     * (1)The SensorChanged interface is set.<br/>
     * (2)The Context is set.<br/>
     * (3)The sensorManger initializes your android device sensor capabilities.<br/>
     * (4)Sets the MagneticField sensor.<br/>
     * (5)Sets the accelerometer sensor.<br/>
     *
     * @param celestialBody CelestialBodyObservationFragmentActivity
     */
    public SensorModule(CelestialBodyObservationFragment celestialBody)
    {
        this.sensorChange = (SensorChange)celestialBody;//(1)
        this.context = celestialBody.getActivity();//(2)
        sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);//(3)

        //(4)
        //All values are in micro-Tesla (uT) and measure the ambient magnetic field
        // values[0] = X axis
        // values[1] = Y axis
        // values[2] = Z axis
       magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //(5)
        //sets the sensor to the type accelerometer
        //values[0] acceleration minus Gx on the x-axis
        //values[1] acceleration minus Gy on the y-axis
        //values[2] acceleration minus Gz on the z-axis
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    /**
     * When the phone is on pause the accelerometer and magnetic field sensors are unregistered from the SensorEventListener.
     */
    public void onPause()
    {
        if(haveSensor && haveSensor2)
        {
            sensorManager.unregisterListener(this,accelerometerSensor);
            sensorManager.unregisterListener(this,magnetometerSensor);
        }
        else
        {
            if(haveSensor)
            {
                sensorManager.unregisterListener(this,rotationSensor);
            }
        }

        //sensorManager.unregisterListener(this,accelerometerSensor);
        //sensorManager.unregisterListener(this, magnetometerSensor);
    }

    /**
     * When the phone starts resumes the accelerometer and magnetic field sensors are registered for the SensorEventListener.
     */
    public void onResume()
    {

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)==null)
        {
            if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)==null || sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)==null)
            {
                noSensorAlert();
            }
            else
            {
                accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

                haveSensor = sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = sensorManager.registerListener(this,magnetometerSensor,SensorManager.SENSOR_DELAY_UI);
            }
        }
        else
        {
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_UI);
        }
        //for the system's orientation sensor registered listeners
        //sensorManager.registerListener(this, magnetometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(this, accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void noSensorAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage("Your device doesn't support compass.");
        alertDialog.setCancelable(false);
        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    /**
     * Is a sensorEventListener requirement.<br/>
     * Is called when their is a new reading from the sensors which does the following:<br/><br/>
     *
     * (1)Checks if the sensor is a magnetic field and if so it clones all values in array.<br/>
     * (2)Checks if the sensor is a accelerometer and if so it clones all values in array.<br/>
     * (3)Computes the inclination and the rotation vector from the device so we can use the rotation to get the orientation.<br/>
     * (4)Sets the azimuth/compass bearing value where values[0] gets your azimuth angle around z-axis.<br/>
     * (5)Sets the height observed value and sets it with the interface.<br/>
     * (6)Gets the direction for the compass.<br/>
     * (7)Sets the compass bearing and direction with the interface.<br/>
     * @param sensorEvent SensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {

        if(sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR)
        {
            SensorManager.getRotationMatrixFromVector(rotation,sensorEvent.values);
            setAzimuth ((float) ((Math.toDegrees(SensorManager.getOrientation(rotation, orientation)[0])+360)%360));
        }

        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            System.arraycopy(sensorEvent.values,0,acceleration,0,sensorEvent.values.length);
            mLastAccelerometerSet = true;
        }

        if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            System.arraycopy(sensorEvent.values,0,magnetic,0,sensorEvent.values.length);
            mLastMagnetometerSet = true;
        }

        if(mLastMagnetometerSet && mLastAccelerometerSet)
        {
            SensorManager.getRotationMatrix(rotation,null,acceleration,magnetic);
            SensorManager.getOrientation(rotation,orientation);
            setAzimuth ((float) ((Math.toDegrees(SensorManager.getOrientation(rotation, orientation)[0])+360)%360));
        }

        this.observedHeight = (float) (90 + Math.toDegrees(SensorManager.getOrientation(rotation, orientation)[1] ));
        sensorChange.observedHeightUpdate(this.observedHeight);

        if (getAzimuth() >= 338 || getAzimuth() <= 22)
            setDirection("N");
        if(getAzimuth() < 338 && getAzimuth() > 292)
            setDirection("NW");
        if(getAzimuth() <= 292 && getAzimuth() > 247)
            setDirection("W");
        if(getAzimuth() <= 247 && getAzimuth() > 202)
            setDirection("SW");
        if(getAzimuth() <= 202 && getAzimuth() > 157)
            setDirection("S");
        if(getAzimuth() <= 157 && getAzimuth() > 112)
            setDirection("SE");
        if(getAzimuth() <= 112 && getAzimuth() > 67)
            setDirection("E");
        else if(getAzimuth() <= 67 && getAzimuth() > 22)
            setDirection("NE");

        sensorChange.compassUpdate(getDirection(), getAzimuth());

//        //(1)
//        //if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
//          //  magnetic = sensorEvent.values.clone();
//
//        //(2)
//        //if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
//          //  acceleration = sensorEvent.values.clone();
//
//        //(3)
//        SensorManager.getRotationMatrix(rotation, null, acceleration,magnetic);
//
//
//        float xAxis = ((float)(Math.toDegrees(orientation[1])+360)%360);// around the x axis
//
//        //if(xAxis >= 268 && xAxis <= 272)
//        //{
//            //(4)
//          //  setAzimuth((float) ((Math.toDegrees(SensorManager.getOrientation(rotation,orientation)[0])+360)%360));//around the z axis
//        //}
//
//        //(5)
//        this.observedHeight = (float) (90 + Math.toDegrees(SensorManager.getOrientation(rotation, orientation)[1] ));
//
//        sensorChange.observedHeightUpdate(this.observedHeight);
//
//        //(6)
//        if (getAzimuth() >= 338 || getAzimuth() <= 22)
//            setDirection("N");
//        if(getAzimuth() < 338 && getAzimuth() > 292)
//            setDirection("NW");
//        if(getAzimuth() <= 292 && getAzimuth() > 247)
//            setDirection("W");
//        if(getAzimuth() <= 247 && getAzimuth() > 202)
//            setDirection("SW");
//        if(getAzimuth() <= 202 && getAzimuth() > 157)
//            setDirection("S");
//        if(getAzimuth() <= 157 && getAzimuth() > 112)
//            setDirection("SE");
//        if(getAzimuth() <= 112 && getAzimuth() > 67)
//            setDirection("E");
//        else if(getAzimuth() <= 67 && getAzimuth() > 22)
//            setDirection("NE");
//
//        //(7)
//        sensorChange.compassUpdate(getDirection(), getAzimuth());
    }

    /**
     * Is a SensorEventListener requirement.<br/>
     * Is called when the accuracy of the registered sensor has changed.
     * @param sensor Sensor
     * @param i int
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    /**
     * Gets the azimuth/compass bearing
     * @return float
     */
    public float getAzimuth() {return azimuth;}

    /**
     * Gets the compass Direction (N,S,W,E)
     * @return String
     */
    public String getDirection(){return direction;}

    /**
     * Gets the observed height.
     * @return float
     */
    public float getObservedHeight(){return observedHeight;}

    /**
     * Sets the azimuth/compass bearing.
     * @param az float
     */
    public void setAzimuth(float az) {azimuth = az;}

    /**
     * Sets the compass Direction (N,S,W,E).
     * @param dir String
     */
    public void setDirection(String dir) {direction = dir;}

    /**
     * Sets the observed height.
     * @param observedHeight float
     */
    public void setObservedHeight(float observedHeight) {this.observedHeight = observedHeight;}


}







