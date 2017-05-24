package learn.zhu.com.personalassistant.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by zhu on 2017/4/20.
 */

public class Utility {
    public static String getString(Context context, String field) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultValue = "";
        if(field.equals("pref_age"))
            defaultValue = "0";
        else
            defaultValue = "未设置";
        return sharedPreferences.getString(field, defaultValue);
    }

    public static boolean getBoolean(Context context, String field) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(field, false);
    }

    public static float getFloat(Context context, String field) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getFloat(field, 0);
    }
    public static void setString(Context context, String field, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(field, value);
        editor.apply();
    }
}
