package at.fhooe.pro2;

/**
 * Created by stefan on 29.01.2017.
 */

public class Constants {

    /* Name of the unity object that should receive the messages (must contain the methods!) */
    public static final String UNITY_OBJECT_NAME = "Player";

    /* The names of the methods inside of the unity object that should be executed when a specific message is sent */
    public static final String MOVEMENT_CHANGED_METHOD_NAME = "WearMovementChanged";
    public static final String ORIENTATION_CHANGED_METHOD_NAME = "WearOrientationChanged";
    public static final String BUTTON_CLICKED_METHOD_NAME = "WearButtonClicked";
    public static final String IS_CONNECTED_METHOD_NAME = "IsConnected";

    /* Event types */
    public static final int EVENT_MOVEMENT_CHANGED = 1;
    public static final int EVENT_ORIENTATION_CHANGED = 2;
    public static final int EVENT_BUTTON_CLICKED = 3;
}
