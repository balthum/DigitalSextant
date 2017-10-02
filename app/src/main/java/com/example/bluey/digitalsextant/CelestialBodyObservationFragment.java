package com.example.bluey.digitalsextant;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.transition.Visibility;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CelestialBodyObservationFragment extends Fragment implements SensorChange
{
    //** -------------------------------------------------------------------------------------**\\
    // Square Robot Code
    //------------------------------ Used with the camera --------------------------------------\\
    private final static String         TAGCAM = "CAM2_API";                // Used to tag CAM items in the using Log API.
    private   TextureView               camPreviewTextureView;              // Used display Camera preview.
    private   String                    cameraId;                           // The name of Camera 0 on the device.
    protected CameraDevice              cameraDevice;                       // Used to interact with the camera.
    protected CameraCaptureSession      cameraCaptureSessions;              // Used to capture the preview from the camera.
    protected CaptureRequest.Builder    captureRequestBuilder;              //
    private Size                        imageDimensions;                    // Object that has the image size and dimensions.
    private final static int            REQUEST_CAMERA_PERMISSION = 200;
    private Handler                     mBackgroundHandler;
    private HandlerThread               mBackgroundThread;
    //-------------------------------------------------------------------------------------------//

    private SensorModule sensorModule;
    private TextView                    compassTextView, ObshTextView;
    private View                        view;
    private String                      spinnerCelestialBodyName;                 // The current selected star in the spinner menu
    private double                      spinnerDeclination;                  //
    private double                      spinnerSHA;
    private float                       compassBearing;                      // Observed compass bearing
    private String                      compassDirection;
    private float                       observedHeight;                      // Observed Height
    private Spinner                     celestialBodySpinner;
    private ImageView                   upArrowImage, downArrowImage;
    private ImageView                   leftArrowImage, rightArrowImage;
    private Button                      takeFixButton, compassButton;
    private boolean                     isCompassButtonpushed = false;
    private boolean                     isupwards = false;



    // Listerner's \\

    // Used with displaying the camera
    TextureView.SurfaceTextureListener camPreviewTextureListener    = new TextureView.SurfaceTextureListener()
    {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1)
        {
            Log.i(TAGCAM, "OnSurface Texture Available, execute openCamera");
            openCamera();

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1)
        {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture)
        {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture)
        {

        }
    };

    // Call Back's \\
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback()
    {
        @Override
        public void onOpened(@NonNull CameraDevice camera)
        {
            //This is called when the camera is open
            Log.e(TAGCAM, "onOpened");
            cameraDevice = camera;

            createCameraPreview();
        }
        @Override
        public void onDisconnected(@NonNull CameraDevice camera)
        {
            cameraDevice.close();
        }
        @Override
        public void onError(@NonNull CameraDevice camera, int error)
        {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    /**
     *
     *  Prepare the Camera Preview.
     *
     *      1.) Create an object to capture the frames from the camera image stream.
     *      2.) Set the Texture View Surface Buffer size to the camera frame size.
     *      3.) Create a weak reference to the surface object that will display the camera frame.
     *      4.)
     *      5.) Create Capture Session state call back.
     *
     *          a.) Check to see if the Camera device is null, if it is return the method.
     *          b.) Get a copy of the current camera capture session object and
     *              run the preview updater.
     */
    private void createCameraPreview()
    {
        try
        {
            // (1)
            SurfaceTexture surfaceTexture = camPreviewTextureView.getSurfaceTexture();
            assert surfaceTexture != null;
            Log.e(TAGCAM, "createCameraPreview Step: 1");
            // (2)
            surfaceTexture.setDefaultBufferSize(imageDimensions.getWidth(), imageDimensions.getHeight());
            Log.e(TAGCAM, "createCameraPreview Step: 2");
            // (3)
            Surface surface = new Surface(surfaceTexture);
            Log.e(TAGCAM, "createCameraPreview Step: 3");

            // (4) //TODO Comment capture Request Builder
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            // (5)
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback()
                    {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession)
                        {
                            // (a)
                            if (null == cameraDevice) return;
                            Log.e(TAGCAM, "createCameraPreview Step: a");
                            // (b)
                            CelestialBodyObservationFragment.this.cameraCaptureSessions = cameraCaptureSession;
                            Log.e(TAGCAM, "createCameraPreview Step: b");
                            updatePreview();
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession)
                        {

                        }
                    }
                    , null);

        }
        catch (Exception ee)
        {
            ee.printStackTrace();
            Log.i(TAGCAM, "Issues with Preview Call Back");
        }

    }

    /**
     *
     *      1.) Ensure that the cameraDevice is not null; log and return the method if it is.
     *      2.) Build the Camera Capture Request.
     *      3.) Setup the Camera Capture Session.
     */
    protected void updatePreview()
    {
        // (1)
        if (null == cameraDevice )
            Log.e(TAGCAM, "update preview, camera device = null, returned");
        Log.e(TAGCAM, "UpdatePreview Step: 1");
        // (2)
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try
        {
            // (3)
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        }
        catch (Exception ee)
        {
            ee.printStackTrace();
        }
    }

    /**
     *  Open's the Camera located on the back of the device for use.
     *
     *    1.) Establish a connection to the Camera Device.
     *    2.) Get the Camera ID for Camera 0, which is the on the back of the device.
     *    3.) Get the characteristics for Camera 0.
     *    4.) Get the minimum frame duration and stall durations for each format and size the camera uses.
     *    5.) Get the image dimensions that Camera 0 has that match the SurfaceTexture View class.
     *    6.) Add permission to Camera 0 and let the user grant the permissions.
     *    7.) Open Camera 0 for use with its stateCallback.
     *
     */
    private void openCamera()
    {
        // (1)
        CameraManager manger = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        Log.i(TAGCAM, "openCamera");

        try
        {
            // (2)
            cameraId = manger.getCameraIdList()[0];
            // (3)
            CameraCharacteristics cameraCharacteristics = manger.getCameraCharacteristics(cameraId);
            // (4)
            StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert null != map;
            // (5)
            imageDimensions = map.getOutputSizes(SurfaceTexture.class)[0];
            // (6)
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]
                        {
                                Manifest.permission.CAMERA
                        }, REQUEST_CAMERA_PERMISSION);
                return;
            }
            // (7)
            manger.openCamera(cameraId, stateCallback, null);
            Log.i(TAGCAM, "Camera " + cameraId + " is open.");

        }
        catch (Exception ee)
        {
            ee.printStackTrace();
            Log.i(TAGCAM, "Camera " + cameraId + " did not open.");
        }
    }

    //** -------------------------------------------------------------------------------------**\\

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable()
    {
        @SuppressLint("InlinedApi")
        @Override
        public void run()
        {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    //    private final Runnable mShowPart2Runnable = new Runnable()
//    {
//        @Override
//        public void run()
//        {
//            // Delayed display of UI elements
//            ActionBar actionBar = getSupportActionBar();
//            if (actionBar != null) {
//                actionBar.show();
//            }
//            mControlsView.setVisibility(View.VISIBLE);
//        }
//    };
    private boolean mVisible;
//    private final Runnable mHideRunnable = new Runnable()
//    {
//        @Override
//        public void run()
//        {
//            hide();
//        }
//    };
//    /**
//     * Touch listener to use for in-layout UI controls to delay hiding the
//     * system UI. This is to prevent the jarring behavior of controls going away
//     * while interacting with activity UI.
//     */
//    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener()
//    {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent)
//        {
//            if (AUTO_HIDE) {
//                delayedHide(AUTO_HIDE_DELAY_MILLIS);
//            }
//            return false;
//        }
//    };

    @SuppressLint("ValidFragment")
    public CelestialBodyObservationFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sensorModule = new SensorModule(this);//TODO Robin Debug Sensor Issues
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_celestial_body_fix, container, false);

        mVisible = true;
        mControlsView       = view.findViewById(R.id.fullscreen_content_controls);
        mContentView        = view.findViewById(R.id.camPreviewTextView);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //              toggle();
            }
        });


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //view.findViewById(R.id.takeFixButton).setOnTouchListener(mDelayHideTouchListener);

        //**-------------- Square Robot OnCreate additions ------------------------------------**\\
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        camPreviewTextureView =         view.findViewById(R.id.camPreviewTextView);
        ObshTextView        =         view.findViewById(R.id.textView_observedHeight);     //TODO comment
        compassTextView       =         view.findViewById(R.id.textView_compass);    //TODO comment
        celestialBodySpinner =  view.findViewById(R.id.starListSpinner);

        leftArrowImage = view.findViewById(R.id.imageView_leftArrow);
        rightArrowImage = view.findViewById(R.id.imageView_rightArrow);
        upArrowImage = view.findViewById(R.id.imageView_upArrow);
        downArrowImage = view.findViewById(R.id.imageView_downArrow);

        leftArrowImage.setVisibility(View.INVISIBLE);
        rightArrowImage.setVisibility(View.INVISIBLE);
        upArrowImage.setVisibility(View.INVISIBLE);
        downArrowImage.setVisibility(View.INVISIBLE);


        getSpinner();

        if ( null == camPreviewTextureView) {Log.e(TAGCAM, "Cam Preview Texture is null");}
        else {camPreviewTextureView.setSurfaceTextureListener(camPreviewTextureListener);}


        pushedCelestialBodyButton();

        pushCompassButton();

        MainActivity.toolbar.setVisibility(View.GONE);

        CelestialBodyObservationDialog myDialog = new CelestialBodyObservationDialog(this);
        myDialog.show(getFragmentManager(), "instructions");


        return view;
    }

    public void getSpinner()
    {
        final CelestialBodyDatabaseManager celestialBodyDatabaseManager = new CelestialBodyDatabaseManager(getActivity());

        // Set up Spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                ( getActivity(), R.layout.spinner_text_layout,
                        celestialBodyDatabaseManager.getCelestialBodyNames() );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        celestialBodySpinner.setAdapter(arrayAdapter);

        celestialBodySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerCelestialBodyName = adapterView.getItemAtPosition(i).toString();
                spinnerDeclination = celestialBodyDatabaseManager.getCelestialBodyDeclination(i);
                spinnerSHA = celestialBodyDatabaseManager.getCelestialBodySiderealHourAngle(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void pushedCelestialBodyButton()
    {
        this.takeFixButton = view.findViewById(R.id.takeFixButton);
        this.takeFixButton.setEnabled(false);

        if ( null == camPreviewTextureView ) {Log.e(TAGCAM, " Cam takeFixButton is null");}
        else {
            takeFixButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    getSensorData();
                    Toast.makeText(getActivity(),"OBSERVATIONS ADDED", Toast.LENGTH_SHORT).show();


                    ObservationListPageFragment observationListFragment = new ObservationListPageFragment();
                    android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,observationListFragment);
                    fragmentTransaction.commit();


                    MainActivity.navigationView.setCheckedItem(R.id.observation_list);

                    MainActivity.toolbar.setVisibility(View.VISIBLE);

                    //set the title of the toolbar to Observation List
                    MainActivity.toolbar.setTitle("Observation List");

                }
            });
        }
    }

    private void getSensorData()
    {
        CelestialBodyObservation observation = new CelestialBodyObservation();
        ObservationDataManager observationDataManager = new ObservationDataManager(getActivity());
        ArrayList<CelestialBodyObservation> arrayList = new ArrayList<>(observationDataManager.getObservationFromDatabase());

        observation.setTitle("Observation " + (arrayList.size() + 1));

        // Get the Star name and add to the observation
        observation.setCelestialBodyName(spinnerCelestialBodyName);

        observation.setCompassHeading(compassBearing);
        observation.setHeightObserver(observedHeight);
        observation.setCompassDirection(compassDirection);

        arrayList.add(observation);
        observationDataManager.updateObservationDatabase(arrayList);
    }



    public void pushCompassButton()
    {

        compassButton = view.findViewById(R.id.compassButton);

        compassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(leftArrowImage.getVisibility() == View.INVISIBLE && rightArrowImage.getVisibility() == View.INVISIBLE)
                {
                    takeFixButton.setEnabled(true);
                    compassButton.setEnabled(false);
                    isCompassButtonpushed = true;
                }

                takeFixButton.setEnabled(true);
                compassButton.setEnabled(false);
                isCompassButtonpushed = true;

//                else
//                {
//                    Toast.makeText(getActivity(), "COMPASS BEARING NOT taken, make sure to follow the arrows", Toast.LENGTH_LONG).show();
//                }
            }
        });

    }


    @Override
    public void onPause()
    {
        super.onPause();
        sensorModule.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        sensorModule.onResume();
    }

    public double getFutureCompassBearing()
    {
        CelestialMath celestialMath = new CelestialMath();

        PreviousPositionDataManager previousPositionDataManager = new PreviousPositionDataManager(getActivity());
        ArrayList<PreviousPosition> positionArrayList = new ArrayList<>(previousPositionDataManager.getPositionFromDatabase());

        PreviousPosition position = positionArrayList.get(0);

        return celestialMath.starBearingFromAssumedPosition(spinnerDeclination,spinnerSHA,position.getLongitude(), position.getLatitude());
    }

    @Override
    public void compassUpdate(String direction, float azimuth) {

        double futureCompassBearing = getFutureCompassBearing();

        int roundCompass = (int) azimuth;
        roundCompass = Math.round(azimuth);
        compassTextView.setText(String.format("Compass: %dº %s", roundCompass, direction));

        if (isCompassButtonpushed)
        {}
        else
        {
            compassBearing = azimuth;
            compassDirection = direction;

            if (futureCompassBearing + 5 >= azimuth && futureCompassBearing - 5 <= azimuth)
            {
                rightArrowImage.setVisibility(View.INVISIBLE);
                leftArrowImage.setVisibility(View.INVISIBLE);
            }
            else
            {
                if (futureCompassBearing > 180)
                {
                    if (futureCompassBearing > azimuth && (futureCompassBearing - azimuth) <= 180)
                    {
                        leftArrowImage.setVisibility(View.INVISIBLE);
                        rightArrowImage.setVisibility(View.VISIBLE);
                    }
                    else if (futureCompassBearing > azimuth && (futureCompassBearing - azimuth) > 180)
                    {
                        rightArrowImage.setVisibility(View.INVISIBLE);
                        leftArrowImage.setVisibility(View.VISIBLE);
                    }
                    else if (futureCompassBearing < azimuth)
                    {
                        rightArrowImage.setVisibility(View.INVISIBLE);
                        leftArrowImage.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    if (futureCompassBearing < azimuth && (azimuth - futureCompassBearing) <= 180)
                    {
                        rightArrowImage.setVisibility(View.INVISIBLE);
                        leftArrowImage.setVisibility(View.VISIBLE);
                    }
                    else if (futureCompassBearing < azimuth && (azimuth - futureCompassBearing) > 180)
                    {
                        leftArrowImage.setVisibility(View.INVISIBLE);
                        rightArrowImage.setVisibility(View.VISIBLE);
                    }
                    else if (futureCompassBearing > azimuth)
                    {
                        leftArrowImage.setVisibility(View.INVISIBLE);
                        rightArrowImage.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void observedHeightUpdate(float observedHeight)
    {
        ObshTextView.setText(String.format("OBSh: %.1fº", observedHeight));
        this.observedHeight = observedHeight;

        if(isCompassButtonpushed)
        {
            if(isupwards)
            {
                if(spinnerDeclination > observedHeight)
                {
                    downArrowImage.setVisibility(View.INVISIBLE);
                    upArrowImage.setVisibility(View.VISIBLE);
                }

                else if(spinnerDeclination < observedHeight)
                {
                    upArrowImage.setVisibility(View.INVISIBLE);
                    downArrowImage.setVisibility(View.VISIBLE);
                }
                else if(spinnerDeclination + 2 >= observedHeight && spinnerDeclination - 2 <= observedHeight)
                {
                    upArrowImage.setVisibility(View.INVISIBLE);
                    downArrowImage.setVisibility(View.INVISIBLE);
                }
            }
            else
            {
                if (Math.round((double)observedHeight) == 1)
                {
                    isupwards = true;
                }
                else
                {
                    downArrowImage.setVisibility(View.INVISIBLE);
                    upArrowImage.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}