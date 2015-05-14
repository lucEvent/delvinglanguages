package com.delvinglanguages.settings;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import com.delvinglanguages.data.ControlDisco;

public class Settings {

	private static final String DEBUG = "##Settings##";

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
	public static final int NUM_TYPES = 7;
	public static final int[] type_colors = { 0xFFFF9933, 0xFF33FF33, 0xFF3399FF, 0xFFFFFF33, 0xFFFF3333, 0xFF9933FF, 0xFFFF3399 };

	// Settings idioma materno
	public static String IdiomaNativo = ""; // (Se guarda) \\
	private static boolean doubleMode, vibration;

	public static final int[] COLORCODE = {
	/** RED , ORANGE, YELLOW, PINK, WHITE, GREEN, PURPLE, DARK BLUE, CYAN **/
	0xFFFF0000, 0xFFFF8000, 0xFFFFFF00, 0xFFFF00FF, 0xFFFFFFFF, 0xFF00FF00, 0xFF8000FF, 0xFF0000FF, 0xFF00FFFF };

	private static ControlDisco disco;

	/** ************************ CREADORA ************************ **/
	public Settings() {
		disco = new ControlDisco();
		readSettings();
	}

	/** ************************ ESTADO ************************ **/
	private static void readSettings() {
		String[] params = disco.readParams(settings, 6);

		if (params == null) {
			setDefault();
			return;
		}

		// 1. Se leen las variables de BACKGROUND
		bg_imagePath = params[0];
		bg_color = Integer.parseInt(params[1]);
		bg_type = Integer.parseInt(params[2]);

		// 2. Se lee el idioma materno
		IdiomaNativo = params[3];
		doubleMode = Boolean.parseBoolean(params[4]);
		vibration = Boolean.parseBoolean(params[5]);

	}

	private static void saveSettings() {
		String[] params = new String[6];

		// 1. Se guardan las variables de BACKGROUND
		params[0] = bg_imagePath;
		params[1] = Integer.toString(bg_color);
		params[2] = Integer.toString(bg_type);

		// 2. Se guarda las settings de la app
		params[3] = IdiomaNativo;
		params[4] = Boolean.toString(doubleMode);
		params[5] = Boolean.toString(vibration);

		disco.saveParams(settings, params);
	}

	private static void setDefault() {
		bg_imagePath = "";
		bg_color = 0;
		bg_type = BG_NO_BG;

		IdiomaNativo = "Native";
		doubleMode = true;
		vibration = true;
	}

	/** ***************** DIFERENTES CONFIGURACIONES ******************* **/

	public static void setIdiomaNativo(String nm) {
		IdiomaNativo = nm;
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

	public static void toggleDoubleMode() {
		doubleMode = !doubleMode;
		saveSettings();
	}

	public static boolean doubleMode() {
		return doubleMode;
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
		String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = time + "_";
		image = disco.createTempFile(imageFileName, ".delving");

		setBackground(image.getAbsolutePath(), -1, true);
		return image;
	}

	public static void copyImage(Activity context, Intent intent) {
		Uri selectedImage = intent.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String photopath = cursor.getString(columnIndex);
		cursor.close();

		File forig = new File(photopath);
		File fcopy = locationForImage();
		disco.copyFile(forig, fcopy);
	}

}
