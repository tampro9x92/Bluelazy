package com.demo.bluetoothchat.bluetooth.presenter;

import com.demo.bluetoothlib.BluetoothExecute;

public interface BluetoothPresenter {
    void initPairedListDevices(BluetoothExecute execute);
    void findDeviceNotPaired(BluetoothExecute execute);
}
