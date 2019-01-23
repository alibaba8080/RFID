package cc.lecent.component.rfid;

import android.os.Build;
import android.util.Log;

import com.rscja.deviceapi.RFIDWithUHF;
import com.rscja.deviceapi.exception.ConfigurationException;

/**
 * Create By：Pst on 2018/12/10 0010 11:33
 * DescriBe:
 */
public class RFID extends Thread {
    private String TAG = this.getClass().getSimpleName();
    private static RFID ourInstance;
    private static final Object mLock = new Object();
    public RFIDWithUHF mReader;
    /**
     * 线程是否开启         --false:关闭状态  --true:开启状态
     */
    private boolean isClose = true;

    /**
     * 是否初始化          --false:未初始化  --true:已初始化
     */
    private boolean isInit = false;
    /**
     * 设备是否开启读卡状态 --false:关闭状态  --true:开启状态
     */
    private boolean isReadable = false;
    /**
     * 手动开启关闭设备     --false:关闭操作  --true:开启操作
     */
    private boolean handOpen = false;
    /**
     * 线程休眠
     */
    private int mBetween = 10;

    /**
     * 初始化超时限定
     */
    private int timeout = 0;

    private ScanHandler messageHandler;
    private OnScanListener listener;

    public static RFID getInstance() {
        if (ourInstance == null) {
            synchronized (mLock) {
                if (ourInstance == null) {
                    ourInstance = new RFID();
                }
            }
        }
        return ourInstance;
    }

    public RFID init(OnScanListener listener) {
        this.listener = listener;
        if (messageHandler == null) {
            messageHandler = new ScanHandler(listener);
        }
        return ourInstance;
    }

    private RFID() {

        try {
            mReader = RFIDWithUHF.getInstance();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        while (!isClose) {
            if (!isInit) {
                if (!(isInit = mReader.init())) {
                    timeout++;
                    if (timeout >= 5) {
                        isClose = true;
                        Log.e(TAG, "初始化失败");
                        if (null != messageHandler) {
                            messageHandler.onInitFail();
                        }
                    }
                } else {
                    Log.e(TAG, "初始化成功");
                    if (null != messageHandler) {
                        messageHandler.onInitSucc();
                    }
                }
            }

            if (isInit && handOpen) {

                if (!isReadable) {
                    // TODO: 2018/12/10 0010 开启扫描读卡状态
                    isReadable = mReader.startInventoryTag((byte) 1, 6);
                }
                if (isReadable) {
                    // TODO: 2018/12/10 0010 读取卡号
                    messageHandler.sendMessage(mReader);
                }
//                Log.e(TAG, "手动开启");
            } else {
//                Log.e(TAG, "手动关闭");
                if (isReadable) {
                    // TODO: 2018/12/10 0010 关闭扫描读卡状态
                    mReader.stopInventory();
                    isReadable = false;
                }
            }

            try {
                Thread.sleep(mBetween);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (mReader != null) {
            if (isReadable) {
                // TODO: 2018/12/10 0010 关闭扫描读卡状态
                mReader.stopInventory();
            }
            mReader.free();
            mReader = null;
        }
        timeout = 0;
        isReadable = false;
        isClose = true;
        handOpen = false;
        if (messageHandler != null) {
            messageHandler.removeCallbacksAndMessages(null);
            messageHandler = null;
        }
        ourInstance = null;
        Log.e("onScan", "关闭扫描");
    }

    @Override
    public synchronized void start() {
        if (this.isAlive()) {
            Log.e("onScan", "已经启动");
            return;
        }
        isClose = false;
        String carrier = Build.MODEL;
        if (null != carrier && carrier.equals("C71")) {
            // TODO: 2018/12/10 0010 c71设备自动开启读操作
            handOpen = true;
        }
        super.start();
    }

    public void setclose() {
        isClose = true;
    }

    public boolean openOrClose() {
        if (isInit) {
            handOpen = !handOpen;
        } else {
            if (null != messageHandler) {
                messageHandler.onInitFail();
            }
        }
        return handOpen;
    }


}
