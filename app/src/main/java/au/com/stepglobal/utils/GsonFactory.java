package au.com.stepglobal.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Modifier;

import au.com.stepglobal.json.DeviceDetailObjectResponse;
import au.com.stepglobal.json.ReasonCodeResponse;
import au.com.stepglobal.json.TimeAndLocationObjectResponse;
import au.com.stepglobal.json.TripObjectResponse;
import au.com.stepglobal.model.TripObject;

/**
 * Configure and center a Gson instance.
 *
 * Created by hiten.bahri on 6/18/2017.
 */
public class GsonFactory {
    private static Gson gson;

    /**
     * @return Get our default gson implementation.
     */
    public static Gson getGson() {
        if(gson == null) {
            gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    .create();
        }
        return gson;
    }

    public static TripObjectResponse fromSampleJson(Context context, String path) {
        Gson gson = new Gson();
        Reader reader = getReader(context, path);
        return gson.fromJson(reader, TripObjectResponse.class);
    }

    public static DeviceDetailObjectResponse fromDeviceDetails(Context context, String path) {
        Gson gson = new Gson();
        Reader reader = getReader(context, path);
        return gson.fromJson(reader, DeviceDetailObjectResponse.class);
    }

    public static TimeAndLocationObjectResponse fromTimeAndLocation(Context context, String path) {
        Gson gson = new Gson();
        Reader reader = getReader(context, path);
        return gson.fromJson(reader, TimeAndLocationObjectResponse.class);
    }

    private static Reader getReader(Context context, String path) {
        return new InputStreamReader(context.getResources().openRawResource(
                context.getResources().getIdentifier(path, "raw", context.getPackageName())
        ));
    }

}
