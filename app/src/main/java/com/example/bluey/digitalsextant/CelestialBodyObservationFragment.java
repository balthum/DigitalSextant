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
import android.support.annotation.NonNull;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
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

    private SensorModule                sensorModule;
    private TextView                    compassTextView, zenithTextView, picAngleTextView;
    private View                        view;
    private String                      spinnerPositionName;                 // The current selected item in the spinner menu
    private float                       compassBearing;                      // Observed compass bearing
    private String                      compassDirection;
    private float                       observedHeight;                      // Observed Height



    // Database Items \\
    public List<CelestialBody>          celestialBodiesSet;                  // Set of celestial bodies



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
            // (2)
            surfaceTexture.setDefaultBufferSize(imageDimensions.getWidth(), imageDimensions.getHeight());
            // (3)
            Surface surface = new Surface(surfaceTexture);
            // (4) //TODO Comment capture Request Builder
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            // (5)
            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback()
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
        if (null == cameraDevice ) { Log.e(TAGCAM, "update preview, camera device = null, returned"); }
        Log.e(TAGCAM, "UpdatePreview Step: 1");
        // (2)
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try
        {
            // (3)
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        }
        catch (Exception ee) {   ee.printStackTrace();    }
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


    @SuppressLint("ValidFragment")
    public CelestialBodyObservationFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sensorModule = new SensorModule(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_celestial_body_fix, container, false);

        boolean mVisible = true;
        View mControlsView = view.findViewById(R.id.fullscreen_content_controls);
        //mContentView        = view.findViewById(R.id.camPreviewTextView);

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
        zenithTextView        =         view.findViewById(R.id.textView_zenith);     //TODO comment
        compassTextView       =         view.findViewById(R.id.textView_compass);    //TODO comment
        Spinner celestialBodySpinner =  view.findViewById(R.id.starListSpinner);

        populateCelestialBodies();

        // Set up Spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (getActivity(), R.layout.spinner_item, celestialBodyNames()) ;
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        celestialBodySpinner.setAdapter(arrayAdapter);

        celestialBodySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerPositionName = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(), "Selected : "+ spinnerPositionName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if ( null == camPreviewTextureView) {Log.e(TAGCAM, "Cam Preview Texture is null");}
        else {camPreviewTextureView.setSurfaceTextureListener(camPreviewTextureListener);}

        Button takeFixButton = view.findViewById(R.id.takeFixButton);
        if ( null == camPreviewTextureView ) {Log.e(TAGCAM, " Cam takeFixButton is null");}
        else {
            takeFixButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSensorData();

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



        MainActivity.toolbar.setVisibility(View.GONE);


        return view;
    }

        private void getSensorData()
    {
        CelestialBodyObservation observation = new CelestialBodyObservation();
        ObservationDataManager observationDataManager = new ObservationDataManager(getActivity());
        ArrayList<CelestialBodyObservation> arrayList = new ArrayList<>(observationDataManager.getObservationFromDatabase());

        observation.setTitle("Observation " + (arrayList.size() + 1));

        // Get the Star name and add to the observation
        observation.setCelestialBodyName(spinnerPositionName);

        observation.setCompassHeading(compassBearing);
        observation.setHeightObserver(observedHeight);
        observation.setCompassDirection(compassDirection);

        arrayList.add(observation);
        observationDataManager.updateObservationDatabase(arrayList);
    }

    /**
     *  populate the Celestial Bodies Database
     *
     *  1.) Create the celestial Bodies set as any ArrayList.
     *  2.) Open the celestial_bodies_text_list file as an read buffer.
     *  3.) read in the file line by line until the end of the file.
     *      Each line of the file as one entry with each of it fields separated by commas.
     *      a.) Split the line using comma's as separation for elements into an array.
     *      b.) Create a new Celestial Body Object. and set the Bodies Name, Sidereal Hour Angle,
     *          and Declination.
     *      c.) Add the Celestial body to the set.
     */
    public void populateCelestialBodies()
    {
        // 1.)
        celestialBodiesSet = new ArrayList<>();
        String fileLine;
        int i = 0;

        try {
            // 2.)
            InputStream inputStream = getActivity().getResources().openRawResource(R.raw.celestial_bodies_text_list);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // 3.)
            while ((fileLine = bufferedReader.readLine()) != null) {
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
            Toast.makeText(getActivity().getApplicationContext(), "Unable to load Celestial Bodies", Toast.LENGTH_LONG).show();
        }
    }

    public String[] celestialBodyNames()
    {
        // 1.)
        String[] celestialBodyNames = new String[celestialBodiesSet.size()];
        int                       i = 0;

        for (CelestialBody aCelestialBodiesSet : celestialBodiesSet) {
            celestialBodyNames[i++] = aCelestialBodiesSet.getCelestialBodyName();
        }

        return celestialBodyNames;
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

    @Override
    public void compassUpdate(String direction, float azimuth)
    {
        compassTextView.setText("COMPASS: " + azimuth + "º " + direction);
        compassBearing = azimuth;
        compassDirection = direction;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void zenithUpdate(float zenith)
    {
        zenithTextView.setText(String.format("ZENITH: %.1fº", zenith));
        observedHeight = zenith;
    }
}
