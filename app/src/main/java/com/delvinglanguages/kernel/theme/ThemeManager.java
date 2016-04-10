package com.delvinglanguages.kernel.theme;

import android.content.Context;

import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.util.ThemePairs;
import com.delvinglanguages.kernel.util.Themes;

public class ThemeManager extends KernelManager {

    public ThemeManager(Context context) {
        super(context);
    }

    public Themes getThemes() {
        Language language = getCurrentLanguage();
        if (language.themes == null) {
            language.setThemes(dbManager.readThemes(language.id));
        }
        return language.themes;
    }

    public Theme addTheme(String theme_name, ThemePairs theme_pairs) {
        Language language = getCurrentLanguage();

        Theme theme = dbManager.insertTheme(language.id, theme_name, theme_pairs);
        language.addTheme(theme);

        return theme;
    }

    public void updateTheme(Theme theme, String new_name, ThemePairs new_pairs) {
        theme.setName(new_name);
        theme.setPairs(new_pairs);
        dbManager.updateTheme(theme);
    }

    public void deleteTheme(Theme theme) {
        getCurrentLanguage().themes.remove(theme);
        dbManager.deleteTheme(theme.id);
    }

}
