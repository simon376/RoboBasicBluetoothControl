package de.othr.robobasic.robobasicbluetoothcontrol.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.othr.robobasic.robobasicbluetoothcontrol.R;
import de.othr.robobasic.robobasicbluetoothcontrol.data.ListItem;
import de.othr.robobasic.robobasicbluetoothcontrol.data.Move;
import de.othr.robobasic.robobasicbluetoothcontrol.data.MoveSequence;

/**
 * Adapter to use with a RecyclerView to show the List of Moves and MoveSequences (no seperate treatment!)
 */
//TODO: for now, only MOVEs are supported, MoveSequences might be added back in later
public class MoveListAdapter extends RecyclerView.Adapter<MoveListAdapter.ViewHolder> {


    private List<Move> mItems;

    private final int MOVE = 0, SEQUENCE = 1;

    private final String TAG = MoveListAdapter.class.getSimpleName();


    public void setClickHandler(MoveListAdapterOnItemClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    private MoveListAdapterOnItemClickHandler mClickHandler;

    /**
     * The interface that receives onItemClick messages.
     */
    public interface MoveListAdapterOnItemClickHandler {
        void onItemClick(Move move);
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mNameTextView;
        private final ImageView mIconView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mNameTextView = itemView.findViewById(R.id.tv_cv_move);
            mIconView = itemView.findViewById(R.id.iv_cv_move);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Move move = mItems.get(position);
                mClickHandler.onItemClick(move);
            }

        }
    }



    public void setMoves(List<Move> moves){
        mItems = moves;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View moveView = inflater.inflate(R.layout.item_move, parent, false);
        return new ViewHolder(moveView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Object item = mItems.get(position);
        if(item != null){
            holder.mNameTextView.setText( ((Move) item).getName());

//            switch(holder.getItemViewType()) {
//                case SEQUENCE:
//                    //holder.mIconView
//                    holder.mNameTextView.setText( ((MoveSequence) item).getName());
//                    break;
//                case MOVE:
//                    holder.mNameTextView.setText( ((Move) item).getName());
//                    break;
//                default:
//                    Log.e(TAG, "RV item is neither sequence nor move");
//                    break;

        }
        else{
            // Covers the case of data not being ready yet.
            Log.i(TAG, "OnBindViewHolder data not ready.");
            holder.mNameTextView.setText("Move #" + String.valueOf(position));
        }
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Deprecated
    @Override
    public int getItemViewType(int position) {
//        if (mItems.get(position) instanceof Move) {
//            return MOVE;
//        } else if (mItems.get(position) instanceof MoveSequence) {
//            return SEQUENCE;
//        }
        return -1;
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

}
