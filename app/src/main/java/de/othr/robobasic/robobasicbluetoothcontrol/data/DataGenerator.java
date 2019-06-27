package de.othr.robobasic.robobasicbluetoothcontrol.data;

import java.util.ArrayList;
import java.util.List;

import de.othr.robobasic.robobasicbluetoothcontrol.R;

import static java.lang.Math.min;

/**
 * Generates data to pre-populate the database
 */
class DataGenerator {

    private static final String[] NAME = new String[]{
            "Stable 1",
            "Stable 2",
            "Stable 3",
            "King Kong",
            "Clapping",
            "Right Arm Chest",
            "Dance 1",
            "Dance 2",
            "Dance 3",
            "Dance 4"};
    private static final String[] SEQUENCES = new String[]{
            "Three-headed Monkey", "Rubber Chicken", "Pint of Grog", "Monocle", "Random Name 5", "test", "blablabla", "moveSequence", "number 9", "thisisit"};
    private static final String[] MESSAGE = new String[]{
            "lift left arm", "lift right arm", "lower left arm", "lower right arm", "go forward", "go backwards", "ID5437", "#3513680", "0x52081408", "message"};


    static List<Move> generateMoves() {
        List<Move> moves = new ArrayList<>();

        //TODO: use emojis instead of images? or Take actual pictures?
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