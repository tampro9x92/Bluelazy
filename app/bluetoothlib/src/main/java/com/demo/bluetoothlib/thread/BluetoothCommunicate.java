package com.demo.bluetoothlib.thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Date 07/10/2024
 * Communicate with bluetooth device
 */
public class BluetoothCommunicate extends Thread {
    private static final String TAG = "BluetoothCommunicate";
    private final BluetoothSocket mmSocket;

    public BluetoothCommunicate(BluetoothSocket mmSocket) {
        this.mmSocket = mmSocket;
    }

    @Override
    public void run() {
        while (mmSocket.isConnected()) {
            try {
                InputStream inputStream = mmSocket.getInputStream();
                readData(inputStream);
//                    OutputStream outputStream = mmSocket.getOutputStream();
//                    writeData(outputStream);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "run: ", e);
                }
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch (IOException ex) {
                    Log.e(TAG, "Close socket fail ", e);
                }
                break;
            }
        }
    }

    private boolean readData(InputStream inputStream) {
        try {
            int singleByte = inputStream.read();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append((char) singleByte);
            Log.d(TAG, "readData: " + stringBuffer.toString());
            return true;
        } catch (IOException e) {
            Log.e(TAG, "readData: ", e);
            return false;
        }
    }

    private void writeData(OutputStream outputStream) {

    }
}
