package com.demo.bluelazy.ui.dialog.bluetooth.presenter;

import com.demo.bluelazy.ui.dialog.bluetooth.view.BluetoothView;
import com.demo.bluetoothlib.BluetoothExecute;
import com.demo.bluetoothlib.callback.DiscoverCallBack;
import com.demo.bluetoothlib.model.BluetoothInfo;

import java.util.LinkedHashSet;

public class BluetoothPresenterImpl implements BluetoothPresenter{
    private BluetoothView view;
    public BluetoothPresenterImpl(BluetoothView view) {
        this.view = view;
    }

    @Override
    public void initPairedListDevices(BluetoothExecute execute) {
        view.listPairedBluetoothInfo(execute.findBluetoothHadPaired());
    }

    @Override
    public void findDeviceNotPaired(BluetoothExecute execute) {
        LinkedHashSet<BluetoothInfo> lstDeviceNotPaired = new LinkedHashSet<>();
        execute.startFindBluetoothSPP(new DiscoverCallBack() {
            @Override
            public void bluetoothDevicesReceived(BluetoothInfo bluetoothInfo) {
                lstDeviceNotPaired.add(bluetoothInfo);
                view.findBluetoothDeviceNotPaired(lstDeviceNotPaired);
            }
        });
    }


}
