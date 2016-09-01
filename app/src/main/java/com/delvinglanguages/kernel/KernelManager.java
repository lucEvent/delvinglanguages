package com.delvinglanguages.kernel;

import android.content.Context;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.data.DatabaseManager;
import com.delvinglanguages.kernel.util.Languages;
import com.delvinglanguages.net.SyncManager;

public class KernelManager extends SyncManager {

    protected static DatabaseManager dbManager;
    //   protected static StorageManager storageManager;
    protected static Languages languages;

    public KernelManager(Context context)
    {
        super(context);

        if (dbManager == null)
            dbManager = new DatabaseManager(context);

        if (languages == null)
            languages = dbManager.readLanguages();

        //   if (storageManager == null)
        //       storageManager = new StorageManager();
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

        if (language.removed_references == null)
            language.setRemovedReferences(dbManager.readRemovedReferences(language.id));

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
        return new_language;
    }

    public void updateLanguage(int code, String language_name)
    {
        Language language = getCurrentLanguage();
        language.setCode(code);
        language.setName(language_name);

        dbManager.updateLanguage(language);
        synchronizeUpdatedLanguage(language.id);
    }

    public void updateLanguageSettings(boolean setting, int mask)
    {
        Language language = getCurrentLanguage();
        language.setSetting(setting, mask);

        dbManager.updateLanguage(language);
        synchronizeUpdatedLanguage(language.id);
    }

    public void deleteLanguage()
    {
        Language language = getCurrentLanguage();
        dbManager.deleteLanguage(language);
        languages.remove(language);
        synchronizeDeleteLanguage(language.id);
    }

    public void invalidateData()
    {
        languages = dbManager.readLanguages();
    }

}
