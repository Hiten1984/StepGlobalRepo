package au.com.stepglobal.connector;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

/**
 * Created by hiten.bahri on 17/06/2017.
 */

public class UARTDataConnector implements IUARTDataConnector {

    int actualNumBytes;
    int iReadIndex;
    final int UI_READ_BUFFER_SIZE = 10240;
    byte[] readBuffer;
    final int UPDATE_TEXT_VIEW_CONTENT = 0;
    Context mContext;
    public static D2xxManager ftD2xx = null;
    FT_Device ftDev;
    int DevCount = -1;
    int currentPortIndex = -1;
    final static String TT = "FTDI";
    int baudRate = 115200; /* baud rate */
    byte stopBit; /* 1:1stop bits, 2:2 stop bits */
    byte dataBit; /* 8:8bit, 7: 7bit */
    byte parity; /* 0: none, 1: odd, 2: even, 3: mark, 4: space */
    byte flowControl; /* 0:none, 1: CTS/RTS, 2:DTR/DSR, 3:XOFF/XON */
    public Context globalContext;
    String uartSettings = "";
    boolean uart_configured = false;
    int portIndex = -1;
    int iTotalBytes;
    // thread to read the data
    HandlerThread handlerThread; // update data to UI
    ReadThread readThread; // read data from USB

    byte[] readDataBuffer;

    final int MAX_NUM_BYTES = 65536;

    int totalReceiveDataBytes = 0;
    boolean bReadThreadEnable = false;

    ArrayAdapter<CharSequence> portAdapter;


    final byte XON = 0x11;    /* Resume transmission */
    final byte XOFF = 0x13;    /* Pause transmission */

    IUARTDataConnector.IUARTDataReceiver mDataReceiver;

    public UARTDataConnector(IUARTDataReceiver mDataReceiver) {
        this.mDataReceiver = mDataReceiver;

    }

    @Override
    public void startReadThread(Context context) {
        try {
            mContext = context;
            ftD2xx = D2xxManager.getInstance(context);
        } catch (D2xxManager.D2xxException e) {
            Log.e("FTDI_HT", "getInstance fail!!");
        }
        handlerThread = new HandlerThread(handler);
        handlerThread.start();
    }

    @Override
    public void createDeviceList() {
        createDevices();
        if (DevCount > 0) {
            connectFunction();
            setUARTInfoString();
            setConfig(baudRate, dataBit, stopBit, parity, flowControl);
        }
    }

    public void createDevices() {
        int tempDevCount = ftD2xx.createDeviceInfoList(globalContext);
        Log.i(TT, "Device Count- " + tempDevCount);
        if (tempDevCount > 0) {
            if (DevCount != tempDevCount) {
                DevCount = tempDevCount;
            }
        } else {
            DevCount = -1;
            currentPortIndex = -1;
        }
    }

    private void connectFunction() {
        if (portIndex + 1 > DevCount) {
            portIndex = 0;
        }

        if (currentPortIndex == portIndex
                && ftDev != null
                && true == ftDev.isOpen()) {
            //Toast.makeText(global_context,"Port("+portIndex+") is already opened.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (true == bReadThreadEnable) {
            bReadThreadEnable = false;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (null == ftDev) {
            ftDev = ftD2xx.openByIndex(mContext, portIndex);
        } else {
            ftDev = ftD2xx.openByIndex(mContext, portIndex);
        }
        uart_configured = false;

        if (ftDev == null) {
            return;
        }

        if (true == ftDev.isOpen()) {
            currentPortIndex = portIndex;
            //Toast.makeText(global_context, "open device port(" + portIndex + ") OK", Toast.LENGTH_SHORT).show();

            if (false == bReadThreadEnable) {
                readThread = new ReadThread(handler);
                readThread.start();
            }
        } else {
        }
    }

    void setConfig(int baud, byte dataBits, byte stopBits, byte parity, byte flowControl) {
        // configure port
        // reset to UART mode for 232 devices
        Log.i(TT, "Config Setting - " + baud + " , " + dataBits + " , " + stopBits + " , " + parity + " , " + flowControl);
        ftDev.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);

        ftDev.setBaudRate(baud);

        switch (dataBits) {
            case 7:
                dataBits = D2xxManager.FT_DATA_BITS_7;
                break;
            case 8:
                dataBits = D2xxManager.FT_DATA_BITS_8;
                break;
            default:
                dataBits = D2xxManager.FT_DATA_BITS_8;
                break;
        }

        switch (stopBits) {
            case 1:
                stopBits = D2xxManager.FT_STOP_BITS_1;
                break;
            case 2:
                stopBits = D2xxManager.FT_STOP_BITS_2;
                break;
            default:
                stopBits = D2xxManager.FT_STOP_BITS_1;
                break;
        }

        switch (parity) {
            case 0:
                parity = D2xxManager.FT_PARITY_NONE;
                break;
            case 1:
                parity = D2xxManager.FT_PARITY_ODD;
                break;
            case 2:
                parity = D2xxManager.FT_PARITY_EVEN;
                break;
            case 3:
                parity = D2xxManager.FT_PARITY_MARK;
                break;
            case 4:
                parity = D2xxManager.FT_PARITY_SPACE;
                break;
            default:
                parity = D2xxManager.FT_PARITY_NONE;
                break;
        }

        ftDev.setDataCharacteristics(dataBits, stopBits, parity);

        short flowCtrlSetting;
        switch (flowControl) {
            case 0:
                flowCtrlSetting = D2xxManager.FT_FLOW_NONE;
                break;
            case 1:
                flowCtrlSetting = D2xxManager.FT_FLOW_RTS_CTS;
                break;
            case 2:
                flowCtrlSetting = D2xxManager.FT_FLOW_DTR_DSR;
                break;
            case 3:
                flowCtrlSetting = D2xxManager.FT_FLOW_XON_XOFF;
                break;
            default:
                flowCtrlSetting = D2xxManager.FT_FLOW_NONE;
                break;
        }

        ftDev.setFlowControl(flowCtrlSetting, XON, XOFF);

        setUARTInfoString();

        uart_configured = true;
    }

