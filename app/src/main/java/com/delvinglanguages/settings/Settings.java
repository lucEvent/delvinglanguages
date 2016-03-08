package com.delvinglanguages.settings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.delvinglanguages.R;
import com.delvinglanguages.data.ControlDisco;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Settings {

    public static final boolean DEBUG = true;

    private static final String settings = "settings.dat";

    // Settings de background
    public static final int BG_NO_BG = -1;
    public static final int BG_IMAGE_ON = 0;
    public static final int BG_COLOR_ON = 1;

    private static String bg_imagePath; // (Se guarda) \\
    private static int bg_color; // (Se guarda) \\
    private static int bg_type; // (Se guarda) \\
    private static Drawable backgroundimage;

    // Tipos de palabras
    public static int[] colors;

    // Settings idioma materno
    public static String NativeLanguage = ""; // (Se guarda) \\
    public static int NativeLanguageCode = 0; // (Se guarda) \\
    private static boolean vibration;

    private static ControlDisco disco;

    /**
     * *********************** CREADORA ************************
     **/
    public Settings(Context context) {
        disco = new ControlDisco();
        readSettings();
        colors = context.getResources().getIntArray(R.array.colors);
    }

    /**
     * *********************** ESTADO ************************
     **/
    private static void readSettings() {
        String[] params = disco.readParams(settings);

        if (params == null) {
            setDefault();
            return;
        }

        // 1. Se leen las variables de BACKGROUND
        bg_imagePath = params[0];
        bg_color = Integer.parseInt(params[1]);
        bg_type = Integer.parseInt(params[2]);

        // 2. Se lee el idioma materno
        NativeLanguage = params[3];
        NativeLanguageCode = Integer.parseInt(params[4]);

        vibration = Boolean.parseBoolean(params[5]);
    }

    private static void saveSettings() {
        String[] params = new String[6];

        // 1. Se guardan las variables de BACKGROUND
        params[0] = bg_imagePath;
        params[1] = Integer.toString(bg_color);
        params[2] = Integer.toString(bg_type);

        // 2. Se guarda las settings de la app
        params[3] = NativeLanguage;
        params[4] = Integer.toString(NativeLanguageCode);
        params[5] = Boolean.toString(vibration);

        disco.saveParams(settings, params);
    }

    private static void setDefault() {
        bg_imagePath = "";
        bg_color = 0;
        bg_type = BG_NO_BG;

        NativeLanguage = "Native Language";
        NativeLanguageCode = 0;
        vibration = true;
    }

    /**
     * **************** DIFERENTES CONFIGURACIONES *******************
     **/

    public static void setNativeLanguage(int code, String name) {
        NativeLanguage = name;
        NativeLanguageCode = code;
        saveSettings();
    }

    public static void setBackground(int imageColor) {
        backgroundimage = null;
        if (bg_type == BG_IMAGE_ON) {
            new File(bg_imagePath).delete();
        }

        bg_type = BG_COLOR_ON;
        bg_color = imageColor;

        saveSettings();
    }


    public static void setBackground(String imagePath) {
        bg_imagePath = createDLFilePath();
        disco.copyFile(imagePath, bg_imagePath);
        setBackgroundImagePath(bg_imagePath);
    }

    public static void setBackgroundImagePath(String imagePath) {
        backgroundimage = null;
        if (bg_type == BG_IMAGE_ON) {
            new File(bg_imagePath).delete();
        }

        bg_type = BG_IMAGE_ON;
        bg_imagePath = imagePath;
        saveSettings();
    }

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

    public static void toggleVibration() {
        vibration = !vibration;
        saveSettings();
    }

    public static boolean vibration() {
        return vibration;
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

    private void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##Settings##", text);
    }

}
