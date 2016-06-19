package no.sport1.test;


import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.*;

public class SerialPortReader {

    private Thread readSerialDataThread;

    private SerialPort serialPort;
    private InputStream inStream;
    private OutputStream outStream;
    private boolean shouldRun = true;

    public SerialPortReader() {
    }

    protected void start() {
        try {
            File portLocation = new File("/dev/ttyS1");
            serialPort = new SerialPort(portLocation, 9600, 0);
            inStream = serialPort.getInputStream();
            outStream = serialPort.getOutputStream();
            sendBytes();
        } catch (IOException e) {
            Log.e("SerialPort", "IOException while opening serial port: " + e.getMessage());
            e.printStackTrace();

        }
        startThread();
    }

    protected void stop() {
        // break thread
        this.shouldRun = false;
        try {
            readSerialDataThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serialPort.close();
    }

    private void sendBytes() {
        // example how to send data to the opened serial port

        byte[] data = new byte[]{(byte) 0xFF, (byte) 0xAA, (byte) 0x64};
        try {
            outStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startThread() {
        readSerialDataThread = new Thread(new Runnable() {
            public void run() {
                while (shouldRun) {
                    int dataSize = 0;
                    try {
                        dataSize = inStream.available();

                        byte[] data = new byte[dataSize];
                        inStream.read(data);

                        processData(data);

                        Thread.sleep(50); // my serial sensor gives 20 Hz updates
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        readSerialDataThread.start();
    }

    private void processData(byte[] data) {
        for (byte singleByte : data) {
            System.out.println("data = " + singleByte);
        }
    }
}