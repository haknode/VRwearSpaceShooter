package at.fhooe.pro2;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VRControllerActivity extends WearableActivity {

    private TextView mTextView;
    private Button reconnectButton;
    private BoxInsetLayout layoutAsFireButton;

    MySensorManager sensorManager;

    MessageApiClient messageClient;

    public static VRControllerActivity Instance;

    /**
     * Set up views, create instances of google api client and sensor menager and start listening for sensor values
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrcontroller);

        Instance = this;    //save the current instance so it is possible to use it from anywhere in the project

        messageClient = MessageApiClient.getInstance(this); //get an instance (create one if necessary) of the google message API client

        mTextView = (TextView) findViewById(R.id.text);
        mTextView.setText("init");
        reconnectButton = (Button) findViewById(R.id.reconnectButton);
        reconnectButton.setText("(re)connect");
        reconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageApiClient.getInstance(VRControllerActivity.this).forceReconnect();
            }
        });

        //make the howl background clickable (background is the fire button)
        layoutAsFireButton = (BoxInsetLayout) findViewById(R.id.container);
        layoutAsFireButton.setClickable(true);
        layoutAsFireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFireButtonClicked();
            }
        });

        //make the text also clickable (as fire button)
        mTextView.setClickable(true);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFireButtonClicked();
            }
        });

        setDisplayStatus(false);

        //start measuring sensor values
        sensorManager = new MySensorManager(this);
        sensorManager.startMeasurement();
    }

    /**
     * Update the content of the view accordingly to the connection status
     * @param connected
     */
    public void setDisplayStatus(final Boolean connected){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(connected){
                    mTextView.setText("Touch to shoot!");
                    reconnectButton.setVisibility(View.GONE);
                }
                else{
                    mTextView.setText("Not connected!");
                    reconnectButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * send the button click event to the phone
     */
    private void sendFireButtonClicked(){
        float[] data = new float[]{0};
        messageClient.sendSensorData(Constants.EVENT_BUTTON_CLICKED, data);

        indicateShotFired();
    }

    /**
     * lets the background of the view blink to indicate a shot
     */
    private void indicateShotFired(){
        int color = Color.TRANSPARENT;
        Drawable background = layoutAsFireButton.getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();

        final int oldColor = color;

        layoutAsFireButton.setBackgroundColor(Color.BLUE);
        layoutAsFireButton.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutAsFireButton.setBackgroundColor(oldColor);
            }
        }, 100);
    }
}
