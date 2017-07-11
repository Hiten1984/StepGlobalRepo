package au.com.stepglobal.model;

/**
 * Created by hiten.bahri on 10/07/2017.
 */

public  class BaseModel {
    public boolean isAckNeeded;
    long timestamp = -1;
    long timestart = -1;

    public boolean isAckNeeded() {
        return isAckNeeded;
    }

    public void setAckNeeded(boolean ackNeeded) {
        isAckNeeded = ackNeeded;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestart() {
        return timestart;
    }

    public void setTimestart(long timestart) {
        this.timestart = timestart;
    }
}
