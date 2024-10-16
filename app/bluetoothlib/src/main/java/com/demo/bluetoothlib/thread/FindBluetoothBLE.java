package com.demo.bluetoothlib.thread;

import androidx.appcompat.app.AppCompatActivity;

public class FindBluetoothBLE extends Thread{
    private AppCompatActivity activity;
    private static final String TAG = "FindBluetoothBLE";

    public FindBluetoothBLE(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        super.run();
    }
}
