package au.com.stepglobal.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hiten.bahri on 6/18/2017.
 */
public class ReasonCodeResponse {
    @SerializedName("reasoncode")
    private String reasonCode;

    @SerializedName("reasonname")
    private String reasonName;

    public String getReasonCode() {
        return reasonCode;
    }

    public String getReasonName() {
        return reasonName;
    }
}
