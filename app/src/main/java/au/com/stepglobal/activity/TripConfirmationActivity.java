package au.com.stepglobal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import au.com.stepglobal.R;
import au.com.stepglobal.activity.view.UARTBaseActivityView;
import au.com.stepglobal.global.BundleKey;
import au.com.stepglobal.global.TripType;
import au.com.stepglobal.mapper.TripObjectResponseMapper;
import au.com.stepglobal.model.DeviceDetail;
import au.com.stepglobal.model.GetOdometerModel;
import au.com.stepglobal.model.OdometerReading;
import au.com.stepglobal.model.ReasonCode;
import au.com.stepglobal.model.TimeAndLocation;
import au.com.stepglobal.model.TripObject;
import au.com.stepglobal.model.TripStatus;
import au.com.stepglobal.preference.StepGlobalPreferences;
import au.com.stepglobal.utils.GsonFactory;
import au.com.stepglobal.utils.StepGlobalConstants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static au.com.stepglobal.utils.StepGlobalConstants.SERVER_RESPONSE_WAIT;

/**
 * Created by hiten.bahri on 16/06/2017.
 */

public class TripConfirmationActivity extends UARTBaseActivityView {

    int WAIT_TIME = SERVER_RESPONSE_WAIT;
    static final int MESSAGE_GET_DEVICE_ID_SUCCESS = 1;
    static final int MESSAGE_GET_DEVICE_ID_TIMEOUT = 2;
    static final int MESSAGE_SET_STATUS_SUCCESS = 3;
    static final int MESSAGE_SET_STATUS_TIMEOUT = 4;
    static final int MESSAGE_GET_TIME_SUCCESS = 5;
    static final int MESSAGE_GET_TIME_TIMEOUT = 6;
    static final int MESSAGE_GET_ODOMETER_SUCCESS = 7;
    static final int MESSAGE_GET_ODOMETER_TIMEOUT = 8;
    static final int MESSAGE_SET_ODOMETER_SUCCESS = 9;
    static final int MESSAGE_SET_ODOMETER_TIMEOUT = 10;

    @BindView(R.id.trip_confirmation_user_id)
    TextView tripConfirmationUserId;
    @BindView(R.id.trip_confirmation_trip_type_spinner)
    Spinner tripTypeSpinner;
    @BindView(R.id.trip_confirmation_trip_reason_label)
    TextView tripReasonLabel;
    @BindView(R.id.trip_confirmation_trip_reason_spinner)
    Spinner tripTypeReasonSpinner;
    @BindView(R.id.trip_confirmation_odo_update_checkbox)
    CheckBox odoUpdateCheckbox;
    @BindView(R.id.trip_confirmation_odo_update_edit_text)
    EditText odoUpdateText;
    @BindView(R.id.trip_confirmation_button)
    Button startTripButton;

    private String userId;
    private TripType tripType;
    private String spinnerItem;
    private String reasonSpinnerItem;
    private String uniqueID;
    private String[] reasonCodes = null;

    private List<ReasonCode> reasonCodeResponse;

    private DeviceDetail deviceDetail;
    private TimeAndLocation timeAndLocation;

