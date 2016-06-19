package no.sport1.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android_serialport_api.RFID;
import android_serialport_api.SerialPortManager;
import android_serialport_api.UHFHXAPI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serialPortReader();
    }

    private void serialPortReader() {
        SerialPortManager.getInstance().openSerialPort();
//        UHFHXAPI uhfhxapi = new UHFHXAPI();
//        uhfhxapi.open();
    }
}
