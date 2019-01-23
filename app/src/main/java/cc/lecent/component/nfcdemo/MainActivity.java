package cc.lecent.component.nfcdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cc.lecent.component.rfid.RFID;
import cc.lecent.component.rfid.OnScanListener;

public class MainActivity extends Activity implements View.OnClickListener,OnScanListener {


    private Button buttonOpen;
    private Button openClose;
    private Button buttonShutdown;
    private TextView cardId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openClose = (Button) findViewById(R.id.open_close);
        buttonOpen = (Button) findViewById(R.id.button_open);
        buttonShutdown = (Button) findViewById(R.id.button_shutdown);
        buttonOpen.setOnClickListener(this);
        buttonShutdown.setOnClickListener(this);
        openClose.setOnClickListener(this);
        cardId = (TextView) findViewById(R.id.card_id);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_open:
                RFID.getInstance().init(this).start();
            case R.id.open_close:
                boolean stat=RFID.getInstance().openOrClose();

                break;
            case R.id.button_shutdown:
                RFID.getInstance().setclose();

                break;
        }
    }

    @Override
    public void onScan(String cardId) {
    Log.e("onScan",cardId);
    }

    @Override
    public void initSuccess() {
        Log.e("onScan","初始化 成功");
    }

    @Override
    public void initFail() {
        Log.e("onScan","初始化 失败");
        cardId.setText("");
    }


    @Override
    protected void onStart() {
        super.onStart();
        RFID.getInstance().init(this).start();
    }

    @Override
    protected void onPause() {
        RFID.getInstance().setclose();
        super.onPause();
    }
}
