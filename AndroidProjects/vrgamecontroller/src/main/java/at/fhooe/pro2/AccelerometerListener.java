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
/*

    Accelerometer not used!

 */

public class AccelerometerListener implements SensorEventListener {
    private static final String TAG = "AccelerometerListener";

    private MessageApiClient client;
    private float[] gravity;

    private long firstSensorEventTime;
    private long lastSensorEventTime;
    private long lastSensorValueSentTime;
    private float[] movement;
    private float[] velocity;

    public AccelerometerListener(Context context){
        client = MessageApiClient.getInstance(context);

        gravity = new float[]{SensorManager.GRAVITY_EARTH,SensorManager.GRAVITY_EARTH,SensorManager.GRAVITY_EARTH};
        movement = new float[]{0,0,0};
        velocity = new float[]{0,0,0};

        Log.w(TAG, "ctor()");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        /*
          The sensor changed event is called more often if the device is NOT in "sleep" mode
         */

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float[] linear_acceleration = new float[3];
            float alpha = 0.8f;

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            //linear_acceleration[0] = round2(linear_acceleration[0], 2);
            //linear_acceleration[1] = round2(linear_acceleration[1], 2);
            //linear_acceleration[2] = round2(linear_acceleration[2], 2);

            Log.d(TAG, "new acceleration value");

            workWithSensorData(event.sensor.getType(), linear_acceleration);
        }
    }

    public static float round2(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }

    public void workWithSensorData(final int sensorType, final float[] acceleration) {
        long time = System.currentTimeMillis();

        if(firstSensorEventTime == 0)
            firstSensorEventTime = time;

        if((time - firstSensorEventTime) < 500)
            return;

        if (lastSensorEventTime == 0 || lastSensorValueSentTime == 0){
            lastSensorEventTime = time;
            lastSensorValueSentTime = time;
            return;
        }

        float secondsSinceLastSensorEvent = (time - lastSensorEventTime) / 1000.0f;
        long timeSinceLastSensorValueSent = time - lastSensorValueSentTime;

        if(Math.abs(acceleration[0]) > 0.01)
            velocity[0] += acceleration[0] * secondsSinceLastSensorEvent;

        if(Math.abs(acceleration[1]) > 0.01)
            velocity[1] += acceleration[1] * secondsSinceLastSensorEvent;

        if(Math.abs(acceleration[2]) > 0.01)
            velocity[2] += acceleration[2] * secondsSinceLastSensorEvent;


        //if(Math.abs(velocity[0]) > 0.01)
            movement[0] += velocity[0] * secondsSinceLastSensorEvent;

        //if(Math.abs(velocity[1]) > 0.01)
            movement[1] += velocity[1] * secondsSinceLastSensorEvent;

        //if(Math.abs(velocity[2]) > 0.01)
            movement[2] += velocity[2] * secondsSinceLastSensorEvent;

        lastSensorEventTime = time;

        Log.d(TAG, "movement calculated");

        if (timeSinceLastSensorValueSent > 200) {    //should not be too small, the app crashes otherwise
            client.sendSensorData(Constants.EVENT_MOVEMENT_CHANGED, movement);
            lastSensorValueSentTime = time;

            movement[0] = 0;
            movement[1] = 0;
            movement[2] = 0;

            Log.d(TAG, "movement sent");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
