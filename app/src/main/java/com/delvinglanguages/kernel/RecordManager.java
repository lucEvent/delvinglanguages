package com.delvinglanguages.kernel;

import android.content.Context;

import com.delvinglanguages.data.RecordDatabaseManager;
import com.delvinglanguages.kernel.record.LanguageRecord;
import com.delvinglanguages.kernel.record.Record;
import com.delvinglanguages.kernel.record.Records;
import com.delvinglanguages.kernel.util.Wrapper;

public class RecordManager {

    private static final long MAX_TIME_DIFFERENCE = 1000 * 60 * 5;   // 5 minutes

    private static LanguageRecord lastLanguageRecord;
    private static RecordDatabaseManager db;

    public static void init(Context context)
    {
        db = new RecordDatabaseManager(context);
        lastLanguageRecord = new LanguageRecord(-1, -1, -1, null, -1);
    }

    public static Records getRecords()
    {
        return db.readRecords();
    }

    /*
     * APP SETTINGS RECORDS
     */
    public static void appLanguageChanged(int language_code)
    {
        db.insertAppSettingsRecord(Record.APPSET_LANGUAGE_CHANGED, language_code);
    }

    public static void appKBVibrationStateChanged(boolean new_state)
    {
        db.insertAppSettingsRecord(Record.APPSET_KBVIBRATION_STATE_CHANGED, new_state);
    }

    public static void appThemeChanged(int new_theme)
    {
        db.insertAppSettingsRecord(Record.APPSET_THEME_CHANGED, new_theme);
    }

    public static void appOnlineBackUpStateChanged(boolean new_state)
    {
        db.insertAppSettingsRecord(Record.APPSET_ONLINE_BACKUP_STATE_CHANGED, new_state);
    }

    public static void appImport(int num_languages_imported)
    {
        db.insertAppSettingsRecord(Record.APPSET_IMPORT, num_languages_imported);
    }

    public static void appExport(int num_languages_exported)
    {
        db.insertAppSettingsRecord(Record.APPSET_EXPORT, num_languages_exported);
    }

    /*
     * LANGUAGE SETTINGS RECORDS
     */
    public static void languageCreated(int list_id, int language_code)
    {
        db.insertLanguageSettingsRecord(Record.LANGUAGE_CREATED, list_id, language_code, "", "");
    }

    public static void languageDeleted(int list_id, int language_code, String list_name)
    {
        db.insertLanguageSettingsRecord(Record.LANGUAGE_REMOVED, list_id, language_code, "", list_name);
    }

    public static void languageIntegrated(int list_id, int language_code, String from_list_name, String in_list_name)
    {
        db.insertLanguageSettingsRecord(Record.LANGUAGE_INTEGRATED, list_id, language_code, from_list_name, in_list_name);
    }

    public static void languageCodeChanged(int list_id, int language_code)
    {
        db.insertLanguageSettingsRecord(Record.LANGUAGE_CODE_CHANGED, list_id, language_code, language_code, language_code);
    }

    public static void languageNameChanged(int list_id, int language_code, String old_list_name, String new_list_name)
    {
        db.insertLanguageSettingsRecord(Record.LANGUAGE_NAME_CHANGED, list_id, language_code, old_list_name, new_list_name);
    }

    public static void languagePhVStateChanged(int list_id, int language_code, boolean new_state)
    {
        db.insertLanguageSettingsRecord(Record.LANGUAGE_PHRASAL_STATE_CHANGED, list_id, language_code, !new_state, new_state);
    }

    public static void languageStatisticsCleared(int list_id, int language_code)
    {
        db.insertLanguageSettingsRecord(Record.LANGUAGE_STATISTICS_CLEARED, list_id, language_code, "", "");
    }

    public static void languageRecycleBinCleared(int list_id, int language_code)
    {
        db.insertLanguageSettingsRecord(Record.RECYCLE_BIN_CLEARED, list_id, language_code, "", "");
    }

    /*
     * Language Records
     */

    public static void drawerWordAdded(int list_id, int language_code, int word_id)
    {
        if (canJoinRecords(lastLanguageRecord, Record.DRAWERWORD_ADDED, list_id)) {
            lastLanguageRecord.addItem(word_id);
            db.updateLanguageRecord(lastLanguageRecord);
        } else
            lastLanguageRecord = db.insertLanguageRecord(Record.DRAWERWORD_ADDED, list_id, language_code, new int[]{word_id});
    }

    public static void drawerWordDeleted(int list_id, int language_code, int word_id)
    {
        if (canJoinRecords(lastLanguageRecord, Record.DRAWERWORD_DELETED, list_id)) {
            lastLanguageRecord.addItem(word_id);
            db.updateLanguageRecord(lastLanguageRecord);
        } else
            lastLanguageRecord = db.insertLanguageRecord(Record.DRAWERWORD_DELETED, list_id, language_code, new int[]{word_id});
    }

    public static void referenceAdded(int list_id, int language_code, int reference_id)
    {
        if (canJoinRecords(lastLanguageRecord, Record.REFERENCE_CREATED, list_id)) {
            lastLanguageRecord.addItem(reference_id);
            db.updateLanguageRecord(lastLanguageRecord);
        } else
            lastLanguageRecord = db.insertLanguageRecord(Record.REFERENCE_CREATED, list_id, language_code, new int[]{reference_id});
    }

