package au.com.stepglobal.connector;

import android.content.Context;

/**
 * Created by hiten.bahri on 17/06/2017.
 */

public interface IUARTDataConnector {


    void startReadThread(Context context);

    void createDeviceList();

    interface IUARTDataReceiver {
        void onDataReceive(String data);
    }

    void sendData(String data);
    void disconnectFunction();
}
