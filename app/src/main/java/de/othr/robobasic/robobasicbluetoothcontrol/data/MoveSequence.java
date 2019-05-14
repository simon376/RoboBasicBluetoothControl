package de.othr.robobasic.robobasicbluetoothcontrol.data;

import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
// !! Wird vorerst nicht benutzt! Kann später verwendet werden, sobald die Basics funktionieren!
@Deprecated
@Entity
public class MoveSequence extends ListItem{

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    @Ignore
    public String getName() {
        return name;
    }

    public MoveSequence(){}

    public MoveSequence(String name, List<TimedMove> moves){
        this.name = name;
        this.timedMoveList = moves;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;    /* Name of the move sequence shown in View */

    /*
     using TypeConverters and no Foreign Keys means the list of TimedMoves will just be serialized
     and there will be no connection to the Moves-Database whatsoever!
     the other option would be to have TimedMove as a Entity (like Move) and TimedMove would be
     set up as a foreignkey to MoveSequence, but there couldnt be a reference like List<TimedMove>
     because Room entities cannot refer to other entities. you would have to associate them to one
     another using a viewmodel or sth similar
    */
    @ColumnInfo(name = "move_list")
    private
    List<TimedMove> timedMoveList; /* list of moves to be executed by the robot for this sequence */


}
