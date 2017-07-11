package au.com.stepglobal.model;

/**
 * Created by hiten.bahri on 09/07/2017.
 */

public class OdometerReading extends BaseModel{

    private String deviceId;
    private double odometer;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;

    }


}
