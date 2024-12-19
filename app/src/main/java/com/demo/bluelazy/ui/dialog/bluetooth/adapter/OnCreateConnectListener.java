package com.demo.bluelazy.ui.dialog.bluetooth.adapter;

import android.view.View;

import com.demo.bluetoothlib.model.BluetoothInfo;

public interface OnCreateConnectListener {
    void createConnect(int position, View v, BluetoothInfo bluetoothInfo);
}
