package de.othr.robobasic.robobasicbluetoothcontrol.data;

public class TimedMove {
    int timestamp;  // timestamp when this move is to be executed
    Move move;      // which move to execute


    public TimedMove(int timestamp, Move move){
        this.timestamp = timestamp;
        this.move = move;
    }

}