    public static void referenceModified(int list_id, int language_code, int reference_id)
    {
        if (canJoinRecords(lastLanguageRecord, Record.REFERENCE_MODIFIED, list_id)) {
            lastLanguageRecord.addItem(reference_id);
            db.updateLanguageRecord(lastLanguageRecord);
        } else
            lastLanguageRecord = db.insertLanguageRecord(Record.REFERENCE_MODIFIED, list_id, language_code, new int[]{reference_id});
    }

    public static void referenceRemoved(int list_id, int language_code, int reference_id)
    {
        if (canJoinRecords(lastLanguageRecord, Record.REFERENCE_REMOVED, list_id)) {
            lastLanguageRecord.addItem(reference_id);
            db.updateLanguageRecord(lastLanguageRecord);
        } else
            lastLanguageRecord = db.insertLanguageRecord(Record.REFERENCE_REMOVED, list_id, language_code, new int[]{reference_id});
    }

    public static void themeAdded(int list_id, int language_code, int theme_id)
    {
        if (canJoinRecords(lastLanguageRecord, Record.THEME_CREATED, list_id)) {
            lastLanguageRecord.addItem(theme_id);
            db.updateLanguageRecord(lastLanguageRecord);
        } else
            lastLanguageRecord = db.insertLanguageRecord(Record.THEME_CREATED, list_id, language_code, new int[]{theme_id});
    }

    public static void themeModified(int list_id, int language_code, int theme_id)
    {
        if (canJoinRecords(lastLanguageRecord, Record.THEME_MODIFIED, list_id)) {
            lastLanguageRecord.addItem(theme_id);
            db.updateLanguageRecord(lastLanguageRecord);
        } else
            lastLanguageRecord = db.insertLanguageRecord(Record.THEME_MODIFIED, list_id, language_code, new int[]{theme_id});
    }

    public static void themeRemoved(int list_id, int language_code, int theme_id)
    {
        if (canJoinRecords(lastLanguageRecord, Record.THEME_REMOVED, list_id)) {
            lastLanguageRecord.addItem(theme_id);
            db.updateLanguageRecord(lastLanguageRecord);
        } else
            lastLanguageRecord = db.insertLanguageRecord(Record.THEME_REMOVED, list_id, language_code, new int[]{theme_id});
    }

    public static void testAdded(int list_id, int language_code, int test_id)
    {
        if (canJoinRecords(lastLanguageRecord, Record.TEST_CREATED, list_id)) {
            lastLanguageRecord.addItem(test_id);
            db.updateLanguageRecord(lastLanguageRecord);
        } else
            lastLanguageRecord = db.insertLanguageRecord(Record.TEST_CREATED, list_id, language_code, new int[]{test_id});
    }

    public static void testDone(int list_id, int language_code, int test_id)
    {
        if (canJoinRecords(lastLanguageRecord, Record.TEST_DONE, list_id)) {
            lastLanguageRecord.addItem(test_id);
            db.updateLanguageRecord(lastLanguageRecord);
        } else
            lastLanguageRecord = db.insertLanguageRecord(Record.TEST_DONE, list_id, language_code, new int[]{test_id});
    }

    public static void testRemoved(int list_id, int language_code, int test_id)
    {
        if (canJoinRecords(lastLanguageRecord, Record.TEST_REMOVED, list_id)) {
            lastLanguageRecord.addItem(test_id);
            db.updateLanguageRecord(lastLanguageRecord);
        } else
            lastLanguageRecord = db.insertLanguageRecord(Record.TEST_REMOVED, list_id, language_code, new int[]{test_id});
    }

    public static void itemRecovered(int list_id, int language_code, int item_type, int item_id)
    {
        switch (item_type) {
            case Wrapper.TYPE_REFERENCE:
                if (canJoinRecords(lastLanguageRecord, Record.REFERENCE_RECOVERED, list_id)) {
                    lastLanguageRecord.addItem(item_id);
                    db.updateLanguageRecord(lastLanguageRecord);
                } else
                    lastLanguageRecord = db.insertLanguageRecord(Record.REFERENCE_RECOVERED, list_id, language_code, new int[]{item_id});
                break;

            case Wrapper.TYPE_THEME:
                if (canJoinRecords(lastLanguageRecord, Record.THEME_RECOVERED, list_id)) {
                    lastLanguageRecord.addItem(item_id);
                    db.updateLanguageRecord(lastLanguageRecord);
                } else
                    lastLanguageRecord = db.insertLanguageRecord(Record.THEME_RECOVERED, list_id, language_code, new int[]{item_id});
                break;

            case Wrapper.TYPE_TEST:
                if (canJoinRecords(lastLanguageRecord, Record.TEST_RECOVERED, list_id)) {
                    lastLanguageRecord.addItem(item_id);
                    db.updateLanguageRecord(lastLanguageRecord);
                } else
                    lastLanguageRecord = db.insertLanguageRecord(Record.TEST_RECOVERED, list_id, language_code, new int[]{item_id});
                break;
        }
    }

    public static void referencePractised(int record_type, int list_id, int language_code, int reference_id)
    {
        if (canJoinRecords(lastLanguageRecord, record_type, list_id)) {
            lastLanguageRecord.addItem(reference_id);
            db.updateLanguageRecord(lastLanguageRecord);
        } else
            lastLanguageRecord = db.insertLanguageRecord(record_type, list_id, language_code, new int[]{reference_id});
    }

    private static boolean canJoinRecords(LanguageRecord record, int type, int list_id)
    {
        return type == record.type && list_id == record.language_id && record.time + MAX_TIME_DIFFERENCE >= System.currentTimeMillis();
    }

    public static void clearRecords()
    {
        db.clearDatabase();
    }

}
