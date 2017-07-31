package com.delvinglanguages.kernel;

import android.content.Context;

import com.delvinglanguages.data.RecordDatabaseManager;
import com.delvinglanguages.kernel.record.DelvingListRecord;
import com.delvinglanguages.kernel.record.Record;
import com.delvinglanguages.kernel.record.Records;
import com.delvinglanguages.kernel.util.Wrapper;

public class RecordManager {

    private static final long MAX_TIME_DIFFERENCE = 1000 * 60 * 5;   // 5 minutes

    private static DelvingListRecord lastDelvingListRecord;
    private static RecordDatabaseManager db;

    public static void init(Context context)
    {
        db = new RecordDatabaseManager(context);
        lastDelvingListRecord = new DelvingListRecord(-1, -1, -1, null, -1);
    }

    public static Records getRecords()
    {
        return db.readRecords();
    }

    /*
     * APP SETTINGS RECORDS
     */
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

    public static void appImport(int num_lists_imported)
    {
        db.insertAppSettingsRecord(Record.APPSET_IMPORT, num_lists_imported);
    }

    public static void appExport(int num_lists_exported)
    {
        db.insertAppSettingsRecord(Record.APPSET_EXPORT, num_lists_exported);
    }

    /*
     * LIST SETTINGS RECORDS
     */
    public static void listCreated(int list_id, int language_code)
    {
        db.insertDelvingListSettingsRecord(Record.LIST_CREATED, list_id, language_code, "", "");
    }

    public static void listDeleted(int list_id, int language_code, String list_name)
    {
        db.insertDelvingListSettingsRecord(Record.LIST_REMOVED, list_id, language_code, "", list_name);
    }

    public static void listIntegrated(int list_id, int language_code, String from_list_name, String in_list_name)
    {
        db.insertDelvingListSettingsRecord(Record.LIST_INTEGRATED, list_id, language_code, from_list_name, in_list_name);
    }

    public static void listLanguageCodesChanged(int list_id, int from_code, int to_code)
    {
        db.insertDelvingListSettingsRecord(Record.LIST_CODES_CHANGED, list_id, from_code, from_code, to_code);
    }

    public static void listNameChanged(int list_id, int language_code, String old_list_name, String new_list_name)
    {
        db.insertDelvingListSettingsRecord(Record.LIST_NAME_CHANGED, list_id, language_code, old_list_name, new_list_name);
    }

    public static void listPhVStateChanged(int list_id, int language_code, boolean new_state)
    {
        db.insertDelvingListSettingsRecord(Record.LIST_PHRASAL_STATE_CHANGED, list_id, language_code, !new_state, new_state);
    }

    public static void listStatisticsCleared(int list_id, int language_code)
    {
        db.insertDelvingListSettingsRecord(Record.LIST_STATISTICS_CLEARED, list_id, language_code, "", "");
    }

    public static void listRecycleBinCleared(int list_id, int language_code)
    {
        db.insertDelvingListSettingsRecord(Record.RECYCLE_BIN_CLEARED, list_id, language_code, "", "");
    }

    /*
     * DelvingList Records
     */

    public static void drawerWordAdded(int list_id, int language_code, int word_id)
    {
        if (canJoinRecords(lastDelvingListRecord, Record.DRAWERWORD_ADDED, list_id)) {
            lastDelvingListRecord.addItem(word_id);
            db.updateDelvingListRecord(lastDelvingListRecord);
        } else
            lastDelvingListRecord = db.insertDelvingListRecord(Record.DRAWERWORD_ADDED, list_id, language_code, new int[]{word_id});
    }

    public static void drawerWordDeleted(int list_id, int language_code, int word_id)
    {
        if (canJoinRecords(lastDelvingListRecord, Record.DRAWERWORD_DELETED, list_id)) {
            lastDelvingListRecord.addItem(word_id);
            db.updateDelvingListRecord(lastDelvingListRecord);
        } else
            lastDelvingListRecord = db.insertDelvingListRecord(Record.DRAWERWORD_DELETED, list_id, language_code, new int[]{word_id});
    }

    public static void referenceAdded(int list_id, int language_code, int reference_id)
    {
        if (canJoinRecords(lastDelvingListRecord, Record.REFERENCE_CREATED, list_id)) {
            lastDelvingListRecord.addItem(reference_id);
            db.updateDelvingListRecord(lastDelvingListRecord);
        } else
            lastDelvingListRecord = db.insertDelvingListRecord(Record.REFERENCE_CREATED, list_id, language_code, new int[]{reference_id});
    }

    public static void referenceModified(int list_id, int language_code, int reference_id)
    {
        if (canJoinRecords(lastDelvingListRecord, Record.REFERENCE_MODIFIED, list_id)) {
            lastDelvingListRecord.addItem(reference_id);
            db.updateDelvingListRecord(lastDelvingListRecord);
        } else
            lastDelvingListRecord = db.insertDelvingListRecord(Record.REFERENCE_MODIFIED, list_id, language_code, new int[]{reference_id});
    }

    public static void referenceRemoved(int list_id, int language_code, int reference_id)
    {
        if (canJoinRecords(lastDelvingListRecord, Record.REFERENCE_REMOVED, list_id)) {
            lastDelvingListRecord.addItem(reference_id);
            db.updateDelvingListRecord(lastDelvingListRecord);
        } else
            lastDelvingListRecord = db.insertDelvingListRecord(Record.REFERENCE_REMOVED, list_id, language_code, new int[]{reference_id});
    }

