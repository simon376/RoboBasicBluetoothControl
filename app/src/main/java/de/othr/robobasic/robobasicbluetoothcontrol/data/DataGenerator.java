package de.othr.robobasic.robobasicbluetoothcontrol.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates data to pre-populate the database
 */
class DataGenerator {

    private static final String[] FIRST = new String[]{
            "Special edition", "New", "Cheap", "Quality", "Used"};
    private static final String[] SECOND = new String[]{
            "Three-headed Monkey", "Rubber Chicken", "Pint of Grog", "Monocle"};
    private static final String[] DESCRIPTION = new String[]{
            "is finally here", "is recommended by Stan S. Stanman",
            "is the best sold product on Mêlée Island", "is \uD83D\uDCAF", "is ❤️", "is fine"};
    private static final String[] INTERVALS = new String[]{
            "Interval 1", "Interval 2", "Interval 3", "Interval 4", "Interval 5", "Interval 6"};

    static List<Move> generateMoves() {
        List<Move> moves = new ArrayList<>();
        //TODO generate some moves

        return moves;
    }

    static List<MoveSequence> generateMoveSequences(final List<Move> moves){
        List<MoveSequence> sequences = new ArrayList<>();
        //TODO generate some moveSequences

        return sequences;
    }

}