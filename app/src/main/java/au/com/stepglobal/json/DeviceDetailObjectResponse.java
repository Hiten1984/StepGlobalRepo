package au.com.stepglobal.json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hiten.bahri on 6/23/2017.
 */
public class DeviceDetailObjectResponse {

    @SerializedName("devID")
    private String deviceId;

    @SerializedName("time")
    private long currentTime;

    public String getDeviceId() {
        return deviceId;
    }

    public long getCurrentTime() {
        return currentTime;
    }
}
