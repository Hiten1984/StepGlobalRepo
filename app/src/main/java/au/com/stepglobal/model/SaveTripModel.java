package au.com.stepglobal.model;

/**
 * Created by hiten.bahri on 6/24/2017.
 *
 * This model is the final object that would be sent to the server when the trip is ended.
 *
 */
public class SaveTripModel {

    private String api;
    private String method;
    private String key;
    private TripObject tripObject;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TripObject getTripObject() {
        return tripObject;
    }

    public void setTripObject(TripObject tripObject) {
        this.tripObject = tripObject;
    }
}
