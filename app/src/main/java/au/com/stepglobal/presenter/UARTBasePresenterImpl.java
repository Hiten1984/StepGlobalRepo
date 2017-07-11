package au.com.stepglobal.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import au.com.stepglobal.activity.view.IUARTBaseActivityView;
import au.com.stepglobal.connector.IUARTDataConnector;
import au.com.stepglobal.connector.UARTDataConnector;
import au.com.stepglobal.model.messagequeue.StepGlobalMessageQueue;

/**
 * Created by hiten.bahri on 17/06/2017.
 */

public class UARTBasePresenterImpl implements IUARTBasePresenter, IUARTDataConnector.IUARTDataReceiver {


    IUARTBaseActivityView baseView;

    StepGlobalMessageQueue messageQueue;
    private static UARTBasePresenterImpl mPresenterImpl = new UARTBasePresenterImpl();

    private UARTBasePresenterImpl() {
    }

    public static UARTBasePresenterImpl getInstance(IUARTBaseActivityView view) {
        mPresenterImpl.baseView = view;
        return mPresenterImpl;
    }

    @Override
    public void onCreate() {
        messageQueue = StepGlobalMessageQueue.getInstance(baseView.getApplicationContext(),this);
        messageQueue.onCreate();
    }

    @Override
    public void sendMessage(String message) {
        messageQueue.addMessageWait(message);
    }

    @Override
    public void onStart() {
        messageQueue.onStart();
    }

    @Override
    public void onDestroy() {
        messageQueue.onDestroy();
    }

    @Override
    public void onDataReceive(String data) {
        messageQueue.checkMessageWaitLocked(data);
        if (baseView != null)
            baseView.onReceiveMessage(data);
    }
}
