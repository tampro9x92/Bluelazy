package com.demo.bluelazy.ui.dialog;

import android.bluetooth.BluetoothClass;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.bluelazy.R;
import com.demo.bluelazy.databinding.ItemBluetoothBinding;
import com.demo.bluetoothlib.model.BluetoothInfo;

import java.util.List;

/**
 *
 */
public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.ViewHolder> {

    private final List<BluetoothInfo> data;
    private BluetoothEventListener bluetoothEventListener;

    public BluetoothAdapter(List<BluetoothInfo> data) {
        this.data = data;
    }

    public void setBluetoothEventListener(BluetoothEventListener bluetoothEventListener) {
        this.bluetoothEventListener = bluetoothEventListener;
    }

    public void insertData(BluetoothInfo bluetoothInfo) {
        if (bluetoothInfo.getName() == null) return;
        if (data.contains(bluetoothInfo)) {
            return;
        }
        data.add(bluetoothInfo);
        notifyItemInserted(data.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBluetoothBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bindData(data.get(position));
        holder.binding.getRoot().setOnClickListener(view -> {
            if (bluetoothEventListener == null) return;
            bluetoothEventListener.createConnect(position,data.get(position));
            holder.binding.pbConnect.setVisibility(View.VISIBLE);
            holder.binding.imvStatus.setVisibility(View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public interface BluetoothEventListener {
        void createConnect(int position,BluetoothInfo bluetoothInfo);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemBluetoothBinding binding;

        public ViewHolder(ItemBluetoothBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(BluetoothInfo bluetoothInfo) {
            if (bluetoothInfo.getType() == BluetoothClass.Device.Major.AUDIO_VIDEO) {
                binding.imvBluetoothDevice.setImageResource(R.drawable.ic_headphones_24);
            } else if (bluetoothInfo.getType() == BluetoothClass.Device.Major.COMPUTER) {
                binding.imvBluetoothDevice.setImageResource(R.drawable.black_computer_24);
            } else if (bluetoothInfo.getType() == BluetoothClass.Device.Major.WEARABLE) {
                binding.imvBluetoothDevice.setImageResource(R.drawable.black_watch_24);
            } else if (bluetoothInfo.getType() == BluetoothClass.Device.Major.HEALTH) {
                binding.imvBluetoothDevice.setImageResource(R.drawable.ic_health_24);
            } else if (bluetoothInfo.getType() == BluetoothClass.Device.Major.IMAGING) {
                binding.imvBluetoothDevice.setImageResource(R.drawable.black_camera_24);
            } else if (bluetoothInfo.getType() == BluetoothClass.Device.Major.PHONE) {
                binding.imvBluetoothDevice.setImageResource(R.drawable.black_phone_24);
            } else {
                binding.imvBluetoothDevice.setImageResource(R.drawable.black_bluetooth);
            }

            binding.tvBluetoothName.setText(bluetoothInfo.getName() == null ? "" : bluetoothInfo.getName());
            binding.tvBluetoothAddress.setText(bluetoothInfo.getAddress());
            if (bluetoothInfo.isConnected()){
                binding.pbConnect.setVisibility(View.GONE);
                binding.imvStatus.setVisibility(View.VISIBLE);
                binding.imvStatus.setImageResource(R.drawable.ic_connect);
            }else {
                binding.pbConnect.setVisibility(View.GONE);
                binding.imvStatus.setVisibility(View.VISIBLE);
                binding.imvStatus.setImageResource(R.drawable.ic_connect_fail);
            }
        }
    }
}