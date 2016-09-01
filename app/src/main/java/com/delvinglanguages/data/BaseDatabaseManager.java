package com.delvinglanguages.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.delvinglanguages.data.Database.DBDrawerReference;
import com.delvinglanguages.data.Database.DBLanguage;
import com.delvinglanguages.data.Database.DBReference;
import com.delvinglanguages.data.Database.DBRemovedReference;
import com.delvinglanguages.data.Database.DBStatistics;
import com.delvinglanguages.data.Database.DBTest;
import com.delvinglanguages.data.Database.DBTheme;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Languages;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.Tests;
import com.delvinglanguages.kernel.util.ThemePairs;
import com.delvinglanguages.kernel.util.Themes;

import java.util.Random;
import java.util.TreeSet;

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

    public BaseDatabaseManager(Context context)
    {
        scheme = new Database(context);
        values = new ContentValues();
    }

    /**
     * *********** Only Readable DB ***************
     **/
    public void openReadableDatabase()
    {
        db = scheme.getReadableDatabase();
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

    public Languages readLanguages()
    {
        Languages result = new Languages();

        Cursor cursorL = db.query(DBLanguage.db, DBLanguage.cols, null, null, null, null, null);
        Cursor cursorS = db.query(DBStatistics.db, DBStatistics.cols, null, null, null, null, null);

        cursorS.moveToFirst();
        if (cursorL.moveToFirst())
            do {
                Language language = DBLanguage.parse(cursorL);
                language.statistics = DBStatistics.parse(cursorS);
                result.add(language);
                cursorS.moveToNext();
            } while (cursorL.moveToNext());

        cursorL.close();
        cursorS.close();

        return result;
    }

    public DReferences readReferences(int language_id)
    {
        TreeSet<Integer> removedReferencesIds = readRemovedReferencesIds(language_id);

        DReferences result = new DReferences();
        Cursor cursor = db.query(DBReference.db, DBReference.cols, Database.lang_id + EQ + language_id, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                int word_id = cursor.getInt(0);
                if (removedReferencesIds.contains(word_id))
                    removedReferencesIds.remove(word_id);
                else
                    result.add(DBReference.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();
        return result;
    }

    public DrawerReferences readDrawerReferences(int language_id)
    {
        DrawerReferences result = new DrawerReferences();
        Cursor cursor = db.query(DBDrawerReference.db, DBDrawerReference.cols, Database.lang_id + EQ + language_id, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                result.add(DBDrawerReference.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();
        return result;
    }

    protected TreeSet<Integer> readRemovedReferencesIds(int language_id)
    {
        Cursor cursor = db.query(DBRemovedReference.db, DBRemovedReference.cols, Database.lang_id + EQ + language_id, null, null, null, null);

        TreeSet<Integer> result = new TreeSet<>();
        if (cursor.moveToFirst())
            do {

                result.add(DBRemovedReference.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();
        return result;
    }

    public Themes readThemes(int language_id)
    {
        Cursor cursor = db.query(DBTheme.db, DBTheme.cols, Database.lang_id + EQ + language_id, null, null, null, null);

        Themes result = new Themes();
        if (cursor.moveToFirst())
            do {

                result.add(DBTheme.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();
        return result;
    }

    public Tests readTests(int language_id)
    {
        Cursor cursor = db.query(DBTest.db, DBTest.cols, Database.lang_id + EQ + language_id, null, null, null, null);

        Tests result = new Tests();
        if (cursor.moveToFirst())
            do {

                result.add(DBTest.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();
        return result;
    }

    // **************************************************** \\
    ////////////////////// Inserts \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    protected int insertLanguage(int id, int code, String name, int settings, int synced)
    {
        Cursor c = db.query(DBLanguage.db, DBLanguage.cols, Database.id + EQ + id, null, null, null, null);
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
        values.put(Database.code, code);
        values.put(Database.name, name);
        values.put(Database.statistics_id, id);
        values.put(Database.settings, settings);
        values.put(Database.synced, synced);
        db.insert(DBLanguage.db, null, values);
        values.clear();

        return id;
    }

    protected void insertReference(int id, int lang_id, String name, String inflexions, String pronunciation, int priority, int synced)
    {
        values.put(Database.id, id);
        values.put(Database.lang_id, lang_id);
        values.put(Database.name, name);
        values.put(Database.pronunciation, pronunciation);
        values.put(Database.inflexions, inflexions);
        values.put(Database.priority, priority);
        values.put(Database.synced, synced);
        db.insert(DBReference.db, null, values);
        values.clear();
    }

    protected void insertRemovedReference(int lang_id, int reference_id, int synced)
    {
        values.put(Database.lang_id, lang_id);
        values.put(Database.reference_id, reference_id);
        values.put(Database.synced, synced);
        db.insert(DBRemovedReference.db, null, values);
        values.clear();
    }

    protected void insertDrawerReference(int id, int lang_id, String note, int synced)
    {
        values.put(Database.id, id);
        values.put(Database.lang_id, lang_id);
        values.put(Database.name, note);
        values.put(Database.synced, synced);
        db.insert(DBDrawerReference.db, null, values);
        values.clear();
    }

    protected void insertTheme(int id, int lang_id, String name, ThemePairs pairs, int synced)
    {
        values.put(Database.id, id);
        values.put(Database.lang_id, lang_id);
        values.put(Database.name, name);
        values.put(Database.pairs, pairs.wrap());
        values.put(Database.synced, synced);
        db.insert(DBTheme.db, null, values);
        values.clear();
    }

    protected void insertTest(int id, int lang_id, String name, int runTimes, String content, int theme_id, int synced)
    {
        values.put(Database.id, id);
        values.put(Database.lang_id, lang_id);
        values.put(Database.name, name);
        values.put(Database.runtimes, runTimes);
        values.put(Database.content, content);
        values.put(Database.theme_id, theme_id);
        values.put(Database.synced, synced);
        db.insert(DBTest.db, null, values);
        values.clear();
    }

    // **************************************************** \\
    ////////////////////// Updates \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    protected void updateLanguage(int id, int code, String name, int settings, int synced)
    {
        values.put(Database.code, code);
        values.put(Database.name, name);
        values.put(Database.settings, settings);
        values.put(Database.synced, synced);
        db.update(DBLanguage.db, values, Database.id + EQ + id, null);
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

    protected void updateReference(int id, int lang_id, String name, String inflexions, String pronunciation, int priority, int synced)
    {
        values.put(Database.name, name);
        values.put(Database.pronunciation, pronunciation);
        values.put(Database.inflexions, inflexions);
        values.put(Database.priority, priority);
        values.put(Database.synced, synced);
        db.update(DBReference.db, values, Database.lang_id + " = " + lang_id + AND + Database.id + EQ + id, null);
        values.clear();
    }

    protected void updateReferencePriority(int id, int lang_id, int priority)
    {
        values.put(Database.priority, priority);
        values.put(Database.synced, Database.NOT_SYNCED);
        db.update(DBReference.db, values, Database.lang_id + " = " + lang_id + AND + Database.id + EQ + id, null);
        values.clear();
    }

    protected void updateTheme(int id, int lang_id, String name, ThemePairs pairs, int synced)
    {
        values.put(Database.name, name);
        values.put(Database.pairs, pairs.wrap());
        values.put(Database.synced, synced);
        db.update(DBTheme.db, values, Database.lang_id + " = " + lang_id + AND + Database.id + " = " + id, null);
        values.clear();
    }

    protected void updateTest(int id, int lang_id, String name, int runTimes, String content, int synced)
    {
        values.put(Database.name, name);
        values.put(Database.runtimes, runTimes);
        values.put(Database.content, content);
        values.put(Database.synced, synced);
        db.update(DBTest.db, values, Database.lang_id + " = " + lang_id + AND + Database.id + " = " + id, null);
        values.clear();
    }

    // **************************************************** \\
    ////////////////////// Deletes \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void deleteLanguage(int language_id)
    {
        db.delete(DBReference.db, Database.lang_id + EQ + language_id, null);
        db.delete(DBRemovedReference.db, Database.lang_id + EQ + language_id, null);
        db.delete(DBDrawerReference.db, Database.lang_id + EQ + language_id, null);
        db.delete(DBTest.db, Database.lang_id + EQ + language_id, null);
        db.delete(DBTheme.db, Database.lang_id + EQ + language_id, null);
        db.delete(DBLanguage.db, Database.id + EQ + language_id, null);
        db.delete(DBStatistics.db, Database.id + EQ + language_id, null);
    }

    public void deleteReference(int language_id, int id)
    {
        db.delete(DBReference.db, Database.lang_id + " = " + language_id + AND + Database.id + " = " + id, null);
    }

    public void deleteRemovedReference(int language_id, int ref_id)
    {
        db.delete(DBRemovedReference.db, Database.lang_id + " = " + language_id + AND + Database.reference_id + " = " + ref_id, null);
    }

    public void deleteDrawerReference(int language_id, int id)
    {
        db.delete(DBDrawerReference.db, Database.lang_id + " = " + language_id + AND + Database.id + " = " + id, null);
    }

    public void deleteTheme(int language_id, int id)
    {
        db.delete(DBTheme.db, Database.lang_id + " = " + language_id + AND + Database.id + " = " + id, null);
    }

    public void deleteTest(int language_id, int id)
    {
        db.delete(DBTest.db, Database.lang_id + " = " + language_id + AND + Database.id + " = " + id, null);
    }

}
