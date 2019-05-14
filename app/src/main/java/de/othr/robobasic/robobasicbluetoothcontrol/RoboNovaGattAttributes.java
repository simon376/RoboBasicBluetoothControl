package de.othr.robobasic.robobasicbluetoothcontrol;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
class RoboNovaGattAttributes {
    private static final HashMap<String, String> attributes = new HashMap<>();
    static final String ROBONOVA_CHARACTERISTIC = "00002a37-0000-1000-8000-00805f9b34fb";
    static final String ROBONOVA_SERVICE = "0000180d-0000-1000-8000-00805f9b34fb";

    static {
        //TODO: Hier die Attribute des RoboNovas speichern (werte ändern)
        attributes.put(ROBONOVA_SERVICE, "RoboNova XY Service");

        attributes.put(ROBONOVA_CHARACTERISTIC, "RoboNova Characteristic");

        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }

    static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}