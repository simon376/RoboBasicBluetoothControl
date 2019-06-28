package de.othr.robobasic.robobasicbluetoothcontrol.data;

import java.util.ArrayList;
import java.util.List;

import de.othr.robobasic.robobasicbluetoothcontrol.R;

/**
 * Generates Movements to pre-populate the database
 */
class DataGenerator {


    /**
     * Generate moves list.
     *
     * @return the list of generated Moves
     */
    static List<Move> generateMoves() {
        List<Move> moves = new ArrayList<>();

        int i = 0;
        moves.add(new Move("Stable 1",              "Move " + String.valueOf(i++), R.drawable.icon_default));
        moves.add(new Move("Stable 2",              "Move " + String.valueOf(i++), R.drawable.icon_default));
        moves.add(new Move("Stable 3",              "Move " + String.valueOf(i++), R.drawable.icon_default));
        moves.add(new Move("King Kong",             "Move " + String.valueOf(i++), R.drawable.icon_default));
        moves.add(new Move("Clapping",              "Move " + String.valueOf(i++), R.drawable.icon_default));
        moves.add(new Move("Right Arm To Chest",    "Move " + String.valueOf(i++), R.drawable.icon_right));
        moves.add(new Move("Dance 1",               "Move " + String.valueOf(i++), R.drawable.icon_default));
        moves.add(new Move("Dance & Wave",          "Move " + String.valueOf(i++), R.drawable.icon_default));
        moves.add(new Move("Dance, Wave & Rotate",  "Move " + String.valueOf(i++), R.drawable.icon_default));
        moves.add(new Move("Dance & Kneel",         "Move " + String.valueOf(i++), R.drawable.icon_default));
        moves.add(new Move("Waltz",                 "Move " + String.valueOf(i++), R.drawable.icon_default));
        moves.add(new Move("Step",                  "Move " + String.valueOf(i++), R.drawable.icon_walk));
        moves.add(new Move("Shuffle Step",          "Move " + String.valueOf(i++), R.drawable.icon_default));
        moves.add(new Move("Wait until Test",       "Move " + String.valueOf(i++), R.drawable.icon_default));
        moves.add(new Move("Stand up",              "Move " + String.valueOf(i++), R.drawable.icon_get_up));
        moves.add(new Move("Wait One Second",       "Move " + String.valueOf(i++), R.drawable.icon_default));
        moves.add(new Move("Wait Five Seconds",     "Move " + String.valueOf(i++), R.drawable.icon_default));

        return moves;
    }

}