package at.fhooe.pro2;

import android.util.Log;

import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.unity3d.player.UnityPlayer;

import java.nio.ByteBuffer;

/**
 * listener for the google message api client
 */
public class SensorValuesMessageListener implements MessageApi.MessageListener {
    private static final String LOG_TAG = "MessageListener";

    /**
     * message is called if a new message is received by the google message api client
     * @param messageEvent
     */
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        byte[] data = messageEvent.getData();

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        int type = byteBuffer.getInt(); //extract the event type from the byte array

        //Accelerometer (movement) not used
//        if(type == Constants.EVENT_MOVEMENT_CHANGED){
//            float x = byteBuffer.getFloat();
//            float y = byteBuffer.getFloat();
//            float z = byteBuffer.getFloat();
//
//            Log.d(LOG_TAG, "INCOMING DATA (type - x,y,z): " + type + " - " + x + "," + y + ","+ z);
//
//            UnityPlayer.UnitySendMessage(Constants.UNITY_OBJECT_NAME, Constants.MOVEMENT_CHANGED_METHOD_NAME, x + " " + y + " " + z);
//        }

        if(type == Constants.EVENT_ORIENTATION_CHANGED){
            float x = byteBuffer.getFloat();    //extract the sensor values from the buffer
            float y = byteBuffer.getFloat();
            float z = byteBuffer.getFloat();

            Log.d(LOG_TAG, "INCOMING DATA (type - x,y,z): " + type + " - " + x + "," + y + ","+ z);

            //send message to unity
            UnityPlayer.UnitySendMessage(Constants.UNITY_OBJECT_NAME, Constants.ORIENTATION_CHANGED_METHOD_NAME, x + " " + y + " " + z);

            return;
        }

        if(type == Constants.EVENT_BUTTON_CLICKED){
            float a = byteBuffer.getFloat();    //data not used

            Log.d(LOG_TAG, "INCOMING DATA (type - a): " + type + " - " + a);

            //send message to unity
            UnityPlayer.UnitySendMessage(Constants.UNITY_OBJECT_NAME, Constants.BUTTON_CLICKED_METHOD_NAME, "" + a);

            return;
        }

        Log.e(LOG_TAG, "Unknown event type!");

    }
}

