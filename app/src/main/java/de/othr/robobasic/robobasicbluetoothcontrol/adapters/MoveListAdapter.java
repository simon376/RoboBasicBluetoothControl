package de.othr.robobasic.robobasicbluetoothcontrol.adapters;

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
import de.othr.robobasic.robobasicbluetoothcontrol.data.Move;

/**
 * Adapter to use with a RecyclerView to show the List of Moves (and MoveSequences)
 * for now, only {@link Move}s are supported, MoveSequences were removed to keep the code clear,
 * since there wasn't enough time to implement everything
 */
public class MoveListAdapter extends RecyclerView.Adapter<MoveListAdapter.ViewHolder> {

    private List<Move> mItems;
    private final String TAG = MoveListAdapter.class.getSimpleName();

    /**
     * Sets OnItemClick handler.
     *
     * @param mClickHandler the m click handler
     */
    public void setClickHandler(MoveListAdapterOnItemClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    private MoveListAdapterOnItemClickHandler mClickHandler;

    /**
     * The interface that receives onItemClick messages.
     */
    public interface MoveListAdapterOnItemClickHandler {
        /**
         * On item click.
         *
         * @param move selected move
         */
        void onItemClick(Move move);
    }


    /**
     * ViewHolder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mNameTextView;
        private final ImageView mIconView;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        ViewHolder(@NonNull View itemView) {
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


    /**
     * Set list of moves.
     *
     * @param moves the List of Moves
     */
    public void setMoves(List<Move> moves){
        mItems = moves;
        Log.d(TAG, ("#items: " + mItems.size()));
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

        Move item = mItems.get(position);
        if(item != null){
            holder.mNameTextView.setText( item.getName());
            holder.mIconView.setImageResource(item.getDrawable());
        }
        else{
            // Covers the case of data not being ready yet.
            Log.i(TAG, "OnBindViewHolder data not ready.");
            holder.mNameTextView.setText("move # " + position);
        }
    }



    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

}
