package de.othr.robobasic.robobasicbluetoothcontrol.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.othr.robobasic.robobasicbluetoothcontrol.R;
import de.othr.robobasic.robobasicbluetoothcontrol.adapters.DeviceAdapter;

/**
 * MainActivity starts search for Bluetooth devices and shows them in a RecyclerView,
 * connect to one by clicking on it
 *
 *
 * in the future this will only be shown once as a splash-screen type of thing and movelistactivity
 * should be the entrypoint of the app
 */
public class MainActivity extends AppCompatActivity {


    private final static String TAG = MainActivity.class.getSimpleName();

    private View mLayout;
    private ProgressBar mScanProgressBar;
    private ObjectAnimator mProgressAnimator;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothScanner;
    private Handler mHandler;
    private DeviceAdapter mDeviceListAdapter;
    private RecyclerView mRecyclerView;


    // Stops scanning after 10 seconds.
    private static final long   SCAN_PERIOD = 10000;
    private static final int    REQUEST_ENABLE_BT = 1;

    private static final int PERMISSION_REQUEST_LOCATION = 0;


    private final ArrayList<BluetoothDevice> mDevices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.app_name));

        mLayout = findViewById(R.id.main_layout);
        mScanProgressBar = findViewById(R.id.progressBar);
        mScanProgressBar.setIndeterminate(false);
        mScanProgressBar.setProgress(0);
        mProgressAnimator = ObjectAnimator.ofInt(mScanProgressBar,"progress", 0, 100);
        mProgressAnimator.setDuration(SCAN_PERIOD);
        mProgressAnimator.setInterpolator(new DecelerateInterpolator());

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

        //for debugging only
        createSampleData(3);

        mDeviceListAdapter = new DeviceAdapter(mDevices);
        mDeviceListAdapter.setOnItemClickListener((itemView, position) -> {
            final BluetoothDevice device = mDevices.get(position);
            if (device == null) return;

            //tell MainActivity to stop scanning
            scanDevice(false);

            //open DebugActivity to Connect to Device
            final Intent intent = new Intent(MainActivity.this, DebugActivity.class);
            intent.putExtra(DebugActivity.EXTRAS_DEVICE_NAME, device.getName());
            intent.putExtra(DebugActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
            startActivity(intent);

        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setAdapter(mDeviceListAdapter);

        mHandler = new Handler();

    }

    public void startScanning(View view) {
        // Check if the Location permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start scanning

            mScanProgressBar.setVisibility(View.VISIBLE);

            mProgressAnimator.start();

            scanDevice(true);
            mDeviceListAdapter.clear(); // remove sample devices
        } else {
            // Permission is missing and must be requested.
            requestLocationPermission();
        }


    }

    //TODO: move to SplashScreenActivity

    /**
     * Requests the {@link android.Manifest.permission#ACCESS_COARSE_LOCATION} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private void requestLocationPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
            Snackbar.make(mLayout, getString(R.string.location_access_req),
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ok), view -> {
                        // Request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_LOCATION);
                    }).show();

        } else {
            Snackbar.make(mLayout, "location unavailable", Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, "location permission granted",
                        Snackbar.LENGTH_SHORT)
                        .show();


            scanDevice(true);

            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "location permission denied",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }


    private void createSampleData(int num) {
        String[] addresses = {"00:11:22:33:AA:BB","44:55:66:77:88:BB","00:22:44:66:AA:BB"};
        for (int i = 0; i < num; i++) {
            String address = addresses[i%3];
            if(BluetoothAdapter.checkBluetoothAddress(address)){
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                if(device != null){
                    mDevices.add(device);
                }
                else
                {
                    String error = "device is null breakpoint " + address; //exception handling at its finest... lol
                    Log.e(TAG, error);
                }
            }
            else{
                String error = "bluetooth address invalid breakpoint " + address;
                Log.e(TAG, error);
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

    }

    private void scanDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(() -> {
                mBluetoothScanner.stopScan(mScanCallback);
                mScanProgressBar.setVisibility(View.INVISIBLE);
              //  invalidateOptionsMenu();
            }, SCAN_PERIOD);

            mBluetoothScanner.startScan(mScanCallback);


        } else {
            mBluetoothScanner.stopScan(mScanCallback);

        }
    }


    // Device scan callback.
    private final ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            // new devices will be inserted at the top
            mDeviceListAdapter.addDevice(0, result.getDevice());
            mRecyclerView.scrollToPosition(0);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            for(ScanResult result : results){
                mDeviceListAdapter.addDevice(0,result.getDevice());
            }
            mRecyclerView.scrollToPosition(0);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };



}
