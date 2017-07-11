package au.com.stepglobal.model;

/**
 * Created by hiten.bahri on 25/06/2017.
 */

public class TripStatus {
    public static final int DEVICE_STATUS_NONE = -1;
    public static final int DEVICE_STATUS_GETTING_DEVICE_ID = 1;
    public static final int DEVICE_STATUS_DEVICE_ID_FAIL =2;
    public static final int DEVICE_STATUS_SETTING_STATUS = 3;
    public static final int DEVICE_STATUS_SETTING_FAIL = 4;
    public static final int DEVICE_STATUS_GETTING_TIME = 5;
    public static final int DEVICE_STATUS_GETTING_TIME_FAIL = 6;
    public static final int DEVICE_STATUS_SAVING_TRIP = 7;
    public static final int DEVICE_STATUS_SAVING_TRIP_FAIL = 8;
    public static final int DEVICE_STATUS_GETTING_ODOMETER = 9;
    public static final int DEVICE_STATUS_SETTING_ODOMETER = 10;

    private int status = DEVICE_STATUS_NONE;

    private static TripStatus mInstance = new TripStatus();

    private TripStatus() {

    }

    public static TripStatus getInstance() {
        return mInstance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
