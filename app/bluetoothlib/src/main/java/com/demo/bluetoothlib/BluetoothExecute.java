package com.demo.bluetoothlib;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.demo.bluetoothlib.callback.BluetoothDetectConnect;
import com.demo.bluetoothlib.callback.BluetoothDetectStatus;
import com.demo.bluetoothlib.callback.DiscoverCallBack;
import com.demo.bluetoothlib.model.BluetoothInfo;
import com.demo.bluetoothlib.thread.BluetoothClassicService;
import com.demo.bluetoothlib.thread.BluetoothClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothExecute {
    private static final String TAG = "BluetoothExecute";
    private final FragmentActivity activity;
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothClassicService bluetoothClassicService;
    private ActivityResultLauncher<Intent> requestEnableBluetooth;
    private ActivityResultLauncher<String[]> locationPermissionRequest;

    /**
     * Initial this in onCreate
     *
     * @param activity: Activity attack this
     */
    public BluetoothExecute(@NonNull FragmentActivity activity) {
        this.activity = activity;
        registerPermission();
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
            requestBluetoothPermission();
        } else {
            //Not support any kind of type
            Log.w(TAG, "BluetoothExecute: BluetoothBLE not supported");
        }

        if (BLUETOOTH_STATUS == 1 || BLUETOOTH_STATUS == 2) {
            requestBluetoothPermission();
        }
        //create broadcast find bluetooth
        bluetoothClassicService = new BluetoothClassicService();
    }

    private void registerPermission() {
        requestEnableBluetooth = activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == Activity.RESULT_OK) {
                    Log.i(TAG, "onActivityResult: enable bluetooth success");
                } else {
                    Log.i(TAG, "onActivityResult: enable bluetooth fail");
                }
            }
        });
        locationPermissionRequest = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
            Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Boolean bluetoothConnectGranted = result.getOrDefault(Manifest.permission.BLUETOOTH_CONNECT, false);
                Boolean bluetoothScanGranted = result.getOrDefault(Manifest.permission.BLUETOOTH_SCAN, false);
                Boolean bluetoothAdvertiseGranted = result.getOrDefault(Manifest.permission.BLUETOOTH_ADVERTISE, false);
                if (Boolean.TRUE.equals(bluetoothConnectGranted)) {
                    Log.i(TAG, String.format("requestBluetoothPermission Success: %s ", Manifest.permission.BLUETOOTH_CONNECT));
                } else {
                    Log.e(TAG, String.format("requestBluetoothPermission Fail: %s ", Manifest.permission.BLUETOOTH_CONNECT));
                    return;
                }
                if (Boolean.TRUE.equals(bluetoothScanGranted)) {
                    Log.i(TAG, String.format("requestBluetoothPermission Success: %s ", Manifest.permission.BLUETOOTH_SCAN));
                } else {
                    Log.e(TAG, String.format("requestBluetoothPermission Fail: %s ", Manifest.permission.BLUETOOTH_SCAN));
                    return;
                }
                if (Boolean.TRUE.equals(bluetoothAdvertiseGranted)) {
                    Log.i(TAG, String.format("requestBluetoothPermission Success: %s ", Manifest.permission.BLUETOOTH_ADVERTISE));
                } else {
                    Log.e(TAG, String.format("requestBluetoothPermission Fail: %s ", Manifest.permission.BLUETOOTH_ADVERTISE));
                    return;
                }
//                startFindBluetoothSPP();
//                BluetoothSPP();
            }

            //Request find bluetooth with physical location
            if (Boolean.TRUE.equals(fineLocationGranted)) {
                Log.i(TAG, String.format("requestBluetoothPermission success: %s", Manifest.permission.ACCESS_FINE_LOCATION));
                findBluetoothWithPhysicalDevice();
            } else {
                Log.e(TAG, String.format("requestBluetoothPermission fail: %s", Manifest.permission.ACCESS_FINE_LOCATION));
            }
            if (Boolean.TRUE.equals(coarseLocationGranted)) {
                Log.i(TAG, String.format("requestBluetoothPermission success: %s", Manifest.permission.ACCESS_COARSE_LOCATION));
                findBluetoothWithPhysicalDevice();
            } else {
                Log.e(TAG, String.format("requestBluetoothPermission fail: %s", Manifest.permission.ACCESS_COARSE_LOCATION));
            }

            Log.i(TAG, "requestBluetoothPermission : request permission finish ");
        });
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
        bluetoothClassicService.setDiscoverCallBack(discoverCallBack);
        activity.registerReceiver(bluetoothClassicService, intentFilter);
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
                    Log.d(TAG, "detectBluetoothIsScan: "+ bluetoothAdapter.isDiscovering());
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    if (bluetoothDetectStatus != null) {
                        boolean isDiscover = bluetoothAdapter.isDiscovering();
                        bluetoothDetectStatus.isDiscoverMode(isDiscover);
                        if (!bluetoothAdapter.isDiscovering()){
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
        if (bluetoothClassicService == null) {
            Log.e(TAG, "cancelFindBluetoothSPP: bluetooth finding not register");
            return;
        }
        activity.unregisterReceiver(bluetoothClassicService);
    }

    public void findBluetoothWithPhysicalDevice() {

    }

    private void requestBluetoothPermission() {
        Log.i(TAG, "requestBluetoothPermission: Start request permission");
        String[] permissions = new String[5];
        permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
        permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions[2] = Manifest.permission.BLUETOOTH_CONNECT;
            permissions[3] = Manifest.permission.BLUETOOTH_SCAN;
            permissions[4] = Manifest.permission.BLUETOOTH_ADVERTISE;
        }
        locationPermissionRequest.launch(permissions);
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
            Log.d(TAG, "createConnection:" + device.getBondState());
            Log.i(TAG, "createConnection: Create pairing success");
        } else {
            Log.e(TAG, "createConnection: Create pairing fail" + macAddress);
            return false;
        }
        return true;
    }
}
