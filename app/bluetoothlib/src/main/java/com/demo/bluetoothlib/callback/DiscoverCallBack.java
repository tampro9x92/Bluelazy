package com.demo.bluetoothlib.callback;

import com.demo.bluetoothlib.model.BluetoothInfo;

import java.util.List;

public interface DiscoverCallBack {
    void bluetoothDevicesReceived(BluetoothInfo bluetoothInfo);
}
