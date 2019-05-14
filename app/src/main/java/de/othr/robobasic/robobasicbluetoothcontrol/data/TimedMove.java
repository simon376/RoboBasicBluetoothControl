package de.othr.robobasic.robobasicbluetoothcontrol.data;
@Deprecated
public class TimedMove {
    private int timestamp;  // timestamp when this move is to be executed
    private Move move;      // which move to execute

    //To Delete. will be replaced by wait commands. see UML diagram in gitlab

    TimedMove(int timestamp, Move move){
        this.timestamp = timestamp;
        this.move = move;
    }

}