    public static void subjectAdded(int list_id, int language_code, int subject_id)
    {
        if (canJoinRecords(lastDelvingListRecord, Record.SUBJECT_CREATED, list_id)) {
            lastDelvingListRecord.addItem(subject_id);
            db.updateDelvingListRecord(lastDelvingListRecord);
        } else
            lastDelvingListRecord = db.insertDelvingListRecord(Record.SUBJECT_CREATED, list_id, language_code, new int[]{subject_id});
    }

    public static void subjectModified(int list_id, int language_code, int subject_id)
    {
        if (canJoinRecords(lastDelvingListRecord, Record.SUBJECT_MODIFIED, list_id)) {
            lastDelvingListRecord.addItem(subject_id);
            db.updateDelvingListRecord(lastDelvingListRecord);
        } else
            lastDelvingListRecord = db.insertDelvingListRecord(Record.SUBJECT_MODIFIED, list_id, language_code, new int[]{subject_id});
    }

    public static void subjectRemoved(int list_id, int language_code, int subject_id)
    {
        if (canJoinRecords(lastDelvingListRecord, Record.SUBJECT_REMOVED, list_id)) {
            lastDelvingListRecord.addItem(subject_id);
            db.updateDelvingListRecord(lastDelvingListRecord);
        } else
            lastDelvingListRecord = db.insertDelvingListRecord(Record.SUBJECT_REMOVED, list_id, language_code, new int[]{subject_id});
    }

    public static void testAdded(int list_id, int language_code, int test_id)
    {
        if (canJoinRecords(lastDelvingListRecord, Record.TEST_CREATED, list_id)) {
            lastDelvingListRecord.addItem(test_id);
            db.updateDelvingListRecord(lastDelvingListRecord);
        } else
            lastDelvingListRecord = db.insertDelvingListRecord(Record.TEST_CREATED, list_id, language_code, new int[]{test_id});
    }

    public static void testDone(int list_id, int language_code, int test_id)
    {
        if (canJoinRecords(lastDelvingListRecord, Record.TEST_DONE, list_id)) {
            lastDelvingListRecord.addItem(test_id);
            db.updateDelvingListRecord(lastDelvingListRecord);
        } else
            lastDelvingListRecord = db.insertDelvingListRecord(Record.TEST_DONE, list_id, language_code, new int[]{test_id});
    }

    public static void testRemoved(int list_id, int language_code, int test_id)
    {
        if (canJoinRecords(lastDelvingListRecord, Record.TEST_REMOVED, list_id)) {
            lastDelvingListRecord.addItem(test_id);
            db.updateDelvingListRecord(lastDelvingListRecord);
        } else
            lastDelvingListRecord = db.insertDelvingListRecord(Record.TEST_REMOVED, list_id, language_code, new int[]{test_id});
    }

    public static void itemRecovered(int list_id, int language_code, int item_type, int item_id)
    {
        switch (item_type) {
            case Wrapper.TYPE_REFERENCE:
                if (canJoinRecords(lastDelvingListRecord, Record.REFERENCE_RECOVERED, list_id)) {
                    lastDelvingListRecord.addItem(item_id);
                    db.updateDelvingListRecord(lastDelvingListRecord);
                } else
                    lastDelvingListRecord = db.insertDelvingListRecord(Record.REFERENCE_RECOVERED, list_id, language_code, new int[]{item_id});
                break;

            case Wrapper.TYPE_SUBJECT:
                if (canJoinRecords(lastDelvingListRecord, Record.SUBJECT_RECOVERED, list_id)) {
                    lastDelvingListRecord.addItem(item_id);
                    db.updateDelvingListRecord(lastDelvingListRecord);
                } else
                    lastDelvingListRecord = db.insertDelvingListRecord(Record.SUBJECT_RECOVERED, list_id, language_code, new int[]{item_id});
                break;

            case Wrapper.TYPE_TEST:
                if (canJoinRecords(lastDelvingListRecord, Record.TEST_RECOVERED, list_id)) {
                    lastDelvingListRecord.addItem(item_id);
                    db.updateDelvingListRecord(lastDelvingListRecord);
                } else
                    lastDelvingListRecord = db.insertDelvingListRecord(Record.TEST_RECOVERED, list_id, language_code, new int[]{item_id});
                break;
        }
    }

    public static void referencePractised(int record_type, int list_id, int language_code, int reference_id)
    {
        if (canJoinRecords(lastDelvingListRecord, record_type, list_id)) {
            lastDelvingListRecord.addItem(reference_id);
            db.updateDelvingListRecord(lastDelvingListRecord);
        } else
            lastDelvingListRecord = db.insertDelvingListRecord(record_type, list_id, language_code, new int[]{reference_id});
    }

    private static boolean canJoinRecords(DelvingListRecord record, int type, int list_id)
    {
        return type == record.type && list_id == record.list_id && record.time + MAX_TIME_DIFFERENCE >= System.currentTimeMillis();
    }

    public static void clearRecords()
    {
        db.clearDatabase();
    }

}
