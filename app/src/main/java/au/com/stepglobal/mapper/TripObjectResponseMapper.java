package au.com.stepglobal.mapper;

import java.util.ArrayList;
import java.util.List;

import au.com.stepglobal.json.DeviceDetailObjectResponse;
import au.com.stepglobal.json.TimeAndLocationObjectResponse;
import au.com.stepglobal.json.TripObjectResponse;
import au.com.stepglobal.model.DeviceDetail;
import au.com.stepglobal.model.ReasonCode;
import au.com.stepglobal.model.TimeAndLocation;

/**
 * Created by hiten.bahri on 6/18/2017.
 */
public class TripObjectResponseMapper {

    public static List<ReasonCode> getReasonCode(TripObjectResponse response) {
        List<ReasonCode> reasonCodeList = new ArrayList<>();
        for(int i=0; i < response.getReasonCodeList().size(); i++) {
            ReasonCode reasonCode = new ReasonCode();
            reasonCode.setReasonCode(response.getReasonCodeList().get(i).getReasonCode());
            reasonCode.setReasonName(response.getReasonCodeList().get(i).getReasonName());
            reasonCodeList.add(reasonCode);
        }
        return reasonCodeList;
    }

    public static long getStartTime(TripObjectResponse response) {
        return response.getStartTime();
    }

    public static long getStopTime(TripObjectResponse response) {
        return response.getStopTime();
    }

    public static DeviceDetail getDeviceDetail(DeviceDetailObjectResponse response) {
        DeviceDetail deviceDetail = new DeviceDetail();
        deviceDetail.setDeviceId(response.getDeviceId());
        deviceDetail.setCurrentTime(System.currentTimeMillis());
        return deviceDetail;
    }

    public static TimeAndLocation getTimeAndLocation(TimeAndLocationObjectResponse response) {
        TimeAndLocation timeAndLocation = new TimeAndLocation();
        timeAndLocation.setTime(System.currentTimeMillis());
        timeAndLocation.setLatitude(response.getLatitude());
        timeAndLocation.setLongitude(response.getLongitude());
        timeAndLocation.setGpsTime(response.getGpsTime());
        return timeAndLocation;
    }
}
