package de.othr.robobasic.robobasicbluetoothcontrol;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private List<BluetoothDevice> mDevices;

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
                //TODO: Connect to Device, open Debug screen
//                final Intent intent = new Intent(this, DebugActivity.class);
////                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
////                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
//                if (mScanning) {
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                    mScanning = false;
//                }
//                startActivity(intent);

            }

        }
    }

    public DeviceAdapter(List<BluetoothDevice> devices){
        mDevices = devices;
    }

    @NonNull
    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

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
        return mDevices.size();
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
