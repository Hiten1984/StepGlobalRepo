package au.com.stepglobal.presenter;

import au.com.stepglobal.activity.view.IUARTBaseActivityView;
import au.com.stepglobal.connector.IUARTDataConnector;
import au.com.stepglobal.connector.UARTDataConnector;

/**
 * Created by hiten.bahri on 17/06/2017.
 */

public class UARTBasePresenterImpl implements IUARTBasePresenter, IUARTDataConnector.IUARTDataReceiver {


    IUARTBaseActivityView baseView;
    IUARTDataConnector connector;

    public UARTBasePresenterImpl(IUARTBaseActivityView view) {
        baseView = view;
    }

    @Override
    public void onCreate() {
        connector = new UARTDataConnector(this);
        connector.startReadThread(baseView.getApplicationContext());
    }

    @Override
    public void sendMessage(String message) {
        connector.sendData(message);
    }

    @Override
    public void onStart() {
        connector.createDeviceList();
    }

    @Override
    public void onDestroy() {
        connector.disconnectFunction();
    }

    @Override
    public void onDataReceive(String data) {
        baseView.onReceiveMessage(data);
    }
}
