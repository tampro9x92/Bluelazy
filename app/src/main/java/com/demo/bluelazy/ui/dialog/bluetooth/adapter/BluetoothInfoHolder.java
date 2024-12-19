package com.demo.bluelazy.ui.dialog.bluetooth.adapter;

import android.bluetooth.BluetoothClass;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.bluelazy.R;
import com.demo.bluelazy.databinding.ItemBluetoothBinding;
import com.demo.bluetoothlib.model.BluetoothInfo;

public class BluetoothInfoHolder extends RecyclerView.ViewHolder {
    private final ItemBluetoothBinding binding;

    public BluetoothInfoHolder(@NonNull ItemBluetoothBinding itemBluetoothBinding) {
        super(itemBluetoothBinding.getRoot());
        binding = itemBluetoothBinding;
    }

    public ItemBluetoothBinding getBinding() {
        return binding;
    }

    public void bindData(BluetoothInfo bluetoothInfo) {
        detectBluetoothType(bluetoothInfo.getType());
        binding.tvBluetoothName.setText(bluetoothInfo.getName());
        binding.tvBluetoothAddress.setText(bluetoothInfo.getAddress());

        int icon = R.drawable.ic_connect_fail;
        if (bluetoothInfo.isConnected()) {
            binding.pbConnect.setVisibility(View.GONE);
            binding.imvStatus.setVisibility(View.VISIBLE);
            icon = R.drawable.ic_connect;
        } else {
            binding.pbConnect.setVisibility(View.GONE);
            binding.imvStatus.setVisibility(View.VISIBLE);
        }
        binding.imvStatus.setImageResource(icon);
    }

    private void detectBluetoothType(int type) {
        int icon = R.drawable.black_bluetooth;
        switch (type) {
            case BluetoothClass.Device.Major.MISC:
            case BluetoothClass.Device.Major.AUDIO_VIDEO:
            case BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER:
            case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO:
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
            case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES:
            case BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO:
            case BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER:
            case BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE:
            case BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO:
            case BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX:
            case BluetoothClass.Device.AUDIO_VIDEO_VCR:
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA:
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING:
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER:
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY:
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR:
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                icon = R.drawable.ic_headphones_24;
                break;
            case BluetoothClass.Device.Major.COMPUTER:
            case BluetoothClass.Device.COMPUTER_DESKTOP:
            case BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA:
            case BluetoothClass.Device.COMPUTER_LAPTOP:
            case BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA:
            case BluetoothClass.Device.COMPUTER_SERVER:
            case BluetoothClass.Device.COMPUTER_WEARABLE:
                icon = R.drawable.black_computer_24;
                break;
            case BluetoothClass.Device.Major.HEALTH:
            case BluetoothClass.Device.HEALTH_BLOOD_PRESSURE:
            case BluetoothClass.Device.HEALTH_DATA_DISPLAY:
            case BluetoothClass.Device.HEALTH_GLUCOSE:
            case BluetoothClass.Device.HEALTH_PULSE_OXIMETER:
            case BluetoothClass.Device.HEALTH_PULSE_RATE:
            case BluetoothClass.Device.HEALTH_THERMOMETER:
            case BluetoothClass.Device.HEALTH_WEIGHING:
                icon = R.drawable.black_monitor_heart_24;
                break;
            case BluetoothClass.Device.PERIPHERAL_KEYBOARD:
            case BluetoothClass.Device.PERIPHERAL_KEYBOARD_POINTING:
                icon = R.drawable.black_keyboard_24;
                break;
            case BluetoothClass.Device.PERIPHERAL_POINTING:
            case BluetoothClass.Device.Major.PHONE:
            case BluetoothClass.Device.PHONE_CELLULAR:
            case BluetoothClass.Device.PHONE_CORDLESS:
            case BluetoothClass.Device.PHONE_ISDN:
            case BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY:
            case BluetoothClass.Device.PHONE_SMART:
                icon = R.drawable.black_phone_24;
                break;
            case BluetoothClass.Device.Major.TOY:
            case BluetoothClass.Device.TOY_CONTROLLER:
            case BluetoothClass.Device.TOY_DOLL_ACTION_FIGURE:
            case BluetoothClass.Device.TOY_GAME:
            case BluetoothClass.Device.TOY_ROBOT:
            case BluetoothClass.Device.TOY_VEHICLE:
                icon = R.drawable.black_toys_24;
                break;
            case BluetoothClass.Device.Major.WEARABLE:
            case BluetoothClass.Device.WEARABLE_GLASSES:
            case BluetoothClass.Device.WEARABLE_HELMET:
            case BluetoothClass.Device.WEARABLE_JACKET:
            case BluetoothClass.Device.WEARABLE_PAGER:
            case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
                icon = R.drawable.black_watch_24;
                break;
            case BluetoothClass.Device.Major.IMAGING:
                icon = R.drawable.black_camera_24;
                break;
            case BluetoothClass.Device.Major.NETWORKING:
                icon = R.drawable.black_network_24;
                break;
            case BluetoothClass.Device.Major.PERIPHERAL:
            default:
                break;
        }
        binding.imvBluetoothDevice.setImageResource(icon);
    }
}
