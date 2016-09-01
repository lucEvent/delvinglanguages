package com.delvinglanguages.data;

import android.content.Context;
import android.database.Cursor;

import com.delvinglanguages.data.Database.DBDrawerReference;
import com.delvinglanguages.data.Database.DBItemeRemoved;
import com.delvinglanguages.data.Database.DBLanguage;
import com.delvinglanguages.data.Database.DBReference;
import com.delvinglanguages.data.Database.DBStatistics;
import com.delvinglanguages.data.Database.DBTest;
import com.delvinglanguages.data.Database.DBTheme;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.util.Languages;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.ThemePairs;
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

    public Languages readLanguages()
    {
        Languages result = new Languages();

        Cursor cursor = db.query(DBLanguage.db, DBLanguage.cols, SELECTION_NOT_SYNCED, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                result.add(Database.DBLanguage.parse(cursor));

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

    public SyncWrappers readThemes()
    {
        SyncWrappers result = new SyncWrappers();

        Cursor cursor = db.query(DBTheme.db, DBTheme.cols, SELECTION_NOT_SYNCED, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                Theme theme = DBTheme.parse(cursor);
                result.add(new SyncWrapper(theme.id, cursor.getInt(1), theme.wrapType(), theme.wrap()));

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

    public SyncWrappers readRemoves()
    {
        SyncWrappers result = new SyncWrappers();

        Cursor cursor = db.query(DBTest.db, DBTest.cols, SELECTION_NOT_SYNCED, null, null, null, null);

        if (cursor.moveToFirst())
            do {

                result.add(DBItemeRemoved.parse(cursor));

            } while (cursor.moveToNext());

        cursor.close();

        return result;
    }


    // **************************************************** \\
    ////////////////////// Inserts \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void insertLanguage(int lang_id, int code, String name, int settings)
    {
        super.insertLanguage(lang_id, code, name, settings, Database.SYNCED);
    }

    public void insertReference(int id, int lang_id, String name, String inflexions, String pronunciation, int priority)
    {
        super.insertReference(id, lang_id, name, inflexions, pronunciation, priority, Database.SYNCED);
    }

    public void insertDrawerReference(int id, int lang_id, String note)
    {
        super.insertDrawerReference(id, lang_id, note, Database.SYNCED);
    }

    public void insertTheme(int id, int lang_id, String name, ThemePairs pairs)
    {
        super.insertTheme(id, lang_id, name, pairs, Database.SYNCED);
    }

    public void insertTest(int id, int lang_id, String name, int runTimes, String content, int theme_id)
    {
        super.insertTest(id, lang_id, name, runTimes, content, theme_id, Database.SYNCED);
    }

    public void insertSyncItem(int item_id, int language_id, int type)
    {
        values.put(Database.id, item_id);
        values.put(Database.lang_id, language_id);
        values.put(Database.type, type);
        db.insert(DBItemeRemoved.db, null, values);
        values.clear();
    }

    // **************************************************** \\
    ////////////////////// Updates \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void updateLanguage(int id, int code, String name, int settings)
    {
        super.updateLanguage(id, code, name, settings, Database.SYNCED);
    }

    public void updateStatistics(Statistics statistics)
    {
        super.updateStatistics(statistics.id, statistics, Database.SYNCED);
    }

    public void updateReference(DReference reference, int lang_id)
    {
        super.updateReference(reference.id, lang_id, reference.name, reference.getInflexions().wrap(),
                reference.pronunciation, reference.priority, Database.SYNCED);
    }

    public void updateTheme(Theme theme, int lang_id)
    {
        super.updateTheme(theme.id, lang_id, theme.getName(), theme.getPairs(), Database.SYNCED);
    }

    public void updateTest(Test test, int lang_id)
    {
        super.updateTest(test.id, lang_id, test.name, test.getRunTimes(), Test.wrapContent(test), Database.SYNCED);
    }

    // **************************************************** \\
    ////////////////////// Syncs \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void syncLanguages()
    {
        values.put(Database.synced, Database.SYNCED);
        db.update(DBLanguage.db, values, SELECTION_NOT_SYNCED, null);
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

    public void syncThemes()
    {
        values.put(Database.synced, Database.SYNCED);
        db.update(DBTheme.db, values, SELECTION_NOT_SYNCED, null);
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
        db.delete(DBItemeRemoved.db, null, null);
    }

}
