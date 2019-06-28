package de.othr.robobasic.robobasicbluetoothcontrol.activities;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.othr.robobasic.robobasicbluetoothcontrol.R;
import de.othr.robobasic.robobasicbluetoothcontrol.misc.BluetoothService;
import de.othr.robobasic.robobasicbluetoothcontrol.misc.RoboNovaGattAttributes;

/**
 * DebugActivity is used to send custom text messages to the robot to test the bluetooth connection
 * and print its responds to a Terminal
 * <p>
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


    private ShareActionProvider shareActionProvider;

    private ExpandableListView mGattServicesList;
    private BluetoothService mBluetoothService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<>();

    private BluetoothGattCharacteristic mNotifyCharacteristic;

    /**
     * A Service Connection is used to manage the Service's lifecycle.
     */
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

    /**
     * The BroadcastReceiver is used to receive Broadcasts from the Android OS,
     * which we're sent by the BluetoothService, reacting to Bluetooth GATT Events.
     * ACTION_GATT_CONNECTED:           connected to a GATT server.
     * ACTION_GATT_DISCONNECTED:        disconnected from a GATT server.
     * ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
     * ACTION_DATA_AVAILABLE:           received data from the device.  This can be a result of read or notification operations.
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothService.ACTION_GATT_CONNECTED.equals(action)) {
                boolean mConnected = true;
                mConnectionProgressBar.setVisibility(View.INVISIBLE);
                runOnUiThread(() -> Toast.makeText(DebugActivity.this, getResources().getString(R.string.connected), Toast.LENGTH_SHORT).show());

            } else if (BluetoothService.ACTION_GATT_DISCONNECTED.equals(action)) {
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

    /**
     * OnClickListener for the ExpandableListView containing the GATT Services & Characteristics.
     */
    private final ExpandableListView.OnChildClickListener servicesListClickListener =
            new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    if (mGattCharacteristics != null) {
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
                            Log.d(TAG,"selected Read Characteristic, requesting read.");
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = characteristic;
                            mBluetoothService.setCharacteristicNotification(
                                    characteristic, true);
                            Log.d(TAG,"selected Notify Characteristic, enabling notifications.");
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                            mBluetoothService.setWritableCharacteristic(characteristic);
                            Log.d(TAG,"selected Write Characteristic.");
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

        // get selected Device Data from Intent Extras to connect to it.
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        if(mDeviceName == null)
            mDeviceName = "device";
        if(mDeviceAddress == null)
            mDeviceAddress = "unknown address";

        //Save selected device in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.saved_mac_address_key), mDeviceAddress);
        editor.apply();


        // Sets up UI references.
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        String toolbarTitle = mDeviceName + ": " + mDeviceAddress;
        Objects.requireNonNull(getSupportActionBar()).setTitle(toolbarTitle);

        mConnectionProgressBar = findViewById(R.id.progressBar);
        mConnectionProgressBar.setIndeterminate(true);
        mConnectionProgressBar.setVisibility(View.VISIBLE);
        mConnectionProgressBar.setScaleY(4);

        mGattServicesList = findViewById(R.id.gatt_services_list);
        mGattServicesList.setOnChildClickListener(servicesListClickListener);
        mTerminal = findViewById(R.id.tv_terminal);
        mTerminal.setMovementMethod(new ScrollingMovementMethod());


        mDataField = findViewById(R.id.et_dbg_send);
        Button sendButton = findViewById(R.id.btn_dbg_send);
        sendButton.setOnClickListener(v -> {
            String data = mDataField.getText().toString();
            mBluetoothService.writeDataToCharacteristic(data);
            mDataField.getText().clear();
        });

        //Connect to BluetoothService
        Intent gattServiceIntent = new Intent(this, BluetoothService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
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

    /** method to update the share intent data */
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

    /** method to update the terminal text data */
    private void displayData(String data) {
        if (data != null) {
            mTerminal.append(("\n" + data));
            setShareIntent(mTerminal.getText().toString());
        }
    }

    /**
     * Iterates through all the GATT Services and displays all found Services & Characteristics
     * in a ExpandableListView
     * @param gattServices List of Bluetooth GATT Services
     */
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
        String LIST_UUID = "UUID";
        String LIST_NAME = "NAME";
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, RoboNovaGattAttributes.lookupService(uuid, unknownServiceString));
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

    /**
     * Create an Intent Filter used to filter the Broadcasts received by the BroadcastReceiver
     * @return IntentFilter
     */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    /**
     * Return to move list.
     *
     * @param view the view
     */
    public void returnToMoveList(View view) {
        //open MoveListActivity
        final Intent intent = new Intent(DebugActivity.this, MoveListActivity.class);
        intent.putExtra(DebugActivity.EXTRAS_DEVICE_NAME, mDeviceName);
        intent.putExtra(DebugActivity.EXTRAS_DEVICE_ADDRESS, mDeviceAddress);
        startActivity(intent);
    }


}
