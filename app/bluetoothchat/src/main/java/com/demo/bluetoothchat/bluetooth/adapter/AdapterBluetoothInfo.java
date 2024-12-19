package com.demo.bluetoothchat.bluetooth.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.bluelazy.databinding.ItemBluetoothBinding;
import com.demo.bluetoothlib.model.BluetoothInfo;

import java.util.ArrayList;
import java.util.List;

public class AdapterBluetoothInfo extends RecyclerView.Adapter<BluetoothInfoHolder> {
    private List<BluetoothInfo> data = new ArrayList<>();
    private OnCreateConnectListener onCreateConnectListener;

    public void setData(List<BluetoothInfo> data) {
        this.data = data;
    }

    public void insertItem(BluetoothInfo bluetoothInfo) {
        data.add(bluetoothInfo);
        this.notifyItemInserted(data.size() - 1);
    }

    public void setOnCreateConnectListener(OnCreateConnectListener onCreateConnectListener) {
        this.onCreateConnectListener = onCreateConnectListener;
    }

    @NonNull
    @Override
    public BluetoothInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BluetoothInfoHolder(ItemBluetoothBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothInfoHolder holder, int position) {
        if (data == null) return;
        holder.bindData(data.get(position));
        if (onCreateConnectListener != null) {
            holder.getBinding().getRoot().setOnClickListener(view -> onCreateConnectListener.createConnect(holder.getLayoutPosition(), view, data.get(holder.getLayoutPosition())));
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