    private OdometerReading reading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_confirmation);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            odoUpdateCheckbox.setChecked(savedInstanceState.getBoolean("CHECKBOX_STATE"));
            setOdaTextState();
        }
        uniqueID = UUID.randomUUID().toString();
        reasonCodeResponse = TripObjectResponseMapper.getReasonCode(GsonFactory.fromSampleJson(getApplicationContext(), "stepglobalsample"));
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getExtras().getString(BundleKey.USER_ID.key);
            tripType = intent.getExtras().getParcelable(BundleKey.TRIP_TYPE.key);
        }
        if (userId != null) {
            tripConfirmationUserId.setText(userId.toString().toUpperCase());
        }
        initAdapter();
        initReasonAdapter();
        getDeviceDetail();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("CHECKBOX_STATE", odoUpdateCheckbox.isChecked());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onReceiveMessage(String message) {
        Log.i("TCA - OnReceive", "status: " + TripStatus.getInstance().getStatus());
        switch (TripStatus.getInstance().getStatus()) {
            // Device ID response
            case TripStatus.DEVICE_STATUS_GETTING_DEVICE_ID: {
                messageHandler.removeMessages(MESSAGE_GET_DEVICE_ID_TIMEOUT);
                DeviceDetail deviceDetail = GsonFactory.getGson().fromJson(message, DeviceDetail.class);
                Message msg = new Message();
                msg.what = MESSAGE_GET_DEVICE_ID_SUCCESS;
                msg.obj = deviceDetail;
                messageHandler.sendMessage(msg);
                break;
            }
            case TripStatus.DEVICE_STATUS_GETTING_TIME: {
                messageHandler.removeMessages(MESSAGE_GET_TIME_TIMEOUT);
                TimeAndLocation timeAndLocation = GsonFactory.getGson().fromJson(message, TimeAndLocation.class);
                Message msg = new Message();
                msg.what = MESSAGE_GET_TIME_SUCCESS;
                msg.obj = timeAndLocation;
                messageHandler.sendMessage(msg);
                break;
            }
            case TripStatus.DEVICE_STATUS_GETTING_ODOMETER: {
                messageHandler.removeMessages(MESSAGE_GET_ODOMETER_TIMEOUT);
                OdometerReading odometerReading = GsonFactory.getGson().fromJson(message, OdometerReading.class);
                Message msg = new Message();
                msg.what = MESSAGE_GET_ODOMETER_SUCCESS;
                msg.obj = odometerReading;
                messageHandler.sendMessage(msg);
                break;
            }
        }
    }

    public void initAdapter() {
        String[] list;
        if (tripType.display.equalsIgnoreCase(TripType.PRIVATE.toString())) {
            list = new String[]{tripType.display.toUpperCase(), TripType.BUSINESS.toString()};
        } else {
            list = new String[]{tripType.display.toUpperCase(), TripType.PRIVATE.toString()};
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripTypeSpinner.setAdapter(adapter);
        tripTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerItem = (String) parent.getItemAtPosition(position);
                hideTripReason(spinnerItem.equalsIgnoreCase(TripType.PRIVATE.toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setOdaTextState() {
        if (odoUpdateCheckbox.isChecked())
            odoUpdateText.setEnabled(true);
        else
            odoUpdateText.setEnabled(false);
    }

    public void initReasonAdapter() {
        ArrayAdapter<CharSequence> reasonAdapter;
        if (reasonCodes == null) {
            //TODO to be decided on list when reason codes are empty.
            List<String> reasonName = new ArrayList<>();
            for (int i = 0; i < reasonCodeResponse.size(); i++) {
                reasonName.add(reasonCodeResponse.get(i).getReasonName());
            }
            reasonAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item,
                    reasonName);
        } else {
            reasonAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item,
                    reasonCodes);
        }

        // Specify the layout to use when the list of choices appears
        reasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        tripTypeReasonSpinner.setAdapter(reasonAdapter);
        tripTypeReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reasonSpinnerItem = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void hideTripReason(boolean hide) {
        tripReasonLabel.setVisibility(hide ? View.GONE : View.VISIBLE);
        tripTypeReasonSpinner.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.trip_confirmation_button)
    public void startTripClicked() {
        getTime();
    }

    @OnClick(R.id.trip_confirmation_odo_update_checkbox)
    public void clickOdoUpdateCheckbox() {
        setOdaTextState();
    }


    Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.i("TCA - handleMessage", "msg: " + msg.what);
            switch (msg.what) {
                case MESSAGE_GET_DEVICE_ID_SUCCESS:
                    deviceDetail = (DeviceDetail) msg.obj;
                    Log.i("TCA - deviceDetail", "Device Id: "+deviceDetail.getDeviceId() + " , " + "Time : "+deviceDetail.getCurrentTime());
                    setAlert();
                    getOdometer();
                    break;
                case MESSAGE_GET_DEVICE_ID_TIMEOUT:
                    TripStatus.getInstance().setStatus(TripStatus.DEVICE_STATUS_DEVICE_ID_FAIL);
                    deviceDetail = TripObjectResponseMapper.getDeviceDetail(GsonFactory.fromDeviceDetails(getApplicationContext(), "devdetail"));
                    setAlert();
                    getOdometer();
                    break;
                case MESSAGE_GET_TIME_SUCCESS:
                    timeAndLocation = (TimeAndLocation) msg.obj;
                    TripStatus.getInstance().setStatus(TripStatus.DEVICE_STATUS_NONE);
                    if (odoUpdateText.isEnabled()) {
                        setOdometer(Double.parseDouble(odoUpdateText.getText().toString()));
                    }else {
                        sendStartTrip();
                    }
                    break;
                case MESSAGE_GET_TIME_TIMEOUT:
                    timeAndLocation = TripObjectResponseMapper.getTimeAndLocation(GsonFactory.fromTimeAndLocation(getApplicationContext(), "timeandlocation"));
                    TripStatus.getInstance().setStatus(TripStatus.DEVICE_STATUS_NONE);
                    if (odoUpdateText.isEnabled()) {
                        setOdometer(Double.parseDouble(odoUpdateText.getText().toString()));
                    } else {
                        sendStartTrip();
                    }
                    break;
                case MESSAGE_SET_STATUS_SUCCESS:
                    break;
                case MESSAGE_SET_STATUS_TIMEOUT:
                    break;
                case MESSAGE_GET_ODOMETER_SUCCESS:
                    reading = (OdometerReading) msg.obj;
                    Log.i("TCA - odometer", "Device Id: "+reading.getDeviceId() + " , " + "Odometer : "+reading.getOdometer());
                    odoUpdateText.setText(reading.getOdometer() != 0 ? Double.toString(reading.getOdometer()) : "0.0");
                    TripStatus.getInstance().setStatus(TripStatus.DEVICE_STATUS_NONE);
                    break;
                case MESSAGE_SET_ODOMETER_SUCCESS:
                    sendStartTrip();
                    break;
                case MESSAGE_GET_ODOMETER_TIMEOUT:
                    odoUpdateText.setText("0.0");
                    break;
                case MESSAGE_SET_ODOMETER_TIMEOUT:
                    sendStartTrip();
                    break;
            }
        }
    };

    public void setAlert() {
        TripConfirmationActivity.this.sendMessage(StepGlobalConstants.REQUEST_TYPE_ALERT + "CLR");
    }

    public void getTime() {
        TripConfirmationActivity.this.sendMessage(StepGlobalConstants.REQUEST_TYPE_TIME);
        TripStatus.getInstance().setStatus(TripStatus.DEVICE_STATUS_GETTING_TIME);
        messageHandler.sendEmptyMessageDelayed(MESSAGE_GET_TIME_TIMEOUT, WAIT_TIME);
    }

    public void getDeviceDetail() {
        sendMessage(StepGlobalConstants.REQUEST_TYPE_DEVICE_DETAIL);
        TripStatus.getInstance().setStatus(TripStatus.DEVICE_STATUS_GETTING_DEVICE_ID);
        messageHandler.sendEmptyMessageDelayed(MESSAGE_GET_DEVICE_ID_TIMEOUT, WAIT_TIME);
    }

    /**
     * This method is called to get odometer values from server.
     */
    public void getOdometer() {
        GetOdometerModel model = new GetOdometerModel();
        OdometerReading reading = new OdometerReading();
        reading.setDeviceId(deviceDetail.getDeviceId());
        model.setData(reading);
        sendMessage(GsonFactory.getGson().toJson(model));
        TripStatus.getInstance().setStatus(TripStatus.DEVICE_STATUS_GETTING_ODOMETER);
        messageHandler.sendEmptyMessageDelayed(MESSAGE_GET_ODOMETER_TIMEOUT, WAIT_TIME);
    }

    /**
     * This method is called to set odometer value if nothing is returned from server.
     * @param value - its the value inputted by user
     */
    public void setOdometer(double value) {
        GetOdometerModel model = new GetOdometerModel();
        OdometerReading reading = new OdometerReading();
        reading.setDeviceId(deviceDetail.getDeviceId());
        reading.setOdometer(value);
        reading.setAckNeeded(true);
        reading.setTimestamp(timeAndLocation.getTime());
        model.setData(reading);
        TripStatus.getInstance().setStatus(TripStatus.DEVICE_STATUS_SETTING_STATUS);
        messageHandler.sendEmptyMessageDelayed(MESSAGE_SET_ODOMETER_TIMEOUT, WAIT_TIME);
    }

    /**
     * This method will be called as soon as the user presses start trip button from the activity
     * It will save the trip object in shared preference to retrive if app is killed.
     */
    public void sendStartTrip() {
        TripObject tripObject = new TripObject();
        tripObject.setGUID(uniqueID);
        tripObject.setUserId(userId);
        tripObject.setDeviceId(deviceDetail.getDeviceId());
        tripObject.setTripType(TripType.toTripType(spinnerItem).toString().equals(TripType.PRIVATE.toString()) ? "P" : "B");
        tripObject.setTripReason(spinnerItem.equalsIgnoreCase(TripType.BUSINESS.toString()) ? reasonSpinnerItem : "");
        tripObject.setTimestart(timeAndLocation.getTime());
        tripObject.setStatus("Start");
        StepGlobalPreferences.setTripDetails(this, tripObject);
        Intent tripActivityIntent = new Intent(this, TripProgressActivity.class);
        tripActivityIntent.putExtra(BundleKey.TRIP_OBJECT.key, tripObject);
        startActivity(tripActivityIntent);
        finish();
    }

}
