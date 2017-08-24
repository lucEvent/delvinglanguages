package com.delvinglanguages.data;

import android.content.Context;
import android.database.Cursor;

import com.delvinglanguages.data.Database.DBDeletedItem;
import com.delvinglanguages.data.Database.DBDelvingList;
import com.delvinglanguages.data.Database.DBDrawerReference;
import com.delvinglanguages.data.Database.DBReference;
import com.delvinglanguages.data.Database.DBRemovedItem;
import com.delvinglanguages.data.Database.DBStatistics;
import com.delvinglanguages.data.Database.DBSubject;
import com.delvinglanguages.data.Database.DBTest;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.subject.Subject;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.util.DelvingLists;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.net.utils.SyncWrapper;
import com.delvinglanguages.net.utils.SyncWrappers;

/**
 * Steps for a good use:
 * 1. call openReadableDatabase/openWritableDatabase
 * 2. call as many functions read/insert as needed
 * 3. call closeReadableDatabase/closeWritableDatabase
 */
public class SyncDatabaseManager extends BaseDatabaseManager {

    protected static final String SELECTION_NOT_SYNCED = Database.synced + " = " + Database.NOT_SYNCED;

    public SyncDatabaseManager(Context context)
    {
        super(context);
    }

    @Override
    public DelvingLists readLists()
    {
        DelvingLists result = new DelvingLists();

        Cursor cursor = db.query(DBDelvingList.db, DBDelvingList.cols, SELECTION_NOT_SYNCED, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                result.add(DBDelvingList.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();

        return result;
    }

    public SyncWrappers readStatistics()
    {
        SyncWrappers result = new SyncWrappers();

        Cursor cursor = db.query(DBStatistics.db, DBStatistics.cols, SELECTION_NOT_SYNCED, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                Statistics statistics = DBStatistics.parse(cursor);
                result.add(new SyncWrapper(statistics.id, statistics.id, statistics.wrapType(), statistics.wrap()));

            } while (cursor.moveToNext());

        cursor.close();

        return result;
    }

    public SyncWrappers readReferences()
    {
        SyncWrappers result = new SyncWrappers();

        Cursor cursor = db.query(DBReference.db, DBReference.cols, SELECTION_NOT_SYNCED, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                DReference reference = DBReference.parse(cursor);
                result.add(new SyncWrapper(reference.id, cursor.getInt(1), reference.wrapType(), reference.wrap()));

            } while (cursor.moveToNext());

        cursor.close();

        return result;
    }

    public SyncWrappers readDrawerReferences()
    {
        SyncWrappers result = new SyncWrappers();

        Cursor cursor = db.query(DBDrawerReference.db, DBDrawerReference.cols, SELECTION_NOT_SYNCED, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                DrawerReference dreference = DBDrawerReference.parse(cursor);
                result.add(new SyncWrapper(dreference.id, cursor.getInt(1), dreference.wrapType(), dreference.wrap()));

            } while (cursor.moveToNext());

        cursor.close();

        return result;
    }

    public SyncWrappers readSubjects()
    {
        SyncWrappers result = new SyncWrappers();

        Cursor cursor = db.query(DBSubject.db, DBSubject.cols, SELECTION_NOT_SYNCED, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                Subject subject = DBSubject.parse(cursor);
                result.add(new SyncWrapper(subject.id, cursor.getInt(1), subject.wrapType(), subject.wrap()));

            } while (cursor.moveToNext());

        cursor.close();

        return result;
    }

    public SyncWrappers readTests()
    {
        SyncWrappers result = new SyncWrappers();

        Cursor cursor = db.query(DBTest.db, DBTest.cols, SELECTION_NOT_SYNCED, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                Test test = DBTest.parse(cursor);
                result.add(new SyncWrapper(test.id, cursor.getInt(1), test.wrapType(), test.wrap()));

            } while (cursor.moveToNext());

        cursor.close();

        return result;
    }

    public SyncWrappers readDeletes()
    {
        SyncWrappers result = new SyncWrappers();

        Cursor cursor = db.query(DBDeletedItem.db, DBDeletedItem.cols, null, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                result.add(DBDeletedItem.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();

        return result;
    }


    // **************************************************** \\
    ////////////////////// Inserts \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void insertLanguage(int list_id, int from_code, int to_code, String name, int settings)
    {
        super.insertDelvingList(list_id, from_code, to_code, name, settings, Database.SYNCED);
    }

    public void insertReference(int id, int list_id, String name, String inflexions, String pronunciation, int priority)
    {
        super.insertReference(id, list_id, name, inflexions, pronunciation, priority, Database.SYNCED);
    }

    public void insertDrawerReference(int id, int list_id, String note)
    {
        super.insertDrawerReference(id, list_id, note, Database.SYNCED);
    }

    public void insertSubject(int id, int list_id, String name, String referencesIds)
    {
        super.insertSubject(id, list_id, name, referencesIds, Database.SYNCED);
    }

    public void insertTest(int id, int list_id, String name, int runTimes, String content, int subject_id)
    {
        super.insertTest(id, list_id, name, runTimes, content, subject_id, Database.SYNCED);
    }

    public void insertSyncItem(int item_id, int list_id, int type)
    {
        values.put(Database.id, item_id);
        values.put(Database.list_id, list_id);
        values.put(Database.type, type);
        db.insert(DBDeletedItem.db, null, values);
        values.clear();
    }

    // **************************************************** \\
    ////////////////////// Updates \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void updateLanguage(int id, int from_code, int to_code, String name, int settings)
    {
        super.updateDelvingList(id, from_code, to_code, name, settings, Database.SYNCED);
    }

    public void updateStatistics(Statistics statistics)
    {
        super.updateStatistics(statistics.id, statistics, Database.SYNCED);
    }

    public void updateReference(DReference reference, int list_id)
    {
        super.updateReference(reference.id, list_id, reference.name, reference.getInflexions().wrap(),
                reference.pronunciation, reference.priority, Database.SYNCED);
    }

    public void updateSubject(Subject subject, int list_id)
    {
        super.updateSubject(subject.id, list_id, subject.getName(), subject.wrapReferencesIds(), Database.SYNCED);
    }

    public void updateTest(Test test, int list_id)
    {
        super.updateTest(test.id, list_id, test.name, test.getRunTimes(), Test.wrapContent(test), Database.SYNCED);
    }

    // **************************************************** \\
    ////////////////////// Syncs \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void syncLanguages()
    {
        values.put(Database.synced, Database.SYNCED);
        db.update(DBDelvingList.db, values, SELECTION_NOT_SYNCED, null);
        values.clear();
    }

    public void syncStatistics()
    {
        values.put(Database.synced, Database.SYNCED);
        db.update(DBStatistics.db, values, SELECTION_NOT_SYNCED, null);
        values.clear();
    }

    public void syncReferences()
    {
        values.put(Database.synced, Database.SYNCED);
        db.update(DBReference.db, values, SELECTION_NOT_SYNCED, null);
        values.clear();
    }

    public void syncDrawerReferences()
    {
        values.put(Database.synced, Database.SYNCED);
        db.update(DBDrawerReference.db, values, SELECTION_NOT_SYNCED, null);
        values.clear();
    }

    public void syncSubjects()
    {
        values.put(Database.synced, Database.SYNCED);
        db.update(DBSubject.db, values, SELECTION_NOT_SYNCED, null);
        values.clear();
    }

    public void syncTests()
    {
        values.put(Database.synced, Database.SYNCED);
        db.update(DBTest.db, values, SELECTION_NOT_SYNCED, null);
        values.clear();
    }

    public void syncRemoves()
    {
        db.delete(DBRemovedItem.db, null, null);
    }

}
