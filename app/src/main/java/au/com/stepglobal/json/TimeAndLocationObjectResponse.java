package au.com.stepglobal.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hiten.bahri on 6/23/2017.
 */
public class TimeAndLocationObjectResponse {

    @SerializedName("time")
    private long time;

    @SerializedName("lat")
    private long latitude;

    @SerializedName("lon")
    private long longitude;

    @SerializedName("gpstime")
    private long gpsTime;

    public long getTime() {
        return time;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public long getGpsTime() {
        return gpsTime;
    }
}
