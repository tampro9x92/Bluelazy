package com.demo.bluelazy.ui.fragment.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demo.bluelazy.R;
import com.demo.bluelazy.ui.fragment.chat.adapter.MessageItemRecyclerViewAdapter;
import com.demo.bluelazy.databinding.FragmentChatBinding;
import com.demo.bluetoothlib.BluetoothExecute;

import java.util.ArrayList;

public class MessageItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private FragmentChatBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessageItemFragment() {
    }

    @SuppressWarnings("unused")
    public static MessageItemFragment newInstance(int columnCount) {
        MessageItemFragment fragment = new MessageItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    //TODO check how can connect 2 device here
    private void init() {
        binding.fbtnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.fbtnBluetooth);
            }
        });
        MessageItemRecyclerViewAdapter adapter = new MessageItemRecyclerViewAdapter();
        adapter.setListMessage(new ArrayList<>());
        binding.lstChat.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.lstChat.setAdapter(adapter);
    }

    private void bluetooth() {
        BluetoothExecute bluetoothExecute = new BluetoothExecute(getActivity());
        bluetoothExecute.createBluetoothSever();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}