package com.demo.bluetoothlib;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.demo.bluetoothlib.callback.BluetoothDetectConnect;
import com.demo.bluetoothlib.callback.BluetoothDetectStatus;
import com.demo.bluetoothlib.callback.DiscoverCallBack;
import com.demo.bluetoothlib.model.BluetoothInfo;
import com.demo.bluetoothlib.service.BluetoothClassic;
import com.demo.bluetoothlib.service.BluetoothClient;
import com.demo.bluetoothlib.service.BluetoothSever;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothExecute {
    private static final String TAG = "BluetoothExecute";
    private final FragmentActivity activity;
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothClassic bluetoothClassic;
    private ActivityResultLauncher<Intent> requestEnableBluetooth;


    /**
     * Initial this in onCreate
     *
     * @param activity: Activity attack this
     */
    public BluetoothExecute(@NonNull FragmentActivity activity) {
        this.activity = activity;
        BluetoothManager bluetoothManager = activity.getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
        //Detect bluetooth device support
        if (bluetoothAdapter == null) {
            Log.w(TAG, "BluetoothExecute: Device no support bluetooth...");
            return;
        }
        //Request turn on bluetooth
        requestEnableBluetooth();

        //Checking support bluetooth classic mode
        int BLUETOOTH_STATUS = -1;
        if (isClassicBluetoothSupport()) {
            BLUETOOTH_STATUS = 1;
            Log.i(TAG, "BluetoothExecute: Bluetooth classic supported");
            //Checking support bluetooth BLE mode
        } else {
            Log.w(TAG, "BluetoothExecute: Bluetooth classic not supported");
        }

        if (isBLEBluetoothSupport() && BLUETOOTH_STATUS == -1) {
            BLUETOOTH_STATUS = 2;
            Log.i(TAG, "BluetoothExecute: BluetoothBLE supported");
//            requestBluetoothPermission();
        } else {
            //Not support any kind of type
            Log.w(TAG, "BluetoothExecute: BluetoothBLE not supported");
        }

        if (BLUETOOTH_STATUS == 1 || BLUETOOTH_STATUS == 2) {
//            requestBluetoothPermission();
        }
        //create broadcast find bluetooth
        bluetoothClassic = new BluetoothClassic();
    }



    public void requestEnableBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "findBluetoothHadPaired: bluetooth_connect not granted");
                return;
            }
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            requestEnableBluetooth.launch(enableBtIntent);
        }
    }

    public void startFindBluetoothSPP(DiscoverCallBack discoverCallBack) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "findBluetoothSPP: permission bluetooth_scan not granted");
            return;
        }
        bluetoothAdapter.startDiscovery();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        bluetoothClassic.setDiscoverCallBack(discoverCallBack);
        activity.registerReceiver(bluetoothClassic, intentFilter);
    }

    public void stopFindBluetooth() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "stopFindBluetooth: permission bluetooth_scan not granted");
            return;
        }
        bluetoothAdapter.cancelDiscovery();
    }

    public void detectBluetoothIsScan(BluetoothDetectStatus bluetoothDetectStatus) {
        if (bluetoothAdapter != null) {
            Thread thread = new Thread(() -> {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "detectBluetoothIsScan:  permission bluetooth_scan not granted");
                    return;
                }
                while (true) {
                    Log.d(TAG, "detectBluetoothIsScan: " + bluetoothAdapter.isDiscovering());
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    if (bluetoothDetectStatus != null) {
                        boolean isDiscover = bluetoothAdapter.isDiscovering();
                        bluetoothDetectStatus.isDiscoverMode(isDiscover);
                        if (!bluetoothAdapter.isDiscovering()) {
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
            thread.start();
        }

    }

    public void endFindBluetoothSPP() {
        if (bluetoothClassic == null) {
            Log.e(TAG, "cancelFindBluetoothSPP: bluetooth finding not register");
            return;
        }
        activity.unregisterReceiver(bluetoothClassic);
    }

    public void findBluetoothWithPhysicalDevice() {

    }



    public List<BluetoothInfo> findBluetoothHadPaired() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "findBluetoothHadPaired: bluetooth_connect not granted");
            return new ArrayList<>();
        }
        List<BluetoothInfo> bluetoothInfoList = new ArrayList<>();
        Set<BluetoothDevice> devicesBounded = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device :
                devicesBounded) {
            BluetoothClass bluetoothClass = device.getBluetoothClass();
            if (device.getName() != null) {
                bluetoothInfoList.add(new BluetoothInfo(device.getName(), device.getAddress(), bluetoothClass.getMajorDeviceClass()));
            }
        }
        return bluetoothInfoList;
    }

    public boolean isClassicBluetoothSupport() {
        // Use this check to determine whether Bluetooth classic is supported on the device.
        return activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
    }

    public boolean isBLEBluetoothSupport() {
        // Use this check to determine whether BLE is supported on the device. Then
        return activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * Create connect client with bluetooth device
     *
     * @param macAddress:             mac_address of bluetooth device
     * @param bluetoothDetectConnect: call back when connect success
     * @return true (create bound success)
     */
    public boolean createConnectionClient(String macAddress, BluetoothDetectConnect bluetoothDetectConnect) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "createConnection: android.permission.BLUETOOTH_SCAN Not granted ");
            return false;
        }
        stopFindBluetooth();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(macAddress);
        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            BluetoothClient bluetoothClient = new BluetoothClient(device, activity);
            bluetoothClient.setBluetoothDetectConnect(bluetoothDetectConnect);
            bluetoothClient.start();
            return true;
        }
        if (device.createBond()) {
            BluetoothClient bluetoothClient = new BluetoothClient(device, activity);
            bluetoothClient.setBluetoothDetectConnect(bluetoothDetectConnect);
            bluetoothClient.start();
            Log.d(TAG, "createConnectionClient:" + device.getBondState());
            Log.i(TAG, "createConnectionClient: Create pairing success");
        } else {
            Log.e(TAG, "createConnectionClient: Create pairing fail" + macAddress);
            return false;
        }
        return true;
    }

    public boolean createBluetoothSever() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "createConnection: android.permission.BLUETOOTH_SCAN Not granted ");
            return false;
        }
//        stopFindBluetooth();
//        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(macAddress);

//        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
//            BluetoothClient bluetoothClient = new BluetoothClient(device, activity);
//            bluetoothClient.setBluetoothDetectConnect(bluetoothDetectConnect);
//            bluetoothClient.start();
//            return true;
//        }
//        if (device.createBond()) {
            BluetoothSever bluetoothSever = new BluetoothSever(activity, bluetoothAdapter);
            bluetoothSever.start();
//            Log.d(TAG, "createConnectionSever:" + device.getBondState());
//            Log.i(TAG, "createConnectionSever: Create pairing success");
//        } else {
//            Log.e(TAG, "createConnectionSever: Create pairing fail" + macAddress);
//            return false;
//        }
        return true;
    }
}