    private class HandlerThread extends Thread {
        Handler mHandler;

        HandlerThread(Handler h) {
            mHandler = h;
        }

        public void run() {
            byte status;
            Message msg;

            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                status = readData(UI_READ_BUFFER_SIZE, readBuffer);
                Log.i(TT, "Status returned - " + status);
                if (0x00 == status) {
                    msg = mHandler.obtainMessage(UPDATE_TEXT_VIEW_CONTENT);
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

    class ReadThread extends Thread {
        final int USB_DATA_BUFFER = 8192;

        Handler mHandler;

        ReadThread(Handler h) {
            mHandler = h;
            this.setPriority(MAX_PRIORITY);
        }

        public void run() {
            byte[] usbData = new byte[USB_DATA_BUFFER];
            int readCount = 0;
            int iWriteIndex = 0;
            bReadThreadEnable = true;

            while (bReadThreadEnable) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (iTotalBytes > (MAX_NUM_BYTES - (USB_DATA_BUFFER + 1))) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                readCount = ftDev.getQueueStatus();
                //Log.e(">>@@","iavailable:" + iavailable);
                if (readCount > 0) {
                    if (readCount > USB_DATA_BUFFER) {
                        readCount = USB_DATA_BUFFER;
                    }
                    ftDev.read(usbData, readCount);
                    {
                        totalReceiveDataBytes += readCount;
                        Log.i(TT, "totalReceiveDataBytes: " + totalReceiveDataBytes);

                        Log.i(TT, "readcount: " + readCount);
                        for (int count = 0; count < readCount; count++) {
                            readDataBuffer[iWriteIndex] = usbData[count];
                            iWriteIndex++;
                            iWriteIndex %= MAX_NUM_BYTES;
                        }

                        if (iWriteIndex >= iReadIndex) {
                            iTotalBytes = iWriteIndex - iReadIndex;
                        } else {
                            iTotalBytes = (MAX_NUM_BYTES - iReadIndex) + iWriteIndex;
                        }


                    }
                }
            }

        }
    }

    byte readData(int numBytes, byte[] buffer) {
        byte intstatus = 0x00; /* success by default */

		/* should be at least one byte to read */
        if ((numBytes < 1) || (0 == iTotalBytes)) {
            actualNumBytes = 0;
            intstatus = 0x01;
            return intstatus;
        }

        if (numBytes > iTotalBytes) {
            numBytes = iTotalBytes;
        }

		/* update the number of bytes available */
        iTotalBytes -= numBytes;
        actualNumBytes = numBytes;

		/* copy to the user buffer */
        for (int count = 0; count < numBytes; count++) {
            buffer[count] = readDataBuffer[iReadIndex];
            iReadIndex++;
            iReadIndex %= MAX_NUM_BYTES;
        }

        return intstatus;
    }

    int totalUpdateDataBytes = 0;
    char[] readBufferToChar;


    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT_VIEW_CONTENT:
                    if (actualNumBytes > 0) {
                        totalUpdateDataBytes += actualNumBytes;
                        for (int i = 0; i < actualNumBytes; i++) {
                            readBufferToChar[i] = (char) readBuffer[i];
                        }
                        String data = String.copyValueOf(readBufferToChar, 0, actualNumBytes);
                        if (mDataReceiver != null) {
                            mDataReceiver.onDataReceive(data);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void sendData(String data) {
        byte[] writeBuffer = {};
        if (ftDev == null) {
            return;
        }
        if (ftDev.isOpen() == false) {
            return;
        }
        int numBytes = data.length();
        for (int i = 0; i < numBytes; i++) {
            writeBuffer[i] = (byte) (data.charAt(i));
        }
        ftDev.write(writeBuffer, numBytes);
    }

    void setUARTInfoString() {
        String parityString, flowString;

        switch (parity) {
            case 0:
                parityString = new String("None");
                break;
            case 1:
                parityString = new String("Odd");
                break;
            case 2:
                parityString = new String("Even");
                break;
            case 3:
                parityString = new String("Mark");
                break;
            case 4:
                parityString = new String("Space");
                break;
            default:
                parityString = new String("None");
                break;
        }

        switch (flowControl) {
            case 0:
                flowString = new String("None");
                break;
            case 1:
                flowString = new String("CTS/RTS");
                break;
            case 2:
                flowString = new String("DTR/DSR");
                break;
            case 3:
                flowString = new String("XOFF/XON");
                break;
            default:
                flowString = new String("None");
                break;
        }

        uartSettings = "Port " + portIndex + "; UART Setting  -  Baudrate:" + baudRate + "  StopBit:" + stopBit
                + "  DataBit:" + dataBit + "  Parity:" + parityString
                + "  FlowControl:" + flowString;

    }

    public void disconnectFunction() {
        DevCount = -1;
        currentPortIndex = -1;
        bReadThreadEnable = false;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (ftDev != null) {
            if (true == ftDev.isOpen()) {
                ftDev.close();
            }
        }
    }
}
