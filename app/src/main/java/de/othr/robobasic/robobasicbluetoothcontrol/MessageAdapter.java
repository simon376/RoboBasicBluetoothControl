package de.othr.robobasic.robobasicbluetoothcontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Simple Adapter used to display messages received by the robot in a RecyclerView on the Debug screen
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;

    public MessageAdapter(List<Message> messages){
        mMessages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate the custom layout
        View messageView = inflater.inflate((R.layout.item_message),parent,false);

        ViewHolder vh = new ViewHolder(messageView);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = mMessages.get(position);

        TextView textView = holder.mMessageTextView;
        textView.setText(message.getString());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mMessageTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mMessageTextView = itemView.findViewById(R.id.tv_item_message);
        }
    }
}
