package cc.lecent.component.rfid;

/**
 * Create By：Pst on 2018/12/10 0010 15:13
 * DescriBe:
 */
public interface OnScanListener {
    void onScan(String cardId);
    void initSuccess();
    void initFail();
}
