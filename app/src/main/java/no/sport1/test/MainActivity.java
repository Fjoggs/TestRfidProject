package no.sport1.test;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import android_serialport_api.DataUtils;
import android_serialport_api.SerialPortManager;
import android_serialport_api.UHFHXAPI;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();
    private boolean isStop;
    UHFHXAPI api;
    private static final String TAG = "MainActivity";
    private String scanResult = "Scan something!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = new UHFHXAPI();
        api.setRegion(0x31); // Europe
    }

    public void readRFID(View v) {
        serialPortReader();
        TextView textView = (TextView)findViewById(R.id.scanResult);
        textView.setText(scanResult);
    }

    private void serialPortReader() {
        SerialPortManager.getInstance().openSerialPort();
        api.open();
        api.startAutoRead(0x22, new byte[]{0x00, 0x01},
                new UHFHXAPI.AutoRead() {

                    @Override
                    public void timeout() {
                        Log.i("whw", "timeout");
                    }

                    @Override
                    public void start() {
                        Log.i("whw", "start");
                    }

                    @Override
                    public void processing(byte[] data) {
                        String epc = DataUtils.toHexString(data).substring(4);
                        scanResult = epc;
                        handler.obtainMessage(1, epc).sendToTarget();
                        Log.i("whw", "data=" + epc);
                    }

                    @Override
                    public void end() {
                        Log.i("whw", "end");
                        Log.i("whw", "isStop=" + isStop);
                        if (!isStop) {
                            Log.i(TAG, "end: !isStop");
                        } else {
                            System.out.println("Am i here?");
                            handler.sendEmptyMessage(3);
                        }
                    }
                }
        );
    }
}
