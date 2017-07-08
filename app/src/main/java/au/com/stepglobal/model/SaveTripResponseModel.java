package au.com.stepglobal.model;

/**
 * Created by hiten.bahri on 25/06/2017.
 */

public class SaveTripResponseModel {
    public static final int STATUS_CODE_NO_ERROR = 0;
    public static final int STATUS_CODE_INVALID_TRIP_TIME = 1;
    public static final int STATUS_CODE_INVALID_DRIVER_ID = 2;
    public static final int STATUS_CODE_INVALID_KEY = 3;
    public static final int STATUS_CODE_INVALID_API = 4;
    public static final int STATUS_CODE_INVALID_METHOD = 5;
    public static final int STATUS_CODE_INVALID_DEVICE_ID = 6;
    public static final int STATUS_CODE_INVALID_TRIP_TYPE = 7;
    public static final int STATUS_CODE_INVALID_TRIP_REASON = 8;
    public static final int STATUS_CODE_TEMP_SYSTEM_FAILURE = 9;
    public static final int STATUS_CODE_UNKNOWN_ERROR = 10;

    private String status;
    private int statusCode;
    private String statusText;
    private long timeStart;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }
}
