package au.com.stepglobal.model.messagequeue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StepGlobalMessageQueueHandler extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        StepGlobalMessageQueue.getInstance(context.getApplicationContext(),null).sendPendingMessages();
    }
}
