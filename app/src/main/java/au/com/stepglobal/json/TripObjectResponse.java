package au.com.stepglobal.json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hiten.bahri on 6/18/2017.
 */
public class TripObjectResponse {

    @SerializedName("stoptime")
    private long stopTime;

    @SerializedName("starttime")
    private long startTime;

    @SerializedName("reasoncodes")
    private List<ReasonCodeResponse> reasonCodeList = new ArrayList<>();

    public long getStopTime() {
        return stopTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public List<ReasonCodeResponse> getReasonCodeList() {
        return reasonCodeList;
    }
}
