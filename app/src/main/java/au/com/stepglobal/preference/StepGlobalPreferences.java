package au.com.stepglobal.preference;

import android.content.Context;
import android.content.SharedPreferences;

import au.com.stepglobal.model.TripObject;

/**
 * Created by hiten.bahri on 6/17/2017.
 */
public class StepGlobalPreferences {

    private static final String SHARED_PREFERENCES_NAME = "STEP_GLOBAL_PREFERENCES";
    private static final String PROPERTY_USER_ID = "PROPERTY_USER_ID";
    private static final String PROPERTY_TRIP_TYPE = "PROPERTY_TRIP_TYPE";
    private static final String PROPERTY_TRIP_REASON = "PROPERTY_TRIP_REASON";
    private static final String PROPERTY_TRIP_START_TIME = "PROPERTY_TRIP_START_TIME";
    private static final String PROPERTY_TRIP_STOP_TIME = "PROPERTY_TRIP_STOP_TIME";
    private static final String PROPERTY_TRIP_GUID = "PROPERTY_TRIP_GUID";
    private static final String PROPERTY_TRIP_STATUS = "PROPERTY_TRIP_STATUS";
    private static final String PROPERTY_DEVICE_ID = "PROPERTY_DEVICE_ID";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void setTripDetails(Context c, TripObject tripObject) {
        final SharedPreferences prefs = getSharedPreferences(c);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_TRIP_GUID, tripObject.getGUID());
        editor.putString(PROPERTY_USER_ID, tripObject.getUserId());
        editor.putString(PROPERTY_DEVICE_ID, tripObject.getDeviceId());
        editor.putString(PROPERTY_TRIP_TYPE, tripObject.getTripType().toString());
        editor.putString(PROPERTY_TRIP_REASON, tripObject.getTripReason());
        editor.putLong(PROPERTY_TRIP_START_TIME, tripObject.getStartTime());
        editor.putLong(PROPERTY_TRIP_STOP_TIME, tripObject.getStopTime());
        editor.putString(PROPERTY_TRIP_STATUS, tripObject.getStatus());
        editor.commit();
    }

    public static TripObject getTripDetails(Context context) {
        TripObject tripObject = new TripObject();
        SharedPreferences prefs = getSharedPreferences(context);
        tripObject.setGUID(prefs.getString(PROPERTY_TRIP_GUID, null));
        tripObject.setUserId(prefs.getString(PROPERTY_USER_ID, null));
        tripObject.setDeviceId(prefs.getString(PROPERTY_DEVICE_ID, null));
        tripObject.setTripType(prefs.getString(PROPERTY_TRIP_TYPE, null));
//        tripObject.setTripType(getTripType(context));
        tripObject.setTripReason(prefs.getString(PROPERTY_TRIP_REASON, null));
        tripObject.setStartTime(prefs.getLong(PROPERTY_TRIP_START_TIME, -1L));
        tripObject.setStopTime(prefs.getLong(PROPERTY_TRIP_STOP_TIME, -1L));
        tripObject.setStatus(prefs.getString(PROPERTY_TRIP_STATUS, null));
        return tripObject;
    }

//    public static TripType getTripType(Context context) {
//        SharedPreferences prefs = getSharedPreferences(context);
//        String myEnumString = prefs.getString(PROPERTY_TRIP_TYPE, TripType.PRIVATE.toString());
//        return TripType.toTripType(myEnumString);
//    }

}

