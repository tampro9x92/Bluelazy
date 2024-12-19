package com.demo.bluelazy.ui.dialog.bluetooth.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.bluelazy.R;
import com.demo.bluelazy.ui.dialog.bluetooth.adapter.AdapterBluetoothInfo;
import com.demo.bluelazy.ui.dialog.bluetooth.presenter.BluetoothPresenter;
import com.demo.bluelazy.ui.dialog.bluetooth.presenter.BluetoothPresenterImpl;
import com.demo.bluelazy.ui.dialog.bluetooth.view.BluetoothView;
import com.demo.bluelazy.databinding.DialogBluetoothBinding;
import com.demo.bluetoothlib.BluetoothExecute;
import com.demo.bluetoothlib.model.BluetoothInfo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * This only using for bluetooth client
 */
public class DialogFindBluetooth extends DialogFragment implements BluetoothView {
    private static final String TAG = "BluetoothFragment";
    private DialogBluetoothBinding binding;
    private BluetoothExecute bluetoothExecute;
    private BluetoothPresenter presenter;

    private AdapterBluetoothInfo adapterBluetoothInfo;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DialogBluetoothBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    @Override
    public void onResume() {
        initData();
        super.onResume();
    }

    private void initData() {
        bluetoothExecute = new BluetoothExecute(requireActivity());
        presenter = new BluetoothPresenterImpl(this);
        presenter.initPairedListDevices(bluetoothExecute);
        presenter.findDeviceNotPaired(bluetoothExecute);
    }

    private void initView() {
        adapterBluetoothInfo = new AdapterBluetoothInfo();
        binding.pairedList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.pairedList.setAdapter(adapterBluetoothInfo);

        AdapterBluetoothInfo adapterBluetoothInfo = new AdapterBluetoothInfo();
        binding.rcvDeviceExisting.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        binding.rcvDeviceExisting.setAdapter(adapterBluetoothInfo);
    }

    @Override
    public void listPairedBluetoothInfo(List<BluetoothInfo> devicesPaired) {
        adapterBluetoothInfo.setData(devicesPaired);
        adapterBluetoothInfo.setOnCreateConnectListener((position, v, bluetoothInfo) -> {
            boolean isConnected = bluetoothExecute.createConnectionClient(bluetoothInfo.getAddress(),
                    isConnect -> Log.d(TAG, "createConnect: " + isConnect));
            Log.d(TAG, "listPairedBluetoothInfo: " + isConnected);
            if (isConnected) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NavController navController = Navigation.findNavController(v);
                        navController.navigate(R.id.action_fragmentBluetoothList2_to_FirstFragment);
                    }
                });
            }
        });
    }

    @Override
    public void findBluetoothDeviceNotPaired(LinkedHashSet<BluetoothInfo> bluetoothInfo) {
        requireActivity().runOnUiThread(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                adapterBluetoothInfo.insertItem(new ArrayList<>(bluetoothInfo).get(bluetoothInfo.size() - 1));
                adapterBluetoothInfo.setOnCreateConnectListener((position, v, bluetoothInfo1) -> {
                    boolean isConnected = bluetoothExecute.createConnectionClient(bluetoothInfo1.getAddress(),
                            isConnect -> Log.d(TAG, "createConnect: " + isConnect));
                    Log.d(TAG, "listPairedBluetoothInfo: " + isConnected);
                    if (isConnected) {
                        requireActivity().runOnUiThread(() -> {
                            this.dismiss();
                        });
                    }
                });
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bluetoothExecute.stopFindBluetooth();
        binding = null;
    }
}