package de.othr.robobasic.robobasicbluetoothcontrol;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
/**
* Adapter for displaying found devices in a RecyclerView
*/
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private List<BluetoothDevice> mDevices;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mNameTextView;
        TextView mAddressTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.tv_item_device_name);
            mAddressTextView = itemView.findViewById(R.id.tv_item_device_mac);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                final BluetoothDevice device = mDevices.get(position);
                if (device == null) return;
                //TODO: open DebugActivity to Connect to Device
                final Intent intent = new Intent(mContext, DebugActivity.class);
                intent.putExtra(DebugActivity.EXTRAS_DEVICE_NAME, device.getName());
                intent.putExtra(DebugActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());


                //TODO: somehow tell MainActivity to stop scanning
                // or do it inside the DebugActivity class
//                if (mScanning) {
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                    mScanning = false;
//                }
                mContext.startActivity(intent);

            }

        }
    }

    public DeviceAdapter(List<BluetoothDevice> devices){
        mDevices = devices;
    }

    @NonNull
    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View deviceView = inflater.inflate(R.layout.item_device, parent, false);

        ViewHolder viewHolder = new ViewHolder(deviceView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.ViewHolder holder, int position) {
        BluetoothDevice device = mDevices.get(position);

        TextView tvName = holder.mNameTextView;
        TextView tvAddress = holder.mAddressTextView;
        tvName.setText(device.getName());
        tvAddress.setText(device.getAddress());

    }

    @Override
    public int getItemCount() {
        if(mDevices!= null)
            return mDevices.size();
        else
            return 0;
    }

    public void addDevice(BluetoothDevice device){
        if(!mDevices.contains(device)){
            mDevices.add(device);
        }
    }

    public  BluetoothDevice getDevice(int position){
        return mDevices.get(position);
    }

    public void clear(){
        mDevices.clear();
    }


}
