package de.othr.robobasic.robobasicbluetoothcontrol.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.min;

/**
 * Generates data to pre-populate the database
 */
class DataGenerator {

    private static final String[] NAME = new String[]{
            "Left Arm Up", "Right Arm Up", "Left Arm Down", "Right Arm Down", "Move Forward", "Move Backwards", "Turn around", "Sit Down", "Stand Up", "Reset"};
    private static final String[] SEQUENCES = new String[]{
            "Three-headed Monkey", "Rubber Chicken", "Pint of Grog", "Monocle", "Random Name 5", "test", "blablabla", "moveSequence", "number 9", "thisisit"};
    private static final String[] MESSAGE = new String[]{
            "lift left arm", "lift right arm", "lower left arm", "lower right arm", "go forward", "go backwards", "ID5437", "#3513680", "0x52081408", "message"};


    static List<Move> generateMoves() {
        List<Move> moves = new ArrayList<>();
        int max = min(NAME.length, MESSAGE.length);
        for(int i = 0; i < max; i++){
            moves.add(new Move(NAME[i], MESSAGE[i]));
        }
        return moves;
    }

    @Deprecated
    static List<MoveSequence> generateMoveSequences(final List<Move> moves){
        List<MoveSequence> sequences = new ArrayList<>();
        int n = moves.size();
        // have n moves, pick k random from those n, add random timings, put it into one sequence
        for(int i = 0; i < SEQUENCES.length; i++){

            List<TimedMove> timedMoves = new ArrayList<>();

            int k = new Random().nextInt(n+1);  // number of moves this MoveSequence has

            for(int j = 0; j < k; j++){
                int index = new Random().nextInt(k+1);
                Move randomMove = moves.get(index);

                int randomTiming = new Random().nextInt(10000);

                timedMoves.add(new TimedMove(randomTiming, randomMove));

            }

            sequences.add(new MoveSequence(SEQUENCES[i], timedMoves));
        }


        return sequences;
    }

}