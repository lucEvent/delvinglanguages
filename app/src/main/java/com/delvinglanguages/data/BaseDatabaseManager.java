package com.delvinglanguages.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.delvinglanguages.data.Database.DBDelvingList;
import com.delvinglanguages.data.Database.DBDrawerReference;
import com.delvinglanguages.data.Database.DBReference;
import com.delvinglanguages.data.Database.DBRemovedItem;
import com.delvinglanguages.data.Database.DBStatistics;
import com.delvinglanguages.data.Database.DBSubject;
import com.delvinglanguages.data.Database.DBTest;
import com.delvinglanguages.data.Database.DBUsage;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.Usage;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DelvingLists;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.RemovedItems;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.Subjects;
import com.delvinglanguages.kernel.util.Tests;
import com.delvinglanguages.kernel.util.Usages;
import com.delvinglanguages.kernel.util.Wrapper;

import java.util.Random;

/**
 * BaseDatabaseManager
 * <p/>
 * Insert/Update operations don't return the Object
 * <p/>
 * Steps for a good use:
 * 1. call openReadableDatabase/openWritableDatabase
 * 2. call as many functions read/insert/update as needed
 * 3. call closeReadableDatabase/closeWritableDatabase
 */
public abstract class BaseDatabaseManager {

    protected static final String EQ = "=";
    protected static final String AND = " AND ";

    private Database scheme;

    protected SQLiteDatabase db;

    protected ContentValues values;

    BaseDatabaseManager(Context context)
    {
        scheme = new Database(context);
        values = new ContentValues();
    }

    /**
     * *********** Only Readable DB ***************
     **/
    public SQLiteDatabase openReadableDatabase()
    {
        db = scheme.getReadableDatabase();
        return db;
    }

    public void closeReadableDatabase()
    {
        db.close();
    }

    public void openWritableDatabase()
    {
        db = scheme.getWritableDatabase();
        db.beginTransactionNonExclusive();
    }

