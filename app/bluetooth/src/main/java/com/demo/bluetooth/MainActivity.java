package com.demo.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.demo.bluetooth.model.BluetoothInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
        Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
        Boolean bluetoothConnectGranted = null;
        Boolean bluetoothScanGranted = null;
        Boolean bluetoothAdvertiseGranted = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothConnectGranted = result.getOrDefault(Manifest.permission.BLUETOOTH_CONNECT, false);
            bluetoothScanGranted = result.getOrDefault(Manifest.permission.BLUETOOTH_SCAN, false);
            bluetoothAdvertiseGranted = result.getOrDefault(Manifest.permission.BLUETOOTH_ADVERTISE, false);
        }

        if (bluetoothConnectGranted != null && bluetoothConnectGranted && bluetoothScanGranted != null && bluetoothScanGranted && bluetoothAdvertiseGranted != null && bluetoothAdvertiseGranted) {
            findBluetoothHadPaired();
            findBluetoothSPP();
        }
        if (fineLocationGranted != null && fineLocationGranted) {
            findBluetoothWithPhysicalDevice();
        } else if (coarseLocationGranted != null && coarseLocationGranted) {

        } else {

        }
    });

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    List<BluetoothInfo> bluetoothInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        requestBluetoothPermission();
    }

    //TODO REQUEST DEVICE PERMISSION HERE
    private void requestBluetoothPermission() {
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

    @SuppressLint("MissingPermission")
    private void findBluetoothHadPaired() {
        Set<BluetoothDevice> devicesBounded = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device:
             devicesBounded) {
            if (device.getName()!=null){
                bluetoothInfoList.add(new BluetoothInfo(device.getName(),device.getAddress()));
            }
        }
    }
    private void findBluetoothSPP() {

    }

    private void findBluetoothWithPhysicalDevice() {

    }
}