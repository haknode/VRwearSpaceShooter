package at.fhooe.pro2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.google.unity.GoogleUnityActivity;
import com.unity3d.player.UnityPlayer;


/**
 * This class is derived from the activity used by google to provied the google vr support to unity
 *
 * creates a google message api client, tries to connect and registers a listener to this connection
 *
 * this classes are compiled into an android library and used inside of the unity project!
 */
public class MyGoogleUnityActivity extends GoogleUnityActivity implements GoogleApiClient.ConnectionCallbacks {
    private static final String LOG_TAG = "MyGoogleUnityActivity";


    private GoogleApiClient messageClient;

    SensorValuesMessageListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        listener = new SensorValuesMessageListener();

        messageClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();

        messageClient.connect();

        Log.d(LOG_TAG, "ctor()");
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(LOG_TAG, "onConnected!");

        Wearable.MessageApi.addListener(messageClient, listener );

        //send message to unity
        UnityPlayer.UnitySendMessage(Constants.UNITY_OBJECT_NAME, Constants.IS_CONNECTED_METHOD_NAME, "true");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(LOG_TAG, "onConnectionSuspended!");

        Wearable.MessageApi.removeListener(messageClient, listener );

        //send message to unity
        UnityPlayer.UnitySendMessage(Constants.UNITY_OBJECT_NAME, Constants.IS_CONNECTED_METHOD_NAME, "false");
    }
}
