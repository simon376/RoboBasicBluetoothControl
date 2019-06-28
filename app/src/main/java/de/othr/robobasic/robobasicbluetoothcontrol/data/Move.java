package de.othr.robobasic.robobasicbluetoothcontrol.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import de.othr.robobasic.robobasicbluetoothcontrol.R;

/**
 * Move model describes a single predefined movement the robot can do.
 */
@Entity
public class Move {

    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * Name of the move shown in View
     */
    private String name;

    /**
     * message sent to the robot identifying the specific move
     */
    private String message;

    /**
     * drawable resource id which will be shown as the move's icon
     */
    private int drawable;

    /**
     * Instantiates a new Move.
     */
    public Move(){}

    /**
     * Instantiates a new Move.
     *
     * @param name    the name
     * @param message the message
     */
    @Ignore
    public Move(String name, String message){
        this.name = name; this.message = message; this.drawable = R.drawable.icon_default;
    }

    /**
     * Instantiates a new Move.
     *
     * @param name               the name
     * @param message            the message
     * @param drawableResourceId the drawable resource id
     */
    @Ignore
    public Move(String name, String message, int drawableResourceId){
        this.name = name; this.message = message; this.drawable = drawableResourceId;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets drawable.
     *
     * @return the drawable
     */
    public int getDrawable() {
        return drawable;
    }

    /**
     * Sets drawable.
     *
     * @param drawable the drawable
     */
    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
