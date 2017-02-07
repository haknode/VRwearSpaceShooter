package at.fhooe.pro2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by stefan on 02.01.2017.
 */

/**
 * Listener for orientation changed sensor events
 */
public class OrientationSensorListener implements SensorEventListener {
    private static final String TAG = "OrientationSensorL";
    public static final int VALUE_SEND_TIMEOUT_TIME_MS = 200;

    private MessageApiClient client;

    private long lastSensorValueSentTime;
    private float[] acceleration;
    private float[] orientation;

    public OrientationSensorListener(Context context){
        client = MessageApiClient.getInstance(context);

        Log.d(TAG, "ctor()");
    }

    /**
     * Called whenever a sensor changed its value.
     * For the orientation we need the accelerometer and magnetic field sensor.
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        /*
          The sensor changed event is called more often if the device is NOT in "sleep" mode
         */

        //Do not send every sensor event to the phone (that would be to much data and the connection is too slow for that)
        if(System.currentTimeMillis() - lastSensorValueSentTime < VALUE_SEND_TIMEOUT_TIME_MS){
            return;
        }

        lastSensorValueSentTime = System.currentTimeMillis();

        //For the orientation we need the accelerometer and magnetic field sensor.
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            acceleration = event.values;

        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            orientation = event.values;

        //if values form the acceleromete and magnetic field sensor are available
        if(acceleration != null && orientation != null){

            //calculate the rotation and orientation matrices
            float[] rotation = new float[9];

            boolean success = SensorManager.getRotationMatrix(rotation, null, acceleration, orientation);

            if(success){
                float[] calculatedOrientation = new float[3];
                SensorManager.getOrientation(rotation, calculatedOrientation);

                client.sendSensorData(Constants.EVENT_ORIENTATION_CHANGED, calculatedOrientation);

                Log.d(TAG, "Sending data to GoogleApiClient");
            }
            else
                Log.e(TAG, "Could not get rotation matrix!");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
