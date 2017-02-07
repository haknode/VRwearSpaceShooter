package at.fhooe.pro2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class MySensorManager {
    private static final String TAG = "MySensorManager";

    SensorManager mSensorManager;

    //AccelerometerListener accelerometerListener;
    OrientationSensorListener orientationSensorListener;

    public MySensorManager(Context context){
        mSensorManager = ((SensorManager) context.getSystemService(Context.SENSOR_SERVICE));
        //accelerometerListener = new AccelerometerListener(context);
        orientationSensorListener = new OrientationSensorListener(context);

        Log.w(TAG, "ctor()");
    }

    /**
     * Registers the orientation event listener to the accelerometer and the magnetic field sensor
     */
    protected void startMeasurement() {
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (mSensorManager != null) {
            if (accelerometerSensor != null) {
                //accelerometer is not used
                //mSensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

                //to calculate the orientation we also need the accelerometer
                mSensorManager.registerListener(orientationSensorListener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

                Log.d(TAG, "Accelerometer listener registered");
            } else {
                Log.w(TAG, "No Accelerometer found");
            }

            if (magneticFieldSensor != null) {
                mSensorManager.registerListener(orientationSensorListener, magneticFieldSensor, SensorManager.SENSOR_DELAY_GAME);

                Log.d(TAG, "Magnetic Field listener registered");
            } else {
                Log.w(TAG, "No Magnetic Field found");
            }
        }
        Log.d(TAG, "Sensor listeners registered");
    }
}
