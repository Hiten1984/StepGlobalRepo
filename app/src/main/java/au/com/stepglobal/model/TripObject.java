package au.com.stepglobal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hiten.bahri on 6/17/2017.
 */
public class TripObject extends BaseModel implements Parcelable {

    String responseType;
    String GUID;
    String userId;
    String deviceId;
    String tripType;
    String tripReason;
    long timeEnd;
    String status;

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getTripReason() {
        return tripReason;
    }

    public void setTripReason(String tripReason) {
        this.tripReason = tripReason;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(GUID);
        dest.writeString(userId);
        dest.writeString(deviceId);
//        dest.writeParcelable(tripType, flags);
        dest.writeString(tripType);
        dest.writeString(tripReason);
        dest.writeLong(timestart);
        dest.writeLong(timeEnd);
        dest.writeString(status);
    }

    public TripObject() {
    }

    protected TripObject(Parcel in) {
        GUID = in.readString();
        userId = in.readString();
        deviceId = in.readString();
        tripType = in.readString();
//        tripType = in.readParcelable(TripType.class.getClassLoader());
        tripReason = in.readString();
        timestart = in.readLong();
        timeEnd = in.readLong();
        status = in.readString();
    }

    public static final Creator<TripObject> CREATOR = new Creator<TripObject>() {
        @Override
        public TripObject createFromParcel(Parcel in) {
            return new TripObject(in);
        }

        @Override
        public TripObject[] newArray(int size) {
            return new TripObject[size];
        }
    };

}
