package com.delvinglanguages.kernel.theme;

import com.delvinglanguages.kernel.KernelControl;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.kernel.set.Themes;
import com.delvinglanguages.settings.Settings;

public class ThemeKernelControl extends KernelControl {

    public ThemeKernelControl() {
        super(KernelControl.context);
    }

    public Themes getThemes() {
        if (currentLanguage.themes == null) {
            currentLanguage.setThemes(dbManager.readThemes(currentLanguage.id));
        }
        return currentLanguage.themes;
    }

    public Theme getTheme(int theme_id) {
        debug("Buscando theme " + theme_id);
        for (Theme theme : currentLanguage.themes) {
            debug("..Candidato " + theme.id);
            if (theme.id == theme_id) {
                return theme;
            }
        }
        return null;
    }

    public void addTheme(String th_name, ThemePairs th_pairs) {
        Theme theme = dbManager.insertTheme(currentLanguage.id, th_name, th_pairs);
        currentLanguage.addTheme(theme);
    }

    public void modifyTheme(Theme theme) {
        dbManager.updateTheme(theme);
    }

    public void removeTheme(Theme theme) {
        currentLanguage.themes.remove(theme);
        dbManager.deleteTheme(theme.id);
    }

    private static void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##ThemeKernelControl##", text);
    }

}
