package com.delvinglanguages;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;

public class AppSettings {

    public static final boolean DEBUG = false;

    public static final int PROGRESS_COLOR_OK = 0xff33aa33;
    public static final int PROGRESS_COLOR_MISS = 0xffaa3333;
    public static final PorterDuff.Mode PROGRESS_COLOR_MODE = PorterDuff.Mode.SRC_ATOP;
    public static final long TEST_AFTER_HIT_WAITING_TIME = 500; //ms.

    private static final int[] theme_ids = new int[]{
            R.style.default_, R.style.sunny
    };

    /**
     * ************** Default values *********************
     **/
    private static final boolean DEFAULT_PHONKB_VIBRATION = true;
    private static final String DEFAULT_APP_THEME = "0";
    private static final boolean DEFAULT_ONLINE_BACKUP = false;
    private static final int DEFAULT_CURRENT_LIST = -1;

    /**
     * Settings string keys
     */
    public static String PHONKB_VIBRATION_KEY;
    public static String APP_THEME_KEY;
    public static String ONLINE_BACKUP;
    public static String CURRENT_LIST_KEY;
    private static String LAST_SYNCHRONIZATION = "pref20";

    private static SharedPreferences preferences;

    public static final int NUMBER_OF_TYPES = 9;
    public static int[] colors;

    public static void initialize(Context context)
    {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            PHONKB_VIBRATION_KEY = context.getString(R.string.pref_phonetic_keyboard_vibration_key);
            APP_THEME_KEY = context.getString(R.string.pref_app_theme_key);
            CURRENT_LIST_KEY = context.getString(R.string.pref_current_list_key);
            ONLINE_BACKUP = context.getString(R.string.pref_backup_n_synchronization_key);
        }
        if (colors == null) {
            colors = context.getResources().getIntArray(R.array.colors);
        }
    }

    public static boolean isPhoneticKBVibrationEnabled()
    {
        return preferences.getBoolean(PHONKB_VIBRATION_KEY, DEFAULT_PHONKB_VIBRATION);
    }

    public static int getCurrentList()
    {
        return preferences.getInt(CURRENT_LIST_KEY, DEFAULT_CURRENT_LIST);
    }

    public static void setCurrentList(int id)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(CURRENT_LIST_KEY, id);
        editor.apply();
    }

    public static int getAppThemeCode()
    {
        return Integer.parseInt(preferences.getString(APP_THEME_KEY, DEFAULT_APP_THEME));
    }

    public static int getAppThemeResource()
    {
        return theme_ids[getAppThemeCode()];
    }

    public static boolean isOnlineBackUpEnabled()
    {
        return preferences.getBoolean(ONLINE_BACKUP, DEFAULT_ONLINE_BACKUP);
    }

    public static void setOnlineBackUpState(boolean state)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ONLINE_BACKUP, state);
        editor.apply();
    }

    public static long getLastSynchronization()
    {
        return preferences.getLong(LAST_SYNCHRONIZATION, 0);
    }

    public static void setLastSynchronization(long time)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(LAST_SYNCHRONIZATION, time);
        editor.apply();
    }

    public static void printlog(String msg)
    {
        if (DEBUG)
            System.out.println(msg);
    }

    public static void printerror(String msg, Exception e)
    {
        if (DEBUG) {
            System.err.println(msg);

            if (e != null)
                e.printStackTrace();
        }
    }

}
