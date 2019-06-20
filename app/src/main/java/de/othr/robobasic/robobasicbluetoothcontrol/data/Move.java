package de.othr.robobasic.robobasicbluetoothcontrol.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import de.othr.robobasic.robobasicbluetoothcontrol.R;

/**
 * Move model describes a single predefined movement the robot can do.
 * fields aren't final yet.
 */
@Entity
public class Move {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;    /* Name of the move shown in View */

    private String message; /* message sent to the robot identifying the specific move */

    private int drawable;    // icon

    public Move(){}

    @Ignore
    public Move(String name, String message){
        this.name = name; this.message = message; this.drawable = R.drawable.icon_default;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
