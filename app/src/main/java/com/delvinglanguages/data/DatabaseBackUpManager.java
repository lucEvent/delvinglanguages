package com.delvinglanguages.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemePair;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.Tests;
import com.delvinglanguages.kernel.util.ThemePairs;
import com.delvinglanguages.kernel.util.Themes;

import java.util.TreeSet;

public class DatabaseBackUpManager {

    private static final String EQ = "=";

    private DataBase sqlite;

    private SQLiteDatabase db;

    private ContentValues values;

    public DatabaseBackUpManager(Context context) {
        sqlite = new DataBase(context);
        values = new ContentValues();
    }

    /**
     * *********** Only Readable DB ***************
     **/
    public void openReadableDatabase() {
        db = sqlite.getReadableDatabase();
    }

    public void closeReadableDatabase() {
        db.close();
    }

    public DReferences readReferences(int language_id) {
        TreeSet<Integer> removedReferencesIds = readRemovedReferencesIds(language_id);

        DReferences result = new DReferences();
        Cursor cursor = db.query(DataBase.DBReference.db, DataBase.DBReference.cols,
                DataBase.DBReference.lang_id + EQ + language_id, null, null, null, DataBase.DBReference.name + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int word_id = cursor.getInt(0);
            if (removedReferencesIds.contains(word_id)) {

                removedReferencesIds.remove(word_id);

            } else {

                DReference word = new DReference(cursor.getInt(0), cursor.getString(1), cursor.getString(4),
                        cursor.getString(3), cursor.getInt(5));

                result.add(word);

            }
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public DrawerReferences readDrawerReferences(int language_id) {
        DrawerReferences result = new DrawerReferences();
        Cursor cursor = db.query(DataBase.DBDrawerReference.db, DataBase.DBDrawerReference.cols,
                DataBase.DBDrawerReference.lang_id + EQ + language_id, null, null, null, DataBase.DBDrawerReference.name + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            DrawerReference word = new DrawerReference(cursor.getInt(0), cursor.getString(2));
            result.add(word);

            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    private TreeSet<Integer> readRemovedReferencesIds(int language_id) {
        Cursor cursor = db.query(DataBase.DBRemovedReference.db, DataBase.DBRemovedReference.cols,
                DataBase.DBRemovedReference.lang_id + EQ + language_id, null, null, null, null);

        TreeSet<Integer> result = new TreeSet<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            result.add(cursor.getInt(2));
            cursor.moveToNext();

        }
        cursor.close();
        return result;
    }

    public Themes readThemes(int language_id) {
        Cursor cursor = db.query(DataBase.DBTheme.db, DataBase.DBTheme.cols,
                DataBase.DBTheme.lang_id + EQ + language_id, null, null, null, DataBase.DBTheme.name + " ASC");

        Themes result = new Themes();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Theme theme = new Theme(cursor.getInt(0), cursor.getString(2));
            theme.setPairs(readThemePairs(theme.id));
            result.add(theme);
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public ThemePairs readThemePairs(int theme_id) {
        Cursor cursor = db.query(DataBase.DBThemePair.db, DataBase.DBThemePair.cols,
                DataBase.DBThemePair.theme_id + EQ + theme_id, null, null, null, null);

        ThemePairs result = new ThemePairs();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ThemePair pair = new ThemePair(cursor.getString(2), cursor.getString(3));
            result.add(pair);

            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public Tests readTests(int language_id) {
        Cursor cursor = db.query(DataBase.DBTest.db, DataBase.DBTest.cols,
                DataBase.DBTest.lang_id + EQ + language_id, null, null, null, DataBase.DBTest.name + " ASC");

        Tests result = new Tests();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Test test = new Test(cursor.getInt(0), cursor.getString(2), cursor.getInt(3), cursor.getString(4));
            result.add(test);

            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    /**
     * *********** Writable DB ***************
     **/
    public void openWritableDatabase() {
        db = sqlite.getWritableDatabase();
        db.beginTransactionNonExclusive();
    }

    public void closeWritableDatabase() {
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public Language insertLanguage(int code, String name, int settings) {
        values.put(DataBase.DBStatistics.tries, 0);
        values.put(DataBase.DBStatistics.hits1, 0);
        values.put(DataBase.DBStatistics.hits2, 0);
        values.put(DataBase.DBStatistics.hits3, 0);
        values.put(DataBase.DBStatistics.misses, 0);
        long stats_id = db.insert(DataBase.DBStatistics.db, null, values);
        values.clear();

        values.put(DataBase.DBLanguage.code, code);
        values.put(DataBase.DBLanguage.name, name);
        values.put(DataBase.DBLanguage.statistics, stats_id);
        values.put(DataBase.DBLanguage.settings, settings);
        int lang_id = (int) db.insert(DataBase.DBLanguage.db, null, values);
        values.clear();

        Language language = new Language(lang_id, code, name, settings);
        language.setStatistics(new Statistics((int) stats_id));
        return language;
    }

    public void insertStatistics(Statistics S) {
        values.put(DataBase.DBStatistics.tries, S.intentos);
        values.put(DataBase.DBStatistics.hits1, S.aciertos1);
        values.put(DataBase.DBStatistics.hits2, S.aciertos2);
        values.put(DataBase.DBStatistics.hits3, S.aciertos3);
        values.put(DataBase.DBStatistics.misses, S.fallos);
        db.update(DataBase.DBStatistics.db, values, DataBase.DBStatistics.id + EQ + S.id, null);
        values.clear();
    }

    public void insertReference(String name, String inflexions, int lang_id, String pronunciation, int priority) {
        values.put(DataBase.DBReference.name, name);
        values.put(DataBase.DBReference.lang_id, lang_id);
        values.put(DataBase.DBReference.inflexions, inflexions);
        values.put(DataBase.DBReference.pronunciation, pronunciation);
        values.put(DataBase.DBReference.priority, priority);
        db.insert(DataBase.DBReference.db, null, values);
        values.clear();
    }

    public void insertDrawerReference(String note, int lang_id) {
        values.put(DataBase.DBDrawerReference.name, note);
        values.put(DataBase.DBDrawerReference.lang_id, lang_id);
        db.insert(DataBase.DBDrawerReference.db, null, values);
        values.clear();
    }

    public void insertTheme(int lang_id, String name, ThemePairs pairs) {
        values.put(DataBase.DBTheme.lang_id, lang_id);
        values.put(DataBase.DBTheme.name, name);
        int theme_id = (int) db.insert(DataBase.DBTheme.db, null, values);
        values.clear();

        for (ThemePair pair : pairs) {
            values.put(DataBase.DBThemePair.theme_id, theme_id);
            values.put(DataBase.DBThemePair.in_delv, pair.inDelved);
            values.put(DataBase.DBThemePair.in_nativ, pair.inNative);
            db.insert(DataBase.DBThemePair.db, null, values);
            values.clear();
        }
    }

    public void insertTest(String test_name, int runTimes, String content, int lang_id) {
        values.put(DataBase.DBTest.lang_id, lang_id);
        values.put(DataBase.DBTest.name, test_name);
        values.put(DataBase.DBTest.runtimes, runTimes);
        values.put(DataBase.DBTest.content, content);
        db.insert(DataBase.DBTest.db, null, values);
        values.clear();
    }

}
