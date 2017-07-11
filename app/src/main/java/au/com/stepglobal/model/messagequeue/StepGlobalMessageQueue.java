package au.com.stepglobal.model.messagequeue;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import au.com.stepglobal.connector.IUARTDataConnector;
import au.com.stepglobal.connector.UARTDataConnector;
import au.com.stepglobal.model.BaseModel;
import au.com.stepglobal.preference.StepGlobalPreferences;
import au.com.stepglobal.utils.GsonFactory;

/**
 * Created by hiten.bahri on 10/07/2017.
 */

public class StepGlobalMessageQueue implements IUARTDataConnector.IUARTDataReceiver {
    private static int REPEAT_INTERVAL = 1000 * 60 * 10;
    private static StepGlobalMessageQueue instance = null;
    List<String> messageQueue = new ArrayList<>();
    Context mContext;
    AlarmManager alarmManager;
    IUARTDataConnector.IUARTDataReceiver receiver = null;
    IUARTDataConnector connector;

    private StepGlobalMessageQueue(Context context) {
        mContext = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        startQueue();
    }

    public void onCreate() {
        if (connector == null)
            connector = new UARTDataConnector(this);
        connector.startReadThread(mContext.getApplicationContext());
    }

    public void onStart() {
        connector.createDeviceList();
    }

    public void onDestroy() {
        receiver = null;
        //connector.disconnectFunction();
    }

    /**
     * this method would start the receiver to poll the message queue if any and sets an
     * alarm manager which will continue polling after the set interval time has passed.
     *
     * Currently Alarm manager makes sure to poll every 10 minutes and can be configured
     * as per desired repeat interval time.
     */
    private void startQueue() {
        Intent intent = new Intent(mContext, StepGlobalMessageQueueHandler.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (REPEAT_INTERVAL),
                REPEAT_INTERVAL, alarmIntent);
    }

    public static StepGlobalMessageQueue getInstance(Context context,IUARTDataConnector.IUARTDataReceiver receiver) {
        if (instance == null) {
            instance = new StepGlobalMessageQueue(context);
        }
        if(instance.receiver == null && receiver != null)
             instance.receiver = receiver;
        return instance;
    }

    /**
     * This method is for adding a message to the queue when we set odometer manually
     * or when we stop the trip.
     * @param message - type of message to be added to queue.
     */
    public void addMessageWait(String message) {
        synchronized (this) {
            boolean isValidJson = true;
            try {
                new JSONObject(message);
            } catch (JSONException e) {
                isValidJson = false;
            }
            Log.i("MessageQueue", "valid json - "+isValidJson);
            if (isValidJson) {
                BaseModel baseModel = GsonFactory.getGson().fromJson(message, BaseModel.class);
                Log.i("MessageQueue", "start time - "+baseModel.getTimestart() + " , "+ "time stamp : "+baseModel.getTimestamp());
                if (baseModel.getTimestart() != -1 || baseModel.getTimestamp() != -1)
                    messageQueue.add(message);
                StepGlobalPreferences.saveMessageList(mContext, GsonFactory.getGson().toJson(messageQueue));
            }
            connector.sendData(message);
        }
    }

    public void checkMessageWaitLocked(String message) {
        synchronized (this) {
            int i = 0;
            int size = messageQueue.size();
            if (size <= 0) {
                messageQueue = Arrays.asList(GsonFactory.getGson().fromJson(StepGlobalPreferences.getMessageList(mContext), String[].class));
            }
            Log.i("MessageQueue", "Message Queue Size - "+size);
            size = messageQueue.size();
            while (i < size) {
                String messages = messageQueue.get(i);
                BaseModel baseModel = GsonFactory.getGson().fromJson(message, BaseModel.class);
                BaseModel inQueue = GsonFactory.getGson().fromJson(messages, BaseModel.class);
                if (baseModel.getTimestamp() != -1 && inQueue.getTimestamp() == baseModel.getTimestamp()) {
                    messageQueue.remove(i);
                    size--;
                } else if (baseModel.getTimestart() != -1 && inQueue.getTimestart() == baseModel.getTimestart()) {
                    messageQueue.remove(i);
                    size--;
                } else {
                    i++;
                }
            }
        }
    }

    public void sendPendingMessages() {
        onCreate();
        onStart();
        for (String message : messageQueue) {
            connector.sendData(message);
        }
    }

    @Override
    public void onDataReceive(String data) {
        if(receiver!= null) {
            receiver.onDataReceive(data);
        }
        checkMessageWaitLocked(data);
    }
}
