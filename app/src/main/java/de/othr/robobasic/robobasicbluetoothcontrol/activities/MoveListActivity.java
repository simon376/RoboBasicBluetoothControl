package de.othr.robobasic.robobasicbluetoothcontrol.activities;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import de.othr.robobasic.robobasicbluetoothcontrol.R;
import de.othr.robobasic.robobasicbluetoothcontrol.adapters.MoveListAdapter;
import de.othr.robobasic.robobasicbluetoothcontrol.misc.BluetoothService;
import de.othr.robobasic.robobasicbluetoothcontrol.viewmodel.MoveViewModel;

import static de.othr.robobasic.robobasicbluetoothcontrol.activities.DebugActivity.EXTRAS_DEVICE_ADDRESS;
import static de.othr.robobasic.robobasicbluetoothcontrol.activities.DebugActivity.EXTRAS_DEVICE_NAME;

/**
 * The MoveListActivity displays Moves saved in the Database which can be used to control the robot.
 */
public class MoveListActivity extends AppCompatActivity {

    private final static String TAG = MoveListActivity.class.getSimpleName();

    private MoveListAdapter mMoveListAdapter;

    private BluetoothService mBluetoothService;
    private BluetoothGattCharacteristic mWriteCharacteristic;

    private boolean mBound = false;

    private String mDeviceAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_list);

        // set up UI
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("RoboNova Move List");

        // get Intent Extras
        final Intent intent = getIntent();
        String mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);


        RecyclerView mRecyclerView = findViewById(R.id.rv_moves_sequences);
        mMoveListAdapter = new MoveListAdapter();
        mMoveListAdapter.setClickHandler(move -> {
            int id = move.getId();
            Log.d(TAG, "clicked on ListItem @ id "+ id);
            if(mBound){
                if(mBluetoothService != null){
                    String message = move.getMessage();
                    String toast = "clicked move " + move.getName() + "; msg: " + message;
                    Toast.makeText(MoveListActivity.this, toast, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, toast);
                    if(!mBluetoothService.writeDataToCharacteristic(message)){
                        Toast.makeText(MoveListActivity.this, "Write Characteristic not initialized.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMoveListAdapter);


        MoveViewModel mViewModel = ViewModelProviders.of(this).get(MoveViewModel.class);

        // now i can use the ViewModel to access the data which is saved in the database (unknownst of the viewmodel)
        // use an Observer to observe Changes on the LiveData List of Moves and add them to the RecyclerView
        mViewModel.getMoves().observe(this, moves -> {
            if(moves != null) {
                Log.d(TAG, ("ViewModel observed change, size:" + moves.size()));
                mMoveListAdapter.setMoves(moves);
            } else
                Log.d(TAG, "ViewModel observed change, size: null");
            // this connects the Adapter to the ViewModel
            // list of moves has changed, update the UI (recyclerView)
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent1 = new Intent(MoveListActivity.this, CreateMoveSequenceActivity.class);
            startActivity(intent1);
        });
        Log.d(TAG, "onCreate");

    }


    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, BluetoothService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }


    /** Defines callbacks for service binding, passed to bindService() */
    private final ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to BluetoothService, cast the IBinder and get BluetoothService instance

            mBluetoothService = ((BluetoothService.LocalBinder) service).getService();
            mBound = true;

            if(!mBluetoothService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothService.connect(mDeviceAddress);

            // Tell the user about this.
            View parent = findViewById(android.R.id.content);
            Snackbar.make(parent, (getString(R.string.bluetooth_service_connected) + " "+ mDeviceAddress), Snackbar.LENGTH_SHORT);

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mBluetoothService = null;
            View parent = findViewById(android.R.id.content);
            Snackbar.make(parent, getString(R.string.bluetooth_service_disconnected), Snackbar.LENGTH_SHORT);
        }
    };

}
