package com.delvinglanguages.kernel.theme;

import android.content.Context;
import android.util.Log;

import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.kernel.set.Themes;

public class ThemeKernelControl extends KernelControl {

	private static final String DEBUG = "##ThemeKernelControl##";

	public ThemeKernelControl(Context context) {
		super(context);
	}

	public Themes getThemes() {
		if (currentLanguage.getThemes() == null) {
			currentLanguage.setThemes(database.readThemes(currentLanguage.getID()));
		}
		return currentLanguage.getThemes();
	}

	public Theme getTheme(int theme_id) {
		Log.d(DEBUG, "Buscando theme " + theme_id);
		for (Theme theme : currentLanguage.getThemes()) {
			Log.d(DEBUG, "..Candidato " + theme.id);
			if (theme.id == theme_id) {
				return theme;
			}
		}
		return null;
	}

	public void addTheme(String th_name, ThemePairs th_pairs) {
		Theme theme = database.insertTheme(currentLanguage.getID(), th_name, th_pairs);
		currentLanguage.addTheme(theme);
	}

	public void modifyTheme(Theme theme) {
		database.updateTheme(theme);
	}

	public void removeTheme(Theme theme) {
		currentLanguage.getThemes().remove(theme);
		database.deleteTheme(theme.id);
	}

}
