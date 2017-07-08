package au.com.stepglobal.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.stepglobal.R;

import static au.com.stepglobal.model.SaveTripResponseModel.STATUS_CODE_INVALID_API;
import static au.com.stepglobal.model.SaveTripResponseModel.STATUS_CODE_INVALID_DEVICE_ID;
import static au.com.stepglobal.model.SaveTripResponseModel.STATUS_CODE_INVALID_DRIVER_ID;
import static au.com.stepglobal.model.SaveTripResponseModel.STATUS_CODE_INVALID_KEY;
import static au.com.stepglobal.model.SaveTripResponseModel.STATUS_CODE_INVALID_METHOD;
import static au.com.stepglobal.model.SaveTripResponseModel.STATUS_CODE_INVALID_TRIP_REASON;
import static au.com.stepglobal.model.SaveTripResponseModel.STATUS_CODE_INVALID_TRIP_TIME;
import static au.com.stepglobal.model.SaveTripResponseModel.STATUS_CODE_INVALID_TRIP_TYPE;
import static au.com.stepglobal.model.SaveTripResponseModel.STATUS_CODE_NO_ERROR;
import static au.com.stepglobal.model.SaveTripResponseModel.STATUS_CODE_TEMP_SYSTEM_FAILURE;

/**
 * Created by hiten.bahri on 24/06/2017.
 */

public class StepGlobalUtils {

    public static boolean isValidUserId(String userId) {
        if(userId.length() <= 8) {
            String nameMatcher = "^[a-zA-Z0-9]*$";
            Pattern pattern = Pattern.compile(nameMatcher);
            Matcher matcher = pattern.matcher(userId);
            return matcher.matches();
        }
        return false;
    }

    public static String getDateInFormat(long startTime) {
        String dateString = new SimpleDateFormat("HH:mm:ss").format(new Date(startTime));
        return dateString;
    }

    public static String getDiffInFormat(long startTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = simpleDateFormat.format(new Date(startTime));
        return dateString;
    }

    public static void updateTextView(TextView textView, long time) {
        String format = StepGlobalUtils.getDiffInFormat(time);
        textView.setText(DateFormat.format(format, time));
    }

    public static String getTripReasonCode(String tripReason) {
        String code = "";
        switch(tripReason) {
            case "Business Meeting":
                code = "BM";
                break;
            case "Site Inspection":
                code = "SI";
                break;
            case "Business To Office":
                code = "BO";
                break;
            case "To Depot":
                code = "DR";
        }
        return code;
    }

    public static void showDialog(Context context, int errorCode) {
        String message = getErrorMessage(context, errorCode);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO -- need to handle accordingly.
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private static String getErrorMessage(Context context, int errorCode) {
        String errorString = "";
        switch(errorCode) {
            case STATUS_CODE_INVALID_TRIP_TIME:
                errorString = context.getResources().getString(R.string.could_not_find_trip);
                break;
            case STATUS_CODE_INVALID_API:
                errorString = context.getResources().getString(R.string.invalid_api);
                break;
            case STATUS_CODE_INVALID_DEVICE_ID:
                errorString = context.getResources().getString(R.string.invalid_device_id);
                break;
            case STATUS_CODE_INVALID_DRIVER_ID:
                errorString = context.getResources().getString(R.string.invalid_driver_id);
                break;
            case STATUS_CODE_INVALID_KEY:
                errorString = context.getResources().getString(R.string.invalid_key);
                break;
            case STATUS_CODE_INVALID_METHOD:
                errorString = context.getResources().getString(R.string.invalid_method);
                break;
            case STATUS_CODE_INVALID_TRIP_REASON:
                errorString = context.getResources().getString(R.string.invalid_trip_reason);
                break;
            case STATUS_CODE_INVALID_TRIP_TYPE:
                errorString = context.getResources().getString(R.string.invalid_trip_type);
                break;
            case STATUS_CODE_TEMP_SYSTEM_FAILURE:
                errorString = context.getResources().getString(R.string.temporary_failure);
                break;
        }
        return errorString;
    }
}
