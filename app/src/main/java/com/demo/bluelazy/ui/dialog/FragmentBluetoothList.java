//package com.demo.bluelazy.dialog;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.demo.bluelazy.R;
//import com.demo.bluetoothlib.BluetoothExecute;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import java.util.ArrayList;
//
///**
// * A fragment representing a list of Items.
// */
//public class FragmentBluetoothList extends Fragment {
//    private static final String TAG = "FragmentBluetoothList";
//    // TODO: Customize parameter argument names
//    private static final String ARG_COLUMN_COUNT = "column-count";
//    // TODO: Customize parameters
//    private int mColumnCount = 1;
//    BluetoothExecute bluetoothExecute;
//    private BluetoothAdapter bluetoothExistingEquipment;
//
//    /**
//     * Mandatory empty constructor for the fragment manager to instantiate the
//     * fragment (e.g. upon screen orientation changes).
//     */
//    public FragmentBluetoothList() {
//    }
//
//    // TODO: Customize parameter initialization
//    @SuppressWarnings("unused")
//    public static FragmentBluetoothList newInstance(int columnCount) {
//        FragmentBluetoothList fragment = new FragmentBluetoothList();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
//        bluetoothExecute = new BluetoothExecute(requireActivity());
//        //Adding any bluetooth classic find
//        bluetoothExistingEquipment = new BluetoothAdapter(new ArrayList<>());
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_bluetooth, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        Context context = view.getContext();
//        bluetoothPairedInit(view,context);
//        bluetoothExistingEquipmentInit(view,context);
//        reloadFindBluetooth(view);
//
//
//    }
//
//    private void reloadFindBluetooth(View view) {
//        FloatingActionButton fbtnRefresh = view.findViewById(R.id.fbtnRefresh);
//        ProgressBar progressBar = view.findViewById(R.id.scanBluetooth);
//        fbtnRefresh.setOnClickListener(view1 -> {
//            bluetoothExecute.requestEnableBluetooth();
//            bluetoothExecute.startFindBluetoothSPP(bluetoothInfo -> bluetoothExistingEquipment.insertData(bluetoothInfo));
//            bluetoothExecute.detectBluetoothIsScan(isDiscover -> {
//                if (isDiscover) {
//                    //Using to update view bot blocking ui thread
//                    view.post(() -> {
//                        fbtnRefresh.setVisibility(View.GONE);
//                        progressBar.setVisibility(View.VISIBLE);
//                    });
//
//                } else {
//                    //Using to update view bot blocking ui thread
//                    view.post(() -> {
//                        fbtnRefresh.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.INVISIBLE);
//                    });
//
//
//                }
////            Log.d(TAG, "isDiscoverMode: " + isDiscover);
//            });
//        });
//    }
//    private void bluetoothExistingEquipmentInit(View view, Context context) {
//        FloatingActionButton fbtnRefresh = view.findViewById(R.id.fbtnRefresh);
//        ProgressBar progressBar = view.findViewById(R.id.scanBluetooth);
//        bluetoothExecute.startFindBluetoothSPP(bluetoothInfo -> {
//            bluetoothExistingEquipment.insertData(bluetoothInfo);
//        });
//        RecyclerView rcvExistingEquipment = view.findViewById(R.id.rcvDeviceExisting);
//        rcvExistingEquipment.setAdapter(bluetoothExistingEquipment);
//        bluetoothExistingEquipment.setBluetoothEventListener((position, bluetoothInfo) -> {
////            bluetoothExecute.requestEnableBluetooth();
////            bluetoothExecute.createConnectionClient(bluetoothInfo.getAddress(), isConnect -> {
////                view.post(() -> {
////                    bluetoothInfo.setConnected(isConnect);
////                    bluetoothExistingEquipment.notifyItemChanged(position, bluetoothInfo);
////                });
////            });
//            bluetoothExecute.createConnectionSever(bluetoothInfo.getAddress(), isConnect -> {
//                bluetoothExecute.requestEnableBluetooth();
//                view.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        bluetoothInfo.setConnected(isConnect);
//                        bluetoothExistingEquipment.notifyItemChanged(position, bluetoothInfo);
//                    }
//                });
//            });
//        });
//
//
//
//        bluetoothExecute.detectBluetoothIsScan(isDiscover -> {
//            if (isDiscover) {
//                //Using to update view bot blocking ui thread
//                view.post(() -> {
//                    fbtnRefresh.setVisibility(View.GONE);
//                    progressBar.setVisibility(View.VISIBLE);
//                });
//
//            } else {
//                //Using to update view bot blocking ui thread
//                view.post(() -> {
//                    fbtnRefresh.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.INVISIBLE);
//                });
//
//
//            }
////            Log.d(TAG, "isDiscoverMode: " + isDiscover);
//        });
//    }
//
//    private void bluetoothPairedInit(@NonNull View view,Context context){
//        RecyclerView recyclerView = view.findViewById(R.id.list);
//        BluetoothAdapter bluetoothPaired = new BluetoothAdapter(bluetoothExecute.findBluetoothHadPaired());
//        recyclerView.setAdapter(bluetoothPaired);
//        bluetoothPaired.setBluetoothEventListener((position, bluetoothInfo) -> {
////            bluetoothExecute.createConnectionClient(bluetoothInfo.getAddress(), isConnect -> {
////                bluetoothExecute.requestEnableBluetooth();
////                view.post(new Runnable() {
////                    @Override
////                    public void run() {
////                        bluetoothInfo.setConnected(isConnect);
////                        bluetoothPaired.notifyItemChanged(position, bluetoothInfo);
////                    }
////                });
////            });
//            //TODO testing bluetooth sever
//            bluetoothExecute.createConnectionSever(bluetoothInfo.getAddress(), isConnect -> {
//                bluetoothExecute.requestEnableBluetooth();
//                view.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        bluetoothInfo.setConnected(isConnect);
//                        bluetoothPaired.notifyItemChanged(position, bluetoothInfo);
//                    }
//                });
//            });
//        });
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        bluetoothExecute.endFindBluetoothSPP();
//    }
//}