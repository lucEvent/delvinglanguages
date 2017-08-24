package com.delvinglanguages.data;

import android.content.Context;

import com.delvinglanguages.kernel.util.Statistics;

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

    public int insertDelvingList(int list_id, int from_code, int to_code, String name, int settings)
    {
        return super.insertDelvingList(list_id, from_code, to_code, name, settings, Database.NOT_SYNCED);
    }

    public void insertReference(int id, int list_id, String name, String inflexions, String pronunciation, int priority)
    {
        super.insertReference(id, list_id, name, inflexions, pronunciation, priority, Database.NOT_SYNCED);
    }

    public void insertDrawerReference(int id, int list_id, String note)
    {
        super.insertDrawerReference(id, list_id, note, Database.NOT_SYNCED);
    }

    public void insertSubject(int id, int list_id, String name, String referencesIds)
    {
        super.insertSubject(id, list_id, name, referencesIds, Database.NOT_SYNCED);
    }

    public void insertTest(int id, int list_id, String name, int runTimes, String content, int subject_id)
    {
        super.insertTest(id, list_id, name, runTimes, content, subject_id, Database.NOT_SYNCED);
    }

    // **************************************************** \\
    ////////////////////// Updates \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void updateStatistics(Statistics statistics)
    {
        super.updateStatistics(statistics.id, statistics, Database.NOT_SYNCED);
    }

}