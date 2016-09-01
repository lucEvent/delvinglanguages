package com.delvinglanguages.data;

import android.content.Context;

import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.ThemePairs;

/**
 * FastDatabaseManager
 * <p/>
 * Insert/Update operations don't return the Object
 * <p/>
 * Steps for a good use:
 * 1. call openReadableDatabase/openWritableDatabase
 * 2. call as many functions read/insert/update as needed
 * 3. call closeReadableDatabase/closeWritableDatabase
 */
public class BackUpDatabaseManager extends BaseDatabaseManager {

    public BackUpDatabaseManager(Context context)
    {
        super(context);
    }

    // **************************************************** \\
    ////////////////////// Inserts \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public int insertLanguage(int lang_id, int code, String name, int settings)
    {
        return super.insertLanguage(lang_id, code, name, settings, Database.NOT_SYNCED);
    }

    public void insertReference(int id, int lang_id, String name, String inflexions, String pronunciation, int priority)
    {
        super.insertReference(id, lang_id, name, inflexions, pronunciation, priority, Database.NOT_SYNCED);
    }

    public void insertDrawerReference(int id, int lang_id, String note)
    {
        super.insertDrawerReference(id, lang_id, note, Database.NOT_SYNCED);
    }

    public void insertTheme(int id, int lang_id, String name, ThemePairs pairs)
    {
        super.insertTheme(id, lang_id, name, pairs, Database.NOT_SYNCED);
    }

    public void insertTest(int id, int lang_id, String name, int runTimes, String content, int theme_id)
    {
        super.insertTest(id, lang_id, name, runTimes, content, theme_id, Database.NOT_SYNCED);
    }

    // **************************************************** \\
    ////////////////////// Updates \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void updateStatistics(Statistics statistics)
    {
        super.updateStatistics(statistics.id, statistics, Database.NOT_SYNCED);
    }

}