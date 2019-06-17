package de.othr.robobasic.robobasicbluetoothcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * DebugActivity will be used to send custom text messages to the robot to test the bluetooth connection
 * and print its responds to a View (RecyclerView for now, 1 item = 1 message)
 *
 * the Activity communicates with  {@code BluetoothService} which in turn interacts with the Bluetooth LE API
 */
public class DebugActivity extends AppCompatActivity {

    private final static String TAG = DebugActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private ProgressBar mConnectionProgressBar;
    private TextView mTerminal;
    private String mDeviceName;
    private String mDeviceAddress;
    private EditText mDataField;


    private boolean mConnected;

    private ShareActionProvider shareActionProvider;

    private ExpandableListView mGattServicesList;
    private BluetoothService mBluetoothService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<>();

    private BluetoothGattCharacteristic mNotifyCharacteristic, mWriteCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    // Code to manage Service lifecycle.
    //TODO: Stay connected outside of this activity
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothService = ((BluetoothService.LocalBinder) service).getService();

            if(!mBluetoothService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothService = null;
        }
    };

    // Handles various events fired by the Service.
        // ACTION_GATT_CONNECTED:           connected to a GATT server.
        // ACTION_GATT_DISCONNECTED:        disconnected from a GATT server.
        // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
        // ACTION_DATA_AVAILABLE:           received data from the device.  This can be a result of read
        //                                  or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                mConnectionProgressBar.setVisibility(View.INVISIBLE);
                runOnUiThread(() -> Toast.makeText(DebugActivity.this, getResources().getString(R.string.connected), Toast.LENGTH_SHORT).show());

            } else if (BluetoothService.ACTION_GATT_DISCONNECTED.equals(action)) {
                //TODO: display error
                mConnectionProgressBar.setVisibility(View.INVISIBLE);
                runOnUiThread(() -> Toast.makeText(DebugActivity.this, getResources().getString(R.string.disconnected), Toast.LENGTH_SHORT).show());

            } else if (BluetoothService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothService.getSupportedGattServices());
            } else if (BluetoothService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothService.EXTRA_DATA));
            }
        }
    };

    // If a given GATT characteristic is selected, check for supported features.  This sample
    // demonstrates 'Read' and 'Notify' features.  See
    // http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the complete
    // list of supported characteristic features.
    private final ExpandableListView.OnChildClickListener servicesListClickListener =
            new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    if (mGattCharacteristics != null) {

                        int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                        parent.setItemChecked(index, true);

                        final BluetoothGattCharacteristic characteristic =
                                mGattCharacteristics.get(groupPosition).get(childPosition);
                        final int charaProp = characteristic.getProperties();
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            // If there is an active notification on a characteristic, clear
                            // it first so it doesn't update the data field on the user interface.
                            if (mNotifyCharacteristic != null) {
                                mBluetoothService.setCharacteristicNotification(
                                        mNotifyCharacteristic, false);
                                mNotifyCharacteristic = null;
                            }
                            mBluetoothService.readCharacteristic(characteristic);
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = characteristic;
                            mBluetoothService.setCharacteristicNotification(
                                    characteristic, true);
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                            mWriteCharacteristic = characteristic;
                        }
                        return true;
                    }
                    return false;
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        final Intent intent = getIntent();

        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        if(mDeviceName == null)
            mDeviceName = "device";
        if(mDeviceAddress == null)
            mDeviceAddress = "unknown address";

        String toolbarTitle = mDeviceName + ": " + mDeviceAddress;
        getSupportActionBar().setTitle(toolbarTitle);


        //Save selected device in SharedPreferences
        //TODO: add Characteristics
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(getString(R.string.saved_mac_address_key), mDeviceAddress);
        editor.apply();

        // Sets up UI references.
        mConnectionProgressBar = findViewById(R.id.progressBar);
        mConnectionProgressBar.setIndeterminate(true);
        mConnectionProgressBar.setVisibility(View.VISIBLE);
        mConnectionProgressBar.setScaleY(4);

        mGattServicesList = findViewById(R.id.gatt_services_list);
        mGattServicesList.setOnChildClickListener(servicesListClickListener);

//        StateListDrawable selector = new StateListDrawable();
//        selector.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(getResources().getColor(R.color.primaryLightColor,null)));
//        selector.addState(new int[]{-android.R.attr.state_pressed}, new ColorDrawable(Color.WHITE));
//        mGattServicesList.setSelector(selector);


        mTerminal = findViewById(R.id.tv_terminal);
        mTerminal.setMovementMethod(new ScrollingMovementMethod());


        mDataField = findViewById(R.id.et_dbg_send);
        Button sendButton = findViewById(R.id.btn_dbg_send);
        sendButton.setOnClickListener(v -> {
            String data = mDataField.getText().toString();
            writeBLE(data);
            mDataField.getText().clear();
        });



        Intent gattServiceIntent = new Intent(this, BluetoothService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        //Bind activity to service (or other way around.. whatever)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // Return true to display menu
        return true;
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_share) {

            String text = mTerminal.getText().toString();
            if (text.isEmpty())
                return super.onOptionsItemSelected(item);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");
            setShareIntent(sendIntent);
            return true;
        }
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }*/

    // Call to update the share intent
    private void setShareIntent(String text) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");

        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(sendIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothService != null) {
            final boolean result = mBluetoothService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothService = null;
    }

//    private void setConnectionProgress(boolean success){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                //TODO: set drawable instead of progressbar
//                mConnectionProgressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.icon_up,null));
//            }
//        });
//
//    }


    private void displayData(String data) {
        if (data != null) {
            mTerminal.append(("\n" + data));
            setShareIntent(mTerminal.getText().toString());
        }
    }
    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<>();
        mGattCharacteristics = new ArrayList<>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, RoboNovaGattAttributes.lookupService(uuid, unknownServiceString));
//            currentServiceData.put(
//                    LIST_NAME, RoboNovaGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, RoboNovaGattAttributes.lookupCharacteristics(uuid, unknownCharaString));
//                currentCharaData.put(
//                        LIST_NAME, RoboNovaGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 },
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
        mGattServicesList.setAdapter(gattServiceAdapter);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public void returnToMoveList(View view) {
        //open MoveListActivity
        //TODO: backbuttonbehaviour ?
        // Stop Searching? is it already stopped? idk
        final Intent intent = new Intent(DebugActivity.this, MoveListActivity.class);
        intent.putExtra(DebugActivity.EXTRAS_DEVICE_NAME, mDeviceName);
        intent.putExtra(DebugActivity.EXTRAS_DEVICE_ADDRESS, mDeviceAddress);
        startActivity(intent);
    }

    public void disconnectDevice(View view) {
        // TODO: make sure connection stays active until explicitly disconnected.
        //  so we can still send data in different activity (movelistactivity)..
        Log.d(TAG, "should disconnect device in the future");
        Toast.makeText(this, "not implemented yet", Toast.LENGTH_SHORT).show();
    }
    /**
     * TODO: method to create gatt service characteristic and write
     *
     */
    private void writeBLE(String data){
        if(mWriteCharacteristic != null){
            mWriteCharacteristic.setValue(data);
            mBluetoothService.writeCharacteristic(mWriteCharacteristic);
        }
    }

}
