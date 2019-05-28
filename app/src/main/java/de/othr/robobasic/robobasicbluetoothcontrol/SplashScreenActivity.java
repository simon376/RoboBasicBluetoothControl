package de.othr.robobasic.robobasicbluetoothcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String defaultAddress = "ff-ff-ff-ff-ff-ff";
        String macAddress = sharedPreferences.getString(getString(R.string.saved_mac_address_key), defaultAddress);
        if(macAddress.equals(defaultAddress)){
            // no mac address saved -> open "main"activity to scan for device
            startActivity(new Intent(this, MainActivity.class));
        }
        else{
            startActivity(new Intent(this, MoveListActivity.class));
            //TODO: irgendwie auch das richtige GattCharacteristic speichern und hier automatisch "connecten"
        }
    }
}
