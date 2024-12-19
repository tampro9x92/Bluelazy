package com.demo.bluelazy.ui.dialog.bluetooth.view;

import com.demo.bluetoothlib.model.BluetoothInfo;

import java.util.LinkedHashSet;
import java.util.List;

public interface BluetoothView {
    void listPairedBluetoothInfo(List<BluetoothInfo> devicesPaired);

    void findBluetoothDeviceNotPaired(LinkedHashSet<BluetoothInfo> bluetoothInfo);
}
