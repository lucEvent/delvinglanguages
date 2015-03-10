package com.delvinglanguages.core.theme;

import android.content.Context;
import android.util.Log;

import com.delvinglanguages.core.ControlCore;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.kernel.set.Themes;

public class ThemeKernelControl extends ControlCore {

	private static final String DEBUG = "##ThemeKernelControl##";

	public ThemeKernelControl(Context context) {
		super(context);
	}

	public Themes getThemes() {
		if (actualLang.getThemes() == null) {
			actualLang.setThemes(database.readThemes(actualLang.getID()));
		}
		return actualLang.getThemes();
	}

	public Theme getTheme(int theme_id) {
		Log.d(DEBUG, "Buscando theme " + theme_id);
		for (Theme theme : actualLang.getThemes()) {
			Log.d(DEBUG, "..Candidato " + theme.id);
			if (theme.id == theme_id) {
				return theme;
			}
		}
		return null;
	}

	public void addTheme(String th_name, ThemePairs th_pairs) {
		Theme theme = database.insertTheme(actualLang.getID(), th_name,
				th_pairs);
		actualLang.addTheme(theme);
	}

	public void modifyTheme(Theme theme) {
		database.updateTheme(theme);
	}

	public void removeTheme(Theme theme) {
		actualLang.getThemes().remove(theme);
		database.removeTheme(theme.id);
	}

}
