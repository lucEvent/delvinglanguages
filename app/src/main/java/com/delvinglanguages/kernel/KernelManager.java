package com.delvinglanguages.kernel;

import android.content.Context;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.data.DatabaseManager;
import com.delvinglanguages.kernel.util.Languages;
import com.delvinglanguages.net.SyncManager;

public class KernelManager extends SyncManager {

    protected static DatabaseManager dbManager;

    protected static Languages languages;

    public KernelManager(Context context)
    {
        super(context);

        if (dbManager == null)
            dbManager = new DatabaseManager(context);

        if (languages == null)
            languages = dbManager.readLanguages();
    }

    public Languages getLanguages()
    {
        return languages;
    }

    public int getNumberOfLanguages()
    {
        return languages.size();
    }

    public Language getCurrentLanguage()
    {
        int lang_id = AppSettings.getCurrentLanguage();
        if (lang_id == -1) return null;
        else return languages.getLanguageById(lang_id);
    }

    protected synchronized void loadContentOf(Language language)
    {
        if (language.dictionary == null)
            language.setReferences(dbManager.readReferences(language.id));

        if (language.drawer_references == null)
            language.setDrawerReferences(dbManager.readDrawerReferences(language.id));

        if (language.tests == null)
            language.setTests(dbManager.readTests(language.id));

        if (language.themes == null)
            language.setThemes(dbManager.readThemes(language.id));
    }

    public Language createLanguage(int code, String name, int settings)
    {
        Language new_language = dbManager.insertLanguage(code, name, settings);
        languages.add(new_language);
        synchronizeNewLanguage(new_language.id);
        RecordManager.languageCreated(new_language.id, code);
        return new_language;
    }

    public void updateLanguageName(String new_name)
    {
        Language language = getCurrentLanguage();

        RecordManager.languageNameChanged(language.id, language.code, language.language_name, new_name);

        language.setName(new_name);

        dbManager.updateLanguage(language);
        synchronizeUpdatedLanguage(language.id);

    }

    public void updateLanguageCode(int code)
    {
        Language language = getCurrentLanguage();
        language.setCode(code);

        dbManager.updateLanguage(language);
        synchronizeUpdatedLanguage(language.id);
        RecordManager.languageCodeChanged(language.id, language.code);
    }

    public void updateLanguageSettings(boolean state, int mask)
    {
        Language language = getCurrentLanguage();
        language.setSetting(state, mask);

        if (mask == Language.MASK_PHRASAL_VERBS)
            RecordManager.languagePhVStateChanged(language.id, language.code, state);

        dbManager.updateLanguage(language);
        synchronizeUpdatedLanguage(language.id);
    }

    public void deleteLanguage()
    {
        Language language = getCurrentLanguage();
        dbManager.deleteLanguage(language);
        languages.remove(language);
        synchronizeDeleteLanguage(language.id);
        RecordManager.languageDeleted(language.id, language.code, language.language_name);
    }

    public void invalidateData()
    {
        languages = dbManager.readLanguages();
    }

}
