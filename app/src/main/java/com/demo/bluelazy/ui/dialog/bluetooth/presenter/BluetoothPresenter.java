package com.demo.bluelazy.ui.dialog.bluetooth.presenter;

import com.demo.bluetoothlib.BluetoothExecute;
import com.demo.bluetoothlib.model.BluetoothInfo;

import java.util.List;

public interface BluetoothPresenter {
    void initPairedListDevices(BluetoothExecute execute);
    void findDeviceNotPaired(BluetoothExecute execute);
}
