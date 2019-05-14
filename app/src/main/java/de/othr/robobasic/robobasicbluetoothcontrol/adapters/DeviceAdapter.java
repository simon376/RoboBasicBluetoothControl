package de.othr.robobasic.robobasicbluetoothcontrol.adapters;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.othr.robobasic.robobasicbluetoothcontrol.R;

/**
* Adapter for displaying found bluetooth le devices in a RecyclerView in the MainActivity
*/
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private final List<BluetoothDevice> mDevices;

    /***** Creating OnItemClickListener *****/

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mNameTextView;
        final TextView mAddressTextView;
        final TextView mClassTextView;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.tv_item_device_name);
            mAddressTextView = itemView.findViewById(R.id.tv_item_device_mac);
            mClassTextView = itemView.findViewById(R.id.tv_item_device_class);

            // Setup the click listener
            itemView.setOnClickListener(v -> {
                // Triggers click upwards to the adapter on click
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemView, position);
                    }
                }
            });

        }

    }

    public DeviceAdapter(List<BluetoothDevice> devices){
        mDevices = devices;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View deviceView = inflater.inflate(R.layout.item_device, parent, false);

        return new ViewHolder(deviceView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        BluetoothDevice device = mDevices.get(position);

        int deviceClass = device.getBluetoothClass().getDeviceClass();

        TextView tvName = holder.mNameTextView;
        TextView tvAddress = holder.mAddressTextView;
        TextView tvClass = holder.mClassTextView;
        String name = device.getName();
        String address = device.getAddress();
        if(name != null)
            tvName.setText(name);
        else
            tvName.setText(("Default Device Name " + position));
        if(address != null)
            tvAddress.setText(address);
        if(deviceClass != 0)
            tvClass.setText(getClassMajor(deviceClass));



    }

    // small helper function to show BluetoothClass as String, could be replaced later by nice symbols
    private String getClassMajor(int constant){
        switch(constant){
            case BluetoothClass
                    .Device.Major.AUDIO_VIDEO:
                return "AUDIO_VIDEO";
            case BluetoothClass
                    .Device.Major.COMPUTER:
                return "COMPUTER";
            case BluetoothClass
                    .Device.Major.HEALTH:
                return "HEALTH";
            case BluetoothClass
                    .Device.Major.IMAGING:
                return "IMAGING";
            case BluetoothClass
                    .Device.Major.MISC:
                return "MISC";
            case BluetoothClass
                    .Device.Major.NETWORKING:
                return "NETWORKING";
            case BluetoothClass
                    .Device.Major.PERIPHERAL:
                return "PERIPHERAL";
            case BluetoothClass
                    .Device.Major.PHONE:
                return "PHONE";
            case BluetoothClass
                    .Device.Major.TOY:
                return "TOY";
            case BluetoothClass
                    .Device.Major.UNCATEGORIZED:
                return "UNCATEGORIZED";
            case BluetoothClass
                    .Device.Major.WEARABLE:
                return "WEARABLE";
            default:
                return "UNKNOWN";
        }
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
