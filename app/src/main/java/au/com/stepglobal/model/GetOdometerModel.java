package au.com.stepglobal.model;

/**
 * Created by hiten.bahri on 09/07/2017.
 */

public class GetOdometerModel  {
    String api = "lmuLogbookApi";
    String method = "getOdometer";
    String key = "key123";
    OdometerReading data;

    public OdometerReading getData() {
        return data;
    }

    public void setData(OdometerReading data) {
        this.data = data;
    }
}
