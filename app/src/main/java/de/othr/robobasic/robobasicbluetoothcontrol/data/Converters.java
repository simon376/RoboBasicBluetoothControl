package de.othr.robobasic.robobasicbluetoothcontrol.data;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Converters {

    @TypeConverter
    public static List<TimedMove> TimedMoveFromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String TimedMoveToString(List<TimedMove> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
