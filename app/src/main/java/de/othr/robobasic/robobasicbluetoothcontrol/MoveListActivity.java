package de.othr.robobasic.robobasicbluetoothcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.othr.robobasic.robobasicbluetoothcontrol.adapters.MoveListAdapter;
import de.othr.robobasic.robobasicbluetoothcontrol.data.ListItem;
import de.othr.robobasic.robobasicbluetoothcontrol.data.Move;
import de.othr.robobasic.robobasicbluetoothcontrol.viewmodels.MyViewModel;

import static de.othr.robobasic.robobasicbluetoothcontrol.DebugActivity.EXTRAS_DEVICE_ADDRESS;
import static de.othr.robobasic.robobasicbluetoothcontrol.DebugActivity.EXTRAS_DEVICE_NAME;

public class MoveListActivity extends AppCompatActivity {

    private final static String TAG = MoveListActivity.class.getSimpleName();

    MyViewModel mViewModel;
    RecyclerView mRecyclerView;
    MoveListAdapter mMoveListAdapter;

    BluetoothService mBluetoothService;
    boolean mBound = false;

    String mDeviceAddress;
    String mDeviceName;
    //TODO OnItemClickListener


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_list);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);



        mViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        // now i can use the ViewModel to access the data which is saved in the database (unknownst of the viewmodel)
        // use an Observer to observe Changes on the LiveData List of Moves and add them to the RecyclerView

        //TODO get deviceAddress from SharedPreferences or Intent-Extras (see other branch)


        mViewModel.getMoves().observe(this, new Observer<List<Move>>() {
            @Override
            public void onChanged(List<Move> moves) {
                mMoveListAdapter.setMoves(moves);
                // this connects the Adapter to the ViewModel
                // list of moves has changed, update the UI (recyclerView)
            }
        });


        mRecyclerView = findViewById(R.id.rv_moves_sequences);
        mMoveListAdapter = new MoveListAdapter();
        mMoveListAdapter.setClickHandler(new MoveListAdapter.MoveListAdapterOnItemClickHandler() {
            @Override
            public void onItemClick(Move move) {
                int id = move.getId();
                Log.d(TAG, "clicked on ListItem @ id "+ String.valueOf(id));
                //TODO onItemClickBehaviour (send message)
                if(mBound){
                    if(mBluetoothService != null){
                        // writeBLE(String.valueOf(id));
                    }
                }

            }
        });

        mRecyclerView.setAdapter(mMoveListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);


    }

    //TODO: this stuff should be done once in either the Startup Splashscreen Activity or Debug ?

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

//    // TODO: extract method
//    // TODO: Characteristic sollte global oder in SharedPref gespeichert werden
//    private void writeBLE(String data){
//        if(mWriteCharacteristic != null){
//            mWriteCharacteristic.setValue(data);
//            mBluetoothService.writeCharacteristic(mWriteCharacteristic);
//        }
//    }


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

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

            // Tell the user about this for our demo.
            Toast.makeText(MoveListActivity.this, (R.string.bluetooth_service_connected + mDeviceAddress),
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mBluetoothService = null;
            Toast.makeText(MoveListActivity.this, R.string.bluetooth_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };

}
