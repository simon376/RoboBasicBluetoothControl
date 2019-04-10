package de.othr.robobasic.robobasicbluetoothcontrol;


import java.util.ArrayList;
import java.util.Random;

/**
 * Model for a message received by or sent to the robonova robot.
 *
 */
public class Message {

    private int mId;
    private float mTime;
    private String mText;

    private static int totalMessages = 0;

    public Message(int id, float time, String text){
        mId = id;
        mTime = time;
        mText = text;

        totalMessages++;
    }

    public Message(String text){
        mId = 42; mTime = 3.1415f; mText = text;
    }

    public int getId() {
        return mId;
    }

    public float getTime() {
        return mTime;
    }

    public String getText() {
        return mText;
    }

    public String getString() {
        String s = ( String.valueOf(mId) + " (" + String.valueOf(mTime) + "): " + mText);
        return s;
    }

    public static int getTotalMessageCount() {
        return totalMessages;
    }

    public static ArrayList<Message> createTestMessages(int num){
        ArrayList<Message> messages = new ArrayList<>();

        for(int i = 1; i <= num; i++){
            messages.add(new Message(i,i*3.5f,("this is a test message nr. " + i)));
        }

        return messages;
    }
}
