package com.demo.bluetoothlib.thread;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.demo.bluetoothlib.callback.BluetoothDetectConnect;
import com.demo.bluetoothlib.common.BluetoothConstants;

import java.io.IOException;
import java.util.UUID;

public class BluetoothSever extends Thread {
    private static final String TAG = "BluetoothSever";
    private BluetoothServerSocket mmSeverSocket;
    private BluetoothDetectConnect bluetoothDetectConnect;

    public synchronized void setBluetoothDetectConnect(BluetoothDetectConnect bluetoothDetectConnect) {
        this.bluetoothDetectConnect = bluetoothDetectConnect;
    }

    public BluetoothSever(Context context, BluetoothAdapter bluetoothAdapter) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "BluetoothSever: permission android.permission.BLUETOOTH_CONNECT denied");
            return;
        }
        try {
            mmSeverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(BluetoothConstants.BLUETOOTH_NAME, UUID.fromString(BluetoothConstants.DEFAULT_UUID));
        } catch (IOException e) {
            Log.e(TAG, "BluetoothSever: ", e);
        }
    }

    @Override
    public void run() {
        BluetoothSocket socket = null;
        while(true){
            try {
                socket = mmSeverSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "run: ",e );
                break;
            }

            if (socket != null){
                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                BluetoothCommunicate bluetoothCommunicate = new BluetoothCommunicate(socket);
                bluetoothCommunicate.start();
                break;
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSeverSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
