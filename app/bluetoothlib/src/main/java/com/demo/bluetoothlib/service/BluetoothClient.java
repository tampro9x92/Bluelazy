package com.demo.bluetoothlib.service;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.demo.bluetoothlib.callback.BluetoothDetectConnect;
import com.demo.bluetoothlib.common.BluetoothConstants;

import java.io.IOException;
import java.util.UUID;

/**
 * Create connect as a client with bluetooth device
 */
public class BluetoothClient extends Thread {
    private static final String TAG = "BluetoothClient";
    private BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final Context context;
    private BluetoothDetectConnect bluetoothDetectConnect;

    public synchronized void setBluetoothDetectConnect(BluetoothDetectConnect bluetoothDetectConnect) {
        this.bluetoothDetectConnect = bluetoothDetectConnect;
    }

    public BluetoothClient(BluetoothDevice bluetoothDevice, Context context) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mmDevice = bluetoothDevice;
        this.context = context;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "BluetoothClient: android.permission.BLUETOOTH_CONNECT not granted");
            return;
        }
        try {
            //code fixing mmDevice.getUuids() null value
            mmDevice.fetchUuidsWithSdp();
            if (mmDevice.getUuids() == null){
                tmp = mmDevice.createRfcommSocketToServiceRecord(UUID.fromString(BluetoothConstants.DEFAULT_UUID));
            }else{
                tmp = mmDevice.createRfcommSocketToServiceRecord(mmDevice.getUuids()[0].getUuid());
            }
        } catch (IOException e) {
            Log.e(TAG, "BluetoothClient: ", e);
        }
        mmSocket = tmp;
    }

    @Override
    public void run() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "android.permission.BLUETOOTH_CONNECT not granted");
            return;
        }
        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException e) {
            if (bluetoothDetectConnect != null){
                Log.e(TAG, "run: connect fail ",e);
                bluetoothDetectConnect.connectStatus(mmSocket.isConnected());
            }
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException ex) {
                Log.e(TAG, "Could not close the client socket", e);
            }
            return;
        }
        //Call back to detect is connect
        if (bluetoothDetectConnect != null){
            Log.d(TAG, "run: Create connect " +mmSocket.isConnected());
            bluetoothDetectConnect.connectStatus(mmSocket.isConnected());
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        BluetoothCommunicate bluetoothCommunicate = new BluetoothCommunicate(mmSocket);
        bluetoothCommunicate.start();
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}
