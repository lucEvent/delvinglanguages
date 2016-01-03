package com.delvinglanguages.settings;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import com.delvinglanguages.R;
import com.delvinglanguages.data.ControlDisco;

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

    public static void setBackground(String imagePath, int imageColor, boolean isImage) {
        // Destruimos el drawable y la image anteriores
        backgroundimage = null;
        if (bg_type == BG_IMAGE_ON) {
            new File(bg_imagePath).delete();
        }
        if (isImage) {
            bg_imagePath = imagePath;
            bg_type = BG_IMAGE_ON;
        } else {
            bg_color = imageColor;
            bg_type = BG_COLOR_ON;
        }
        saveSettings();
    }

    public static void setBackgroundTo(View view) {
        if (bg_type == BG_COLOR_ON) {
            view.setBackgroundColor(getBackgroundColor());
        } else if (bg_type == BG_IMAGE_ON) {
            view.setBackground(getBackgroundImage());
        }
    }

    public static int getBackgroundType() {
        return bg_type;
    }

    public static Drawable getBackgroundImage() {
        if (bg_type != BG_IMAGE_ON) {
            return null;
        }
        if (backgroundimage == null) {
            backgroundimage = Drawable.createFromPath(bg_imagePath);
        }
        return backgroundimage;
    }

    public static int getBackgroundColor() {
        if (bg_type != BG_COLOR_ON) {
            return -1;
        }
        return bg_color;
    }

    public static void toggleVibration() {
        vibration = !vibration;
        saveSettings();
    }

    public static boolean vibration() {
        return vibration;
    }

    public static File locationForImage() {
        File image = null;
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String imageFileName = time + "_";
        image = disco.createTempFile(imageFileName, ".delving");

        setBackground(image.getAbsolutePath(), -1, true);
        return image;
    }

    public static void copyImage(Activity context, Intent intent) {
        Uri selectedImage = intent.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String photopath = cursor.getString(columnIndex);
        cursor.close();

        File forig = new File(photopath);
        File fcopy = locationForImage();
        disco.copyFile(forig, fcopy);
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
