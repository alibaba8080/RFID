package cc.lecent.component.rfid;


import android.os.Handler;
import android.os.Message;

import com.rscja.deviceapi.RFIDWithUHF;

import java.text.DecimalFormat;

/**
 * Create By：Pst on 2018/12/10 0010 14:19
 * DescriBe:
 */
public class ScanHandler extends Handler {
    private OnScanListener listener;
    private static final byte INIT_FAIL = 1;
    private static final byte INIT_SUCC = 2;
    private static final byte MESSAGE = 3;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.arg1) {
            case INIT_FAIL:
                if (listener != null) {
                    listener.initFail();
                }
                break;
            case INIT_SUCC:
                if (listener != null) {
                    listener.initSuccess();
                }
                break;
            case MESSAGE:
                if (listener != null) {
                    String idCard = msg.obj + "";
                    listener.onScan(idCard);
                }
                break;
            default:
                break;
        }

    }

    public ScanHandler(OnScanListener listener) {
        this.listener = listener;
    }

    public void sendMessage(RFIDWithUHF mReader) {
        String[] buffer = null;
        String result = null;
        String idCart;
        buffer = mReader.readTagFromBuffer();
        Message msg = this.obtainMessage();
        if (buffer != null && buffer.length > 0) {
            result = mReader.convertUiiToEPC(buffer[1]);
            idCart = result.split("@")[0];
            msg.arg1 = MESSAGE;
            msg.obj = idCart;
            this.sendMessage(msg);
        }

    }

    public void onInitFail() {
        Message msg = this.obtainMessage();
        msg.arg1 = INIT_FAIL;
        this.sendMessage(msg);
    }

    public void onInitSucc() {
        Message msg = this.obtainMessage();
        msg.arg1 = INIT_SUCC;
        this.sendMessage(msg);
    }

    public static void main(String[] args) {
        double number = 0.0;

        System.out.println(number);                            //12.0

        System.out.println(Double.toString(number));        //12.0

        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        System.out.println(decimalFormat.format(number));    //12
    }
}




