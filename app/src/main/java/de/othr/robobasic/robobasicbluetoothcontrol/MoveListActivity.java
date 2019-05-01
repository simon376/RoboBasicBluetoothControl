package de.othr.robobasic.robobasicbluetoothcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.othr.robobasic.robobasicbluetoothcontrol.adapters.MoveListAdapter;
import de.othr.robobasic.robobasicbluetoothcontrol.data.ListItem;
import de.othr.robobasic.robobasicbluetoothcontrol.data.Move;
import de.othr.robobasic.robobasicbluetoothcontrol.viewmodels.MyViewModel;

public class MoveListActivity extends AppCompatActivity {

    MyViewModel mViewModel;
    RecyclerView mRecyclerView;
    MoveListAdapter mMoveListAdapter;
    //TODO OnItemClickListener

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_list);


        //TODO connect ViewModel & RecyclerView
        mViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        // now i can use the ViewModel to access the data which is saved in the database (unknownst of the viewmodel)
        // use an Observer to observe Changes on the LiveData List of Moves and add them to the RecyclerView



        mViewModel.getMoves().observe(this, new Observer<List<Move>>() {
            @Override
            public void onChanged(List<Move> moves) {
                List<ListItem> list = new ArrayList<>(moves); //TODO
                mMoveListAdapter.setItems(list);
                // list of moves has changed, update the UI (recyclerView)
            }
        });


        mRecyclerView = findViewById(R.id.rv_moves_sequences);
        mMoveListAdapter = new MoveListAdapter();
        mMoveListAdapter.setOnItemClickListener(new MoveListAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View itemView, int position) {
                //TODO onItemClickBehaviour (send message)
            }
        });
        mRecyclerView.setAdapter(mMoveListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);


    }
}
