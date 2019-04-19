package de.othr.robobasic.robobasicbluetoothcontrol.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Move model describes a single predefined movement the robot can do.
 * fields aren't final yet.
 */
@Entity
public class Move {

    @PrimaryKey(autoGenerate = true)
    private int id;

    String name;    /* Name of the move shown in View */

    String message; /* message sent to the robot identifying the specific move */


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
