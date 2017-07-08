package au.com.stepglobal.presenter;

/**
 * Created by hiten.bahri on 17/06/2017.
 */

public interface IUARTBasePresenter {
    public void onCreate();
    public void sendMessage(String message);
    public void onStart();
    public void onDestroy();
}
