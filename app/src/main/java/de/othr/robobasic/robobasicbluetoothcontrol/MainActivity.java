package de.othr.robobasic.robobasicbluetoothcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity starts search for Bluetooth devices and shows them in a RecyclerView,
 * connect to one by clicking on it
 */
public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothScanner;
    private Handler mHandler;
    private DeviceAdapter mDeviceListAdapter;

    private Button mStartButton;
    private TextView mInfoTextView;
    private RecyclerView mRecyclerView;

    // Stops scanning after 10 seconds.
    private static final long   SCAN_PERIOD = 10000;
    private static final int    REQUEST_ENABLE_BT = 1;

    ArrayList<BluetoothDevice> mDevices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothScanner = mBluetoothAdapter.getBluetoothLeScanner();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        mRecyclerView = findViewById(R.id.rv_main_devices);
         // setup devicelist
        //TODO: for debugging only
        //createSampleData();

        mDeviceListAdapter = new DeviceAdapter(mDevices);
        mDeviceListAdapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                final BluetoothDevice device = mDevices.get(position);
                if (device == null) return;

                //open DebugActivity to Connect to Device
                final Intent intent = new Intent(MainActivity.this, DebugActivity.class);
                intent.putExtra(DebugActivity.EXTRAS_DEVICE_NAME, device.getName());
                intent.putExtra(DebugActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                startActivity(intent);

                //tell MainActivity to stop scanning
                scanDevice(false);
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setAdapter(mDeviceListAdapter);

        mHandler = new Handler();

        mInfoTextView = findViewById(R.id.tv_main_info);

        mStartButton = findViewById(R.id.btn_main_start_scan);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoTextView.setVisibility(View.VISIBLE);
                scanDevice(true);
            }
        });



    }

    private void createSampleData() {
        String[] addresses = {"00:11:22:33:AA:BB","44:55:66:77:88:BB","00:22:44:66:AA:BB"};
        for (int i = 0; i < 10; i++) {
            String address = addresses[i%3];
            if(BluetoothAdapter.checkBluetoothAddress(address)){
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                if(device != null){
                    mDevices.add(device);
                }
                else
                {
                    String error = "device is null breakpoint " + address; //exception handling at its finest... lol
                }
            }
            else{
                String error = "bluetooth address invalid breakpoint " + address;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanDevice(false);
       // mDeviceListAdapter.clear();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
//        mLeDeviceListAdapter = new LeDeviceListAdapter();
//        setListAdapter(mLeDeviceListAdapter);
        mDeviceListAdapter.notifyDataSetChanged();


        scanDevice(true);

    }

    private void scanDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothScanner.stopScan(mScanCallback);
                  //  invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mBluetoothScanner.startScan(mScanCallback);


        } else {
            mBluetoothScanner.stopScan(mScanCallback);

        }
    }


    // Device scan callback.
    // TODO implement callback --> add them to a listview and show them for the user to select
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            String name = result.getDevice().getName();

            Toast.makeText(MainActivity.this, ("found device!:" + name), Toast.LENGTH_SHORT).show();
            mDeviceListAdapter.addDevice(result.getDevice());
            mDeviceListAdapter.notifyDataSetChanged();  //TODO NotifyItemInserted
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            for(ScanResult result : results){
                mDeviceListAdapter.addDevice(result.getDevice());
            }
            mDeviceListAdapter.notifyDataSetChanged();

        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };


}
