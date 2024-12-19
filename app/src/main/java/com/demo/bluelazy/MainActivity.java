package com.demo.bluelazy;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.demo.bluetoothlib.BluetoothExecute;
import com.demo.bluetoothlib.model.BluetoothInfo;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.demo.bluelazy.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ActivityResultLauncher<String[]> locationPermissionRequest;
    private ActivityResultLauncher<Intent> requestEnableBluetooth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BluetoothExecute bluetoothExecute = new BluetoothExecute(this);
        registerPermission();
        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }
    public void registerPermission() {
        requestEnableBluetooth = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == Activity.RESULT_OK) {
                    Log.i(TAG, "onActivityResult: enable bluetooth success");
                } else {
                    Log.i(TAG, "onActivityResult: enable bluetooth fail");
                }
            }
        });
        locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
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
//                findBluetoothWithPhysicalDevice();
            } else {
                Log.e(TAG, String.format("requestBluetoothPermission fail: %s", Manifest.permission.ACCESS_FINE_LOCATION));
            }
            if (Boolean.TRUE.equals(coarseLocationGranted)) {
                Log.i(TAG, String.format("requestBluetoothPermission success: %s", Manifest.permission.ACCESS_COARSE_LOCATION));
//                findBluetoothWithPhysicalDevice();
            } else {
                Log.e(TAG, String.format("requestBluetoothPermission fail: %s", Manifest.permission.ACCESS_COARSE_LOCATION));
            }

            Log.i(TAG, "requestBluetoothPermission : request permission finish ");
        });
    }

    private void requestBluetoothPermission() {
        Log.i(TAG, "requestBluetoothPermission: Start request permission");
        String[] permissions = new String[5];
        permissions[0] = android.Manifest.permission.ACCESS_FINE_LOCATION;
        permissions[1] = android.Manifest.permission.ACCESS_COARSE_LOCATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions[2] = android.Manifest.permission.BLUETOOTH_CONNECT;
            permissions[3] = android.Manifest.permission.BLUETOOTH_SCAN;
            permissions[4] = Manifest.permission.BLUETOOTH_ADVERTISE;
        }
        locationPermissionRequest.launch(permissions);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}