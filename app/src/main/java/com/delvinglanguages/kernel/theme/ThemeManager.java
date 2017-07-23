package com.delvinglanguages.kernel.theme;

import android.content.Context;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.Inflexion;
import com.delvinglanguages.kernel.KernelManager;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.RecordManager;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.kernel.util.ThemePairs;
import com.delvinglanguages.kernel.util.Themes;
import com.delvinglanguages.kernel.util.Wrapper;

public class ThemeManager extends KernelManager {

    public ThemeManager(Context context)
    {
        super(context);
    }

    public Themes getThemes()
    {
        Language language = getCurrentLanguage();
        if (language.themes == null)
            language.setThemes(dbManager.readThemes(language.id));

        return language.themes;
    }

    public Theme addTheme(String theme_name, ThemePairs theme_pairs)
    {
        Language language = getCurrentLanguage();

        Theme theme = dbManager.insertTheme(language.id, theme_name, theme_pairs);
        language.addTheme(theme);

        RecordManager.themeAdded(language.id, language.code, theme.id);
        synchronizeNewItem(language.id, theme.id, theme);
        return theme;
    }

    public void updateTheme(Theme theme, String new_name, ThemePairs new_pairs)
    {
        theme.setName(new_name);
        theme.setPairs(new_pairs);

        Language language = getCurrentLanguage();
        dbManager.updateTheme(theme, language.id);

        RecordManager.themeModified(language.id, language.code, theme.id);
        synchronizeUpdateItem(language.id, theme.id, theme);
    }

    public void removeTheme(Theme theme)
    {
        Language language = getCurrentLanguage();
        language.removeTheme(theme);
        dbManager.removeTheme(language.id, theme);

        RecordManager.themeRemoved(language.id, language.code, theme.id);
        synchronizeDeleteItem(language.id, theme.id, Wrapper.TYPE_THEME);
    }

    public Test toTest(Context context, Theme theme)
    {
        Language language = getCurrentLanguage();
        Test test = dbManager.readTestFromTheme(theme.id);

        if (test == null) {

            String test_name = theme.getName() + " " + context.getString(R.string.test);

            DReferences references = new DReferences(theme.getPairs().size());
            for (ThemePair pair : theme.getPairs()) {
                Inflexions inflexions = new Inflexions(1);
                inflexions.add(new Inflexion(new String[]{}, new String[]{pair.inNative}, DReference.OTHER));
                references.add(new DReference(-1, pair.inDelved, "", inflexions, 0));
            }

            test = dbManager.insertTest(language.id, test_name, references, theme.id);

            RecordManager.testAdded(language.id, language.code, test.id);
            synchronizeNewItem(language.id, test.id, test);
        }
        Test replaceable = language.tests.getTestById(test.id);
        if (replaceable != null) test = replaceable;
        else language.tests.add(test);
        return test;
    }

}
