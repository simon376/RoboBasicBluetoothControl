package de.othr.robobasic.robobasicbluetoothcontrol.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Move model describes a single predefined movement the robot can do.
 * fields aren't final yet.
 */
@Entity
public class Move extends ListItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;    /* Name of the move shown in View */

    private String message; /* message sent to the robot identifying the specific move */

    public Move(){}

    @Ignore
    public Move(String name, String message){
        this.name = name; this.message = message;
    }

    @Override
    @Ignore
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