    public void closeWritableDatabase()
    {
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    // **************************************************** \\
    /////////////////////// Reads \\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    protected DelvingLists readLists()
    {
        DelvingLists result = new DelvingLists();

        Cursor cursorL = db.query(DBDelvingList.db, DBDelvingList.cols, null, null, null, null, null);
        Cursor cursorS = db.query(DBStatistics.db, DBStatistics.cols, null, null, null, null, null);

        cursorS.moveToFirst();
        if (cursorL.moveToFirst())
            do {
                DelvingList delvingList = DBDelvingList.parse(cursorL);
                delvingList.statistics = DBStatistics.parse(cursorS);
                result.add(delvingList);
                cursorS.moveToNext();
            } while (cursorL.moveToNext());

        cursorL.close();
        cursorS.close();

        return result;
    }

    protected DReferences readReferences(int list_id)
    {
        DReferences result = new DReferences();
        Cursor cursor = db.query(DBReference.db, DBReference.cols, Database.list_id + EQ + list_id, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                result.add(DBReference.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();
        return result;
    }

    protected DReference readReference(int list_id, int reference_id)
    {
        DReference result = null;
        Cursor cursor = db.query(DBReference.db, DBReference.cols, Database.list_id + EQ + list_id + AND + Database.id + EQ + reference_id, null, null, null, null);

        if (cursor.moveToFirst())
            result = DBReference.parse(cursor);

        cursor.close();
        return result;
    }

    protected DrawerReferences readDrawerReferences(int list_id)
    {
        DrawerReferences result = new DrawerReferences();
        Cursor cursor = db.query(DBDrawerReference.db, DBDrawerReference.cols, Database.list_id + EQ + list_id, null, null, null, Database.name + " ASC");

        if (cursor.moveToFirst())
            do {

                result.add(DBDrawerReference.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();
        return result;
    }

    protected Subjects readSubjects(int list_id)
    {
        Cursor cursor = db.query(DBSubject.db, DBSubject.cols, Database.list_id + EQ + list_id, null, null, null, null);

        Subjects result = new Subjects();
        if (cursor.moveToFirst())
            do {

                result.add(DBSubject.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();
        return result;
    }

    protected Tests readTests(int list_id)
    {
        Cursor cursor = db.query(DBTest.db, DBTest.cols, Database.list_id + EQ + list_id, null, null, null, null);

        Tests result = new Tests();
        if (cursor.moveToFirst())
            do {

                result.add(DBTest.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();
        return result;
    }

    protected RemovedItems readRemovedItems(int list_id)
    {
        Cursor cursor = db.query(DBRemovedItem.db, DBRemovedItem.cols, Database.list_id + EQ + list_id, null, null, null, null);

        RemovedItems result = new RemovedItems();
        if (cursor.moveToFirst())
            do {

                result.add(DBRemovedItem.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();
        return result;
    }

    protected Usages readUsages(int list_id, int reference_id, Usages usages)
    {
        Cursor cursor = db.query(DBUsage.db, DBUsage.cols, Database.list_id + EQ + list_id + AND + Database.reference_id + EQ + reference_id, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                usages.add(DBUsage.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();
        return usages;
    }

    // **************************************************** \\
    ////////////////////// Inserts \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    protected int insertDelvingList(int id, int from_code, int to_code, String name, int settings, int synced)
    {
        Cursor c = db.query(DBDelvingList.db, DBDelvingList.cols, Database.id + EQ + id, null, null, null, null);
        if (c.moveToFirst())
            id = Math.abs(new Random().nextInt());
        c.close();

        values.put(Database.id, id);
        values.put(Database.attempts, 0);
        values.put(Database.hits1, 0);
        values.put(Database.hits2, 0);
        values.put(Database.hits3, 0);
        values.put(Database.misses, 0);
        values.put(Database.synced, synced);
        db.insert(DBStatistics.db, null, values);
        values.clear();

        values.put(Database.id, id);
        values.put(Database.code, from_code | (to_code << 16));
        values.put(Database.name, name);
        values.put(Database.statistics_id, id);
        values.put(Database.settings, settings);
        values.put(Database.synced, synced);
        db.insert(DBDelvingList.db, null, values);
        values.clear();

        return id;
    }

    protected void insertReference(int id, int list_id, String name, String inflexions, String pronunciation, int priority, int synced)
    {
        values.put(Database.id, id);
        values.put(Database.list_id, list_id);
        values.put(Database.name, name);
        values.put(Database.pronunciation, pronunciation);
        values.put(Database.inflexions, inflexions);
        values.put(Database.priority, priority);
        values.put(Database.synced, synced);
        db.insert(DBReference.db, null, values);
        values.clear();
    }

    protected void insertRemovedItem(int item_id, int list_id, Wrapper wrapper, int synced)
    {
        values.put(Database.id, item_id);
        values.put(Database.list_id, list_id);
        values.put(Database.type, wrapper.wrapType());
        values.put(Database.wrappedContent, wrapper.wrap());
        values.put(Database.synced, synced);
        db.insert(DBRemovedItem.db, null, values);
        values.clear();
    }

    protected void insertDrawerReference(int id, int list_id, String note, int synced)
    {
        values.put(Database.id, id);
        values.put(Database.list_id, list_id);
        values.put(Database.name, note);
        values.put(Database.synced, synced);
        db.insert(DBDrawerReference.db, null, values);
        values.clear();
    }

    protected void insertSubject(int id, int list_id, String name, String referencesIds, int synced)
    {
        values.put(Database.id, id);
        values.put(Database.list_id, list_id);
        values.put(Database.name, name);
        values.put(Database.pairs, referencesIds);
        values.put(Database.synced, synced);
        db.insert(DBSubject.db, null, values);
        values.clear();
    }

    protected void insertTest(int id, int list_id, String name, int runTimes, String content, int from_id, int synced)
    {
        values.put(Database.id, id);
        values.put(Database.list_id, list_id);
        values.put(Database.name, name);
        values.put(Database.runtimes, runTimes);
        values.put(Database.content, content);
        values.put(Database.from_id, from_id);
        values.put(Database.synced, synced);
        db.insert(DBTest.db, null, values);
        values.clear();
    }

    protected void insertUsage(int list_id, int reference_id, Usage usage, int synced)
    {
        values.put(Database.list_id, list_id);
        values.put(Database.reference_id, reference_id);
        values.put(Database.translation, usage.translation);
        values.put(Database.usage, usage.usage);
        values.put(Database.synced, synced);
        long id = db.insert(DBUsage.db, null, values);
        values.clear();
        System.out.println("[" + id + "]=id inserting usage for:" + usage.translation + " [" + reference_id + "]");
    }

    // **************************************************** \\
    ////////////////////// Updates \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    protected void updateDelvingList(int id, int from_code, int to_code, String name, int settings, int synced)
    {
        values.put(Database.code, from_code | (to_code << 16));
        values.put(Database.name, name);
        values.put(Database.settings, settings);
        values.put(Database.synced, synced);
        db.update(DBDelvingList.db, values, Database.id + EQ + id, null);
        values.clear();
    }

    protected void updateStatistics(int id, Statistics st, int synced)
    {
        values.put(Database.attempts, st.attempts);
        values.put(Database.hits1, st.hits_at_1st);
        values.put(Database.hits2, st.hits_at_2nd);
        values.put(Database.hits3, st.hits_at_3rd);
        values.put(Database.misses, st.misses);
        values.put(Database.synced, synced);
        db.update(DBStatistics.db, values, Database.id + EQ + id, null);
        values.clear();
    }

    protected void updateReference(int id, int list_id, String name, String inflexions, String pronunciation, int priority, int synced)
    {
        values.put(Database.name, name);
        values.put(Database.pronunciation, pronunciation);
        values.put(Database.inflexions, inflexions);
        values.put(Database.priority, priority);
        values.put(Database.synced, synced);
        db.update(DBReference.db, values, Database.list_id + " = " + list_id + AND + Database.id + EQ + id, null);
        values.clear();
    }

    protected void updateReferencePriority(int id, int list_id, int priority)
    {
        values.put(Database.priority, priority);
        values.put(Database.synced, Database.NOT_SYNCED);
        db.update(DBReference.db, values, Database.list_id + " = " + list_id + AND + Database.id + EQ + id, null);
        values.clear();
    }

    protected void updateSubject(int id, int list_id, String name, String referencesIds, int synced)
    {
        values.put(Database.name, name);
        values.put(Database.pairs, referencesIds);
        values.put(Database.synced, synced);
        db.update(DBSubject.db, values, Database.list_id + " = " + list_id + AND + Database.id + " = " + id, null);
        values.clear();
    }

    protected void updateTest(int id, int list_id, String name, int runTimes, String content, int synced)
    {
        values.put(Database.name, name);
        values.put(Database.runtimes, runTimes);
        values.put(Database.content, content);
        values.put(Database.synced, synced);
        db.update(DBTest.db, values, Database.list_id + " = " + list_id + AND + Database.id + " = " + id, null);
        values.clear();
    }

    // **************************************************** \\
    ////////////////////// Deletes \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void deleteDelvingList(int list_id)
    {
        db.delete(DBUsage.db, Database.list_id + EQ + list_id, null);
        db.delete(DBReference.db, Database.list_id + EQ + list_id, null);
        db.delete(DBDrawerReference.db, Database.list_id + EQ + list_id, null);
        db.delete(DBTest.db, Database.list_id + EQ + list_id, null);
        db.delete(DBSubject.db, Database.list_id + EQ + list_id, null);
        db.delete(DBRemovedItem.db, Database.list_id + EQ + list_id, null);
        db.delete(DBDelvingList.db, Database.id + EQ + list_id, null);
        db.delete(DBStatistics.db, Database.id + EQ + list_id, null);
    }

    public void deleteReference(int list_id, int id)
    {
        db.delete(DBReference.db, Database.list_id + " = " + list_id + AND + Database.id + " = " + id, null);
    }

    public void deleteDrawerReference(int list_id, int id)
    {
        db.delete(DBDrawerReference.db, Database.list_id + " = " + list_id + AND + Database.id + " = " + id, null);
    }

    public void deleteSubject(int list_id, int id)
    {
        db.delete(DBSubject.db, Database.list_id + " = " + list_id + AND + Database.id + " = " + id, null);
    }

    public void deleteTest(int list_id, int id)
    {
        db.delete(DBTest.db, Database.list_id + " = " + list_id + AND + Database.id + " = " + id, null);
    }

    protected void deleteRemovedItem(int list_id, int item_id, int type)
    {
        db.delete(DBRemovedItem.db,
                Database.list_id + " = " + list_id + AND + Database.id + " = " + item_id + AND + Database.type + " = " + type, null);
    }

    protected void deleteAllRemovedItems(int list_id)
    {
        db.delete(DBRemovedItem.db, Database.list_id + EQ + list_id, null);
    }

    protected void deleteAllUsagesOf(int list_id, int reference_id)
    {
        db.delete(DBUsage.db, Database.list_id + " = " + list_id + AND + Database.reference_id + " = " + reference_id, null);
    }

    protected void deleteUsages(int list_id, int reference_id, String translation)
    {
        db.delete(DBUsage.db, Database.list_id + " = " + list_id
                + AND + Database.reference_id + " = " + reference_id
                + AND + Database.translation + " = '" + translation + "'", null);
    }

}
