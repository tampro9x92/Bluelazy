package com.demo.bluetoothlib.service;

import androidx.appcompat.app.AppCompatActivity;

public class BluetoothBLE extends Thread{
    private static final String TAG = "BluetoothBLE";
    private AppCompatActivity activity;

    public BluetoothBLE(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        super.run();
    }
}
