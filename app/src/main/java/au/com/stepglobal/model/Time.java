package au.com.stepglobal.model;

import static au.com.stepglobal.utils.Constant.REQUEST_TYPE_TIME;

/**
 * Created by hiten.bahri on 18/06/2017.
 */

public class Time {
    private String requestType = REQUEST_TYPE_TIME;
    private long time;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
