package de.othr.robobasic.robobasicbluetoothcontrol.data;

import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MoveSequence {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    class timedMove{
        int timestamp;  // timestamp when this move is to be executed
        Move move;      // which move to execute
    }
    //TODO: ForeignKeys

    @PrimaryKey(autoGenerate = true)
    private int id;

    String name;    /* Name of the move sequence shown in View */

   // @ColumnInfo(name = "move_list")
  //  List<timedMove> timedMoveList; /* list of moves to be executed by the robot for this sequence */
//TODO: converter? anders lösen?

}
