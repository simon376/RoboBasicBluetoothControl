package de.othr.robobasic.robobasicbluetoothcontrol.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.othr.robobasic.robobasicbluetoothcontrol.R;

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
        moves.get(0).setDrawable(R.drawable.icon_left);
        moves.get(1).setDrawable(R.drawable.icon_up);
        moves.get(2).setDrawable(R.drawable.icon_down);
        moves.get(3).setDrawable(R.drawable.icon_right);
        moves.get(4).setDrawable(R.drawable.icon_walk);
        moves.get(5).setDrawable(R.drawable.icon_return);
        moves.get(6).setDrawable(R.drawable.icon_turn);
        moves.get(6).setDrawable(R.drawable.icon_sit_down);
        moves.get(7).setDrawable(R.drawable.icon_get_up);
        moves.get(8).setDrawable(R.drawable.icon_default);

        return moves;
    }

}