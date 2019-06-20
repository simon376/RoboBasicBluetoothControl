package de.othr.robobasic.robobasicbluetoothcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import de.othr.robobasic.robobasicbluetoothcontrol.R;

/**
 * CreateMoveSequenceActivity will be used to create custom combinations of movements for the robot to act out
 */
public class CreateMoveSequenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_movesequence);
    }
}
