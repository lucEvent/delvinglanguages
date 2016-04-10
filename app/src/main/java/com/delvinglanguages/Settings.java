package com.delvinglanguages;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;

public class Settings {

    public static final boolean DEBUG = true;

    /**
     * ************** Default values *********************
     **/
    private static final String DEFAULT_APP_LANGUAGE_CODE = "0";
    private static final boolean DEFAULT_PHONKB_VIBRATION = true;
    private static final int DEFAULT_APP_THEME = 0;
    private static final int DEFAULT_CURRENT_LANGUAGE = -1;

    private static String APP_LANGUAGE_CODE;
    private static String PHONKB_VIBRATION;
    private static String APP_THEME;
    private static String CURRENT_LANGUAGE;

    private static SharedPreferences preferences;

    public static final int NUMBER_OF_TYPES = 9;
    public static int[] colors;

    public Settings(Context context) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            APP_LANGUAGE_CODE = context.getString(R.string.pref_app_language_key);
            PHONKB_VIBRATION = context.getString(R.string.pref_phonetic_keyboard_vibration_key);
            APP_THEME = context.getString(R.string.pref_app_theme_key);
            CURRENT_LANGUAGE = context.getString(R.string.pref_current_language_key);
        }
        if (colors == null) {
            colors = context.getResources().getIntArray(R.array.colors);
        }
    }

    public static int getAppLanguageCode() {
        String scode = preferences.getString(APP_LANGUAGE_CODE, DEFAULT_APP_LANGUAGE_CODE);
        return Integer.parseInt(scode);
    }

    public static boolean getPreferencePhonKBVibration() {
        return preferences.getBoolean(PHONKB_VIBRATION, DEFAULT_PHONKB_VIBRATION);
    }

    public static int getCurrentLanguage() {
        return preferences.getInt(CURRENT_LANGUAGE, DEFAULT_CURRENT_LANGUAGE);
    }

    public static void setCurrentLanguage(int id) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(CURRENT_LANGUAGE, id);
        editor.apply();
    }

    /*
    public static void setBackgroundTo(View view) {
        if (bg_type == BG_COLOR_ON) {
            view.setBackgroundColor(bg_color);
        } else if (bg_type == BG_IMAGE_ON) {
            view.setBackground(getBackgroundImage());
        }
    }

    public static Drawable getBackgroundImage() {
        if (backgroundimage == null) {
            backgroundimage = Drawable.createFromPath(bg_imagePath);
        }
        return backgroundimage;
    }
    public static String createDLFilePath() {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        return disco.getFolderPath() + File.separator + time + ".delv";
    }
    public static void setBackgroundColorsforType(View[] labels, int type) {
        for (int i = 0; i < labels.length; ++i) {
            if ((type & (1 << i)) != 0) {
                labels[i].setBackgroundColor(colors[i]);
            } else {
                labels[i].setBackgroundColor(0xFFCCCCCC);
            }
        }
    }


*/
    public static final int PROGRESS_COLOR_OK = 0xff33aa33;
    public static final int PROGRESS_COLOR_MISS = 0xffaa3333;
    public static final PorterDuff.Mode PROGRESS_COLOR_MODE = PorterDuff.Mode.SRC_ATOP;
    public static final long TEST_AFTER_HIT_WAITING_TIME = 500; //ms.

}
