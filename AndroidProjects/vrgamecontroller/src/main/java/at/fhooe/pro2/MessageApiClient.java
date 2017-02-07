package at.fhooe.pro2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.nio.ByteBuffer;

public class MessageApiClient implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "MessageApiClient";

    public static MessageApiClient instance;

    //singelton
    public static MessageApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new MessageApiClient(context.getApplicationContext());
        }

        return instance;
    }

    private String  nodeId;
    private GoogleApiClient googleApiClient;

    /**
     * create a google wear  api client object
     * and try to connect
     * @param context
     */
    private MessageApiClient(Context context) {
        googleApiClient = new GoogleApiClient.Builder(context)
                            .addApi(Wearable.API)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .build();

        googleApiClient.connect();
    }

    /**
     * Sends data to the phone via the google message api client
     * the data is encoded into an array
     * @param eventType
     * @param data
     */
    public void sendSensorData(final int eventType, final float[] data){
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + 4 * data.length);
        byteBuffer.putInt(eventType);   //ecnode eventType

        for(float d : data){    //encode data
            byteBuffer.putFloat(d);
        }

        if(googleApiClient != null && nodeId != null){
            Log.d(TAG, "sending message" );
            Wearable.MessageApi.sendMessage(googleApiClient, nodeId, "data", byteBuffer.array());
        }
        else
        {
            Log.e(TAG, "error while sending message googleApiClient="+googleApiClient+" nodeId="+nodeId );
        }
    }

    /**
     * disconnects and reconnects the google api client
     */
    public void forceReconnect(){
        googleApiClient.disconnect();
        googleApiClient.connect();
    }

    /**
     * called when a connection was established
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");

        connectAndGetDeviceNode();
    }

    /**
     * get the connected devices and use he first device as communication partner.
     * ! does not work with multiple connected devices!
     */
    private void connectAndGetDeviceNode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( googleApiClient ).await();

                if(!nodes.getNodes().isEmpty()){
                    nodeId = nodes.getNodes().get(0).getId();
                    VRControllerActivity.Instance.setDisplayStatus(true);
                    Log.d(TAG, "connected to device!");
                }
                else{
                    VRControllerActivity.Instance.setDisplayStatus(false);

                    Log.e(TAG, "NOT connected!");
                }
            }
        }).start();
    }

    @Override
    public void onConnectionSuspended(int i) {
        nodeId = null;

        Log.e(TAG, "onConnectionSuspended");

        VRControllerActivity.Instance.setDisplayStatus(false);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: "+connectionResult.getErrorMessage()+"!");

        VRControllerActivity.Instance.setDisplayStatus(false);
    }
}
