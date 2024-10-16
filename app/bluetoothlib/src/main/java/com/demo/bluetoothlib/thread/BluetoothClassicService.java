package com.demo.bluetoothlib.thread;

import android.Manifest;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.demo.bluetoothlib.callback.DiscoverCallBack;
import com.demo.bluetoothlib.model.BluetoothInfo;

/**
 * This register find bluetooth ask background thread
 */
public class BluetoothClassicService extends BroadcastReceiver {
    private static final String TAG = "FindBluetoothClassic";
    DiscoverCallBack discoverCallBack;

    public void setDiscoverCallBack(DiscoverCallBack discoverCallBack) {
        this.discoverCallBack = discoverCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null || !action.equals(BluetoothDevice.ACTION_FOUND)) {
            Log.e(TAG, "onReceive: not the right action or action may be null");
            return;
        }

        // Discovery has found a device. Get the BluetoothDevice
        // object and its info from the Intent.
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Log.e(TAG, String.format("onReceive: %s Permission denied", Manifest.permission.BLUETOOTH_CONNECT));
            }
            return;
        }

        if (device == null) {
            Log.e(TAG, "onReceive: not bluetooth found BluetoothDevice is null");
            return;
        }
        String deviceName = device.getName();
        String deviceAddress = device.getAddress();
        Log.i(TAG, String.format("onReceive: %s %s",deviceName,deviceAddress ));
        BluetoothClass bluetoothClass = device.getBluetoothClass();
        BluetoothInfo bluetoothInfo = new BluetoothInfo(deviceName, deviceAddress, bluetoothClass.getMajorDeviceClass());
        if (discoverCallBack != null) {
            discoverCallBack.bluetoothDevicesReceived(bluetoothInfo);
        }
    }

}
