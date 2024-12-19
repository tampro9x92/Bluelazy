package com.demo.bluetoothchat.chat.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.bluelazy.chat.model.Message;
import com.demo.bluelazy.databinding.ChatMessageBinding;

import java.util.ArrayList;
import java.util.List;

public class MessageItemRecyclerViewAdapter extends RecyclerView.Adapter<MessageItemRecyclerViewAdapter.ViewHolder> {
    private List<Message> listMessage;

    public void insertData(Message message){
        if (listMessage == null){
            listMessage = new ArrayList<>();
        }
        listMessage.add(message);
        notifyItemInserted(listMessage.size()-1);
    }

    public void setListMessage(List<Message> listMessage) {
        this.listMessage = listMessage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ChatMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (listMessage == null || listMessage.isEmpty()) return;
        holder.bindData(listMessage.get(position));
    }

    @Override
    public int getItemCount() {
        return listMessage == null ? 0 : listMessage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ChatMessageBinding binding;

        public ChatMessageBinding getBinding() {
            return binding;
        }

        public ViewHolder(ChatMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(Message message){
            binding.addressBluetooth.setText(message.getAddress());
            binding.content.setText(message.getMessage());
            binding.tvTime.setText(message.getTime());
        }
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}