package com.delvinglanguages.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.delvinglanguages.data.DataBase.DBDrawerReference;
import com.delvinglanguages.data.DataBase.DBLanguage;
import com.delvinglanguages.data.DataBase.DBReference;
import com.delvinglanguages.data.DataBase.DBRemovedReference;
import com.delvinglanguages.data.DataBase.DBStatistics;
import com.delvinglanguages.data.DataBase.DBTest;
import com.delvinglanguages.data.DataBase.DBTheme;
import com.delvinglanguages.data.DataBase.DBThemePair;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemePair;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.kernel.util.Languages;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.Tests;
import com.delvinglanguages.kernel.util.ThemePairs;
import com.delvinglanguages.kernel.util.Themes;

import java.util.TreeSet;

public class DataBaseManager {

    private DataBase gateway;

    public DataBaseManager(Context context) {
        gateway = new DataBase(context);
    }

    /**
     * ******** READS *************
     **/
    public Languages readLanguages() {
        SQLiteDatabase database = gateway.getReadableDatabase();
        Languages result = new Languages();
        Cursor cursorL = database.query(DBLanguage.db, DBLanguage.cols, null, null, null, null, null);

        Cursor cursorS = database.query(DBStatistics.db, DBStatistics.cols, null, null, null, null, null);

        cursorL.moveToFirst();
        cursorS.moveToFirst();
        while (!cursorL.isAfterLast()) {
            Language language = cursorToLanguage(cursorL);
            language.setStatistics(cursorToStatistics(cursorS));
            result.add(language);
            cursorL.moveToNext();
            cursorS.moveToNext();
        }
        cursorL.close();
        cursorS.close();
        database.close();
        System.out.println("# Languages in DB: " + result.size());
        return result;
    }

    public DReferences readReferences(int langID) {
        TreeSet<Integer> remIds = readRemovedReferencesIds(langID);
        SQLiteDatabase database = gateway.getReadableDatabase();
        DReferences result = new DReferences();
        Cursor cursor = database.query(DBReference.db, DBReference.cols, DBReference.lang_id + "=" + langID, null, null, null, DBReference.name + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (!remIds.contains(cursor.getInt(0))) {
                result.add(cursorToReference(cursor));
            }
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        System.out.println("# References in DB: " + result.size());
        return result;
    }

    public DrawerReferences readDrawerReferences(int langID) {
        SQLiteDatabase database = gateway.getReadableDatabase();
        DrawerReferences result = new DrawerReferences();
        Cursor cursor = database.query(DBDrawerReference.db, DBDrawerReference.cols, DBDrawerReference.lang_id + "=" + langID, null, null, null, DBDrawerReference.name + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursorToDrawerReference(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        System.out.println("# DrawerReferences in DB: " + result.size());
        return result;
    }

    private TreeSet<Integer> readRemovedReferencesIds(int lang_id) {
        SQLiteDatabase database = gateway.getReadableDatabase();
        Cursor cursor = database.query(DBRemovedReference.db, DBRemovedReference.cols, DBRemovedReference.lang_id + " = " + lang_id, null, null, null, null);

        TreeSet<Integer> result = new TreeSet<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursor.getInt(2));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return result;
    }

    public DReferences readRemovedReferences(int langID) {
        TreeSet<Integer> referenceIds = readRemovedReferencesIds(langID);

        SQLiteDatabase database = gateway.getReadableDatabase();

        DReferences result = new DReferences();
        for (Integer reference_id : referenceIds) {
            Cursor cursor = database.query(DBReference.db, DBReference.cols, DBReference.id + " = " + reference_id, null, null, null, null);
            cursor.moveToFirst();
            DReference reference = cursorToReference(cursor);
            cursor.close();
            result.add(reference);
        }
        database.close();

        System.out.println("# RemovedReferences in DB: " + result.size());
        return result;
    }

    public Tests readTests(int langID) {
        SQLiteDatabase database = gateway.getReadableDatabase();
        Tests result = new Tests();
        Cursor cursor = database.query(DBTest.db, DBTest.cols, DBTest.lang_id + "=" + langID, null, null, null, DBTest.name + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Test test = cursorToTest(cursor);
            result.add(test);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        System.out.println("# Tests in DB: " + result.size());
        return result;
    }

    public Themes readThemes(int langID) {
        SQLiteDatabase database = gateway.getReadableDatabase();
        Cursor cursor = database.query(DBTheme.db, DBTheme.cols, DBTheme.lang_id + "=" + langID, null, null, null, DBTheme.name + " ASC");

        Themes result = new Themes();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Theme theme = cursorToTheme(cursor);
            theme.setPairs(readThemePairs(theme.id));
            result.add(theme);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        System.out.println("# Themes in DB: " + result.size());
        return result;
    }

    public ThemePairs readThemePairs(int themeID) {
        SQLiteDatabase database = gateway.getReadableDatabase();
        Cursor cursor = database.query(DBThemePair.db, DBThemePair.cols, DBThemePair.theme_id + "=" + themeID, null, null, null, null);

        ThemePairs result = new ThemePairs();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursorToThemePair(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return result;
    }

    /**
     * ******** UPDATES *************
     **/
    public void updateLanguage(Language lang) {
        ContentValues values = new ContentValues();
        values.put(DBLanguage.code, lang.CODE);
        values.put(DBLanguage.name, lang.language_name);
        values.put(DBLanguage.settings, lang.settings);
        SQLiteDatabase database = gateway.getWritableDatabase();
        database.update(DBLanguage.db, values, DBLanguage.id + " = " + lang.id, null);
        database.close();
    }

    public void updateReference(DReference reference) {
        ContentValues values = new ContentValues();
        values.put(DBReference.name, reference.name);
        values.put(DBReference.inflexions, reference.getInflexions().toString());
        values.put(DBReference.pronunciation, reference.pronunciation);
        values.put(DBReference.priority, reference.priority);

        SQLiteDatabase database = gateway.getWritableDatabase();
        database.update(DBReference.db, values, DBReference.id + " = " + reference.id, null);
        database.close();
    }

    public void updateReferenceLanguage(DReference reference, int newlang_id) {
        ContentValues values = new ContentValues();
        values.put(DBReference.lang_id, newlang_id);
        SQLiteDatabase database = gateway.getWritableDatabase();
        database.update(DBReference.db, values, DBReference.id + " = " + reference.id, null);
        database.close();
    }

    public void updateReferencePriority(DReference reference) {
        ContentValues values = new ContentValues();
        values.put(DBReference.priority, reference.priority);
        SQLiteDatabase database = gateway.getWritableDatabase();
        database.update(DBReference.db, values, DBReference.id + " = " + reference.id, null);
        database.close();
    }

    public void updateDrawerReferencesLanguage(int oldlang_id, int newlang_id) {
        ContentValues values = new ContentValues();
        values.put(DBDrawerReference.lang_id, newlang_id);
        SQLiteDatabase database = gateway.getWritableDatabase();
        database.update(DBDrawerReference.db, values, DBDrawerReference.lang_id + " = " + oldlang_id, null);
        database.close();
    }

    public void updateTest(Test test) {
        SQLiteDatabase database = gateway.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBTest.name, test.name);
        values.put(DBTest.runtimes, test.getRunTimes());
        values.put(DBTest.content, Test.wrapContent(test));

        database.update(DBTest.db, values, DBTest.id + " = " + test.id, null);
        database.close();
    }

    public void updateStatistics(Statistics e) {
        ContentValues values = new ContentValues();
        values.put(DBStatistics.tries, e.intentos);
        values.put(DBStatistics.hits1, e.aciertos1);
        values.put(DBStatistics.hits2, e.aciertos2);
        values.put(DBStatistics.hits3, e.aciertos3);
        values.put(DBStatistics.misses, e.fallos);
        SQLiteDatabase database = gateway.getWritableDatabase();
        database.update(DBStatistics.db, values, DBStatistics.id + " = " + e.id, null);
        database.close();
    }

    public void updateTheme(Theme theme) {
        ContentValues values = new ContentValues();
        values.put(DBTheme.name, theme.getName());
        SQLiteDatabase database = gateway.getWritableDatabase();
        database.update(DBTheme.db, values, DBTheme.id + " = " + theme.id, null);
        database.delete(DBThemePair.db, DBThemePair.theme_id + " = " + theme.id, null);
        database.close();

        for (ThemePair pair : theme.getPairs()) {
            insertThemePair(theme.id, pair);
        }
    }

    /**
     * *********************** INSERTS ************************
     **/
    public Language insertLanguage(int code, String name, int settings) {
        // Inserting stadistics
        ContentValues values = new ContentValues();
        values.put(DBStatistics.tries, 0);
        values.put(DBStatistics.hits1, 0);
        values.put(DBStatistics.hits2, 0);
        values.put(DBStatistics.hits3, 0);
        values.put(DBStatistics.misses, 0);
        SQLiteDatabase database = gateway.getWritableDatabase();
        long stats_id = database.insert(DBStatistics.db, null, values);

        // Inserting language
        values.clear();
        values.put(DBLanguage.code, code);
        values.put(DBLanguage.name, name);
        values.put(DBLanguage.statistics, stats_id);
        values.put(DBLanguage.settings, settings);
        int lang_id = (int) database.insert(DBLanguage.db, null, values);

        Language language = new Language(lang_id, code, name, settings);
        language.setStatistics(new Statistics((int) stats_id));
        database.close();
        return language;
    }

    public DReference insertReference(String name, Inflexions inflexions, int langID, String pronunciation, int priority) {
        ContentValues values = new ContentValues();
        values.put(DBReference.name, name);
        values.put(DBReference.lang_id, langID);
        values.put(DBReference.inflexions, inflexions.toString());
        values.put(DBReference.pronunciation, pronunciation);
        values.put(DBReference.priority, priority);

        SQLiteDatabase database = gateway.getWritableDatabase();
        int reference_id = (int) database.insert(DBReference.db, null, values);
        database.close();

        return new DReference(reference_id, name, pronunciation, inflexions, DReference.INITIAL_PRIORITY);
    }

    public void insertRemovedReference(int langID, int reference_id) {
        ContentValues values = new ContentValues();
        values.put(DBRemovedReference.lang_id, langID);
        values.put(DBRemovedReference.reference_id, reference_id);
        SQLiteDatabase database = gateway.getWritableDatabase();
        database.insert(DBRemovedReference.db, null, values);
        database.close();
    }

    public DrawerReference insertDrawerReference(String note, int langID) {
        ContentValues values = new ContentValues();
        values.put(DBDrawerReference.name, note);
        values.put(DBDrawerReference.lang_id, langID);
        SQLiteDatabase database = gateway.getWritableDatabase();
        long note_id = database.insert(DBDrawerReference.db, null, values);
        database.close();
        return new DrawerReference((int) note_id, note);
    }

    public Test insertTest(String test_name, DReferences refs, int lang_id) {
        Test test = new Test(test_name, refs);

        ContentValues values = new ContentValues();
        values.put(DBTest.lang_id, lang_id);
        values.put(DBTest.name, test_name);
        values.put(DBTest.runtimes, test.getRunTimes());
        values.put(DBTest.content, Test.wrapContent(test));

        SQLiteDatabase database = gateway.getWritableDatabase();
        test.id = (int) database.insert(DBTest.db, null, values);
        database.close();
        return test;
    }

    public Theme insertTheme(int langID, String thname, ThemePairs pairs) {
        ContentValues values = new ContentValues();
        values.put(DBTheme.lang_id, langID);
        values.put(DBTheme.name, thname);
        SQLiteDatabase database = gateway.getWritableDatabase();
        int id = (int) database.insert(DBTheme.db, null, values);
        database.close();

        for (ThemePair pair : pairs) {
            insertThemePair(id, pair);
        }
        return new Theme(id, thname, pairs);
    }

    public void insertThemePair(int theme_id, ThemePair pair) {
        ContentValues values = new ContentValues();
        values.put(DBThemePair.theme_id, theme_id);
        values.put(DBThemePair.in_delv, pair.inDelved);
        values.put(DBThemePair.in_nativ, pair.inNative);
        SQLiteDatabase database = gateway.getWritableDatabase();
        database.insert(DBThemePair.db, null, values);
        database.close();
    }

    /**
     * ******************** DELETES *********************
     **/
    public void deleteLanguage(Language lang) {
        SQLiteDatabase database = gateway.getWritableDatabase();
        // Removing references
        database.delete(DBReference.db, DBReference.lang_id + " = " + lang.id, null);
        // Removing trash
        database.delete(DBRemovedReference.db, DBRemovedReference.lang_id + " = " + lang.id, null);
        // Removing store
        database.delete(DBDrawerReference.db, DBDrawerReference.lang_id + " = " + lang.id, null);
        // Removing tests
        database.delete(DBTest.db, DBTest.lang_id + " = " + lang.id, null);
        // Removing themes
        database.close();
        Themes themes = readThemes(lang.id);
        database = gateway.getWritableDatabase();
        for (Theme theme : themes) {
            // Removing theme pairs
            database.delete(DBThemePair.db, DBThemePair.theme_id + " = " + theme.id, null);
        }
        database.delete(DBTheme.db, DBTheme.lang_id + " = " + lang.id, null);
        // Removing language
        database.delete(DBLanguage.db, DBLanguage.id + " = " + lang.id, null);
        // Removing statistics
        database.delete(DBStatistics.db, DBStatistics.id + " = " + lang.id, null);
        database.close();
    }

    public void deleteReferenceTemporarily(int lang_id, int reference_id) {
        insertRemovedReference(lang_id, reference_id);
    }

    public void deleteAllRemovedReferences(int lang_id) {
        TreeSet<Integer> ids = readRemovedReferencesIds(lang_id);
        SQLiteDatabase database = gateway.getWritableDatabase();
        database.delete(DBRemovedReference.db, DBRemovedReference.lang_id + " = " + lang_id, null);
        for (Integer reference_id : ids) {
            database.delete(DBReference.db, DBReference.id + " = " + reference_id, null);
        }
        database.close();
    }

    public void deleteDrawerReference(int id) {
        SQLiteDatabase database = gateway.getWritableDatabase();
        database.delete(DBDrawerReference.db, DBDrawerReference.id + " = " + id, null);
        database.close();
    }

    public void deleteTest(int id) {
        SQLiteDatabase database = gateway.getWritableDatabase();
        database.delete(DBTest.db, DBTest.id + " = " + id, null);
        database.close();
    }

    public void deleteTheme(int theme_id) {
        SQLiteDatabase database = gateway.getWritableDatabase();
        database.delete(DBThemePair.db, DBThemePair.theme_id + " = " + theme_id, null);
        database.delete(DBTheme.db, DBTheme.id + " = " + theme_id, null);
        database.close();
    }

    /**
     * ******************** Restore *********************
     **/
    public void restoreReference(int reference_id) {
        SQLiteDatabase database = gateway.getWritableDatabase();
        database.delete(DBRemovedReference.db, DBRemovedReference.reference_id + " = " + reference_id, null);
        database.close();
    }

    /**
     * ******************** "Cursor to" functions *********************
     **/
    private Language cursorToLanguage(Cursor c) {
        return new Language(c.getInt(0), c.getInt(1), c.getString(2), c.getInt(4));
    }

    private DReference cursorToReference(Cursor c) {
        return new DReference(c.getInt(0), c.getString(1), c.getString(4), c.getString(3), c.getInt(5));
    }

    private Statistics cursorToStatistics(Cursor c) {
        return new Statistics(c.getInt(0), c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));
    }

    private DrawerReference cursorToDrawerReference(Cursor c) {
        return new DrawerReference(c.getInt(0), c.getString(2));
    }

    private Test cursorToTest(Cursor c) {
        return new Test(c.getInt(0), c.getString(2), c.getInt(3), c.getString(4));
    }

    private Theme cursorToTheme(Cursor c) {
        return new Theme(c.getInt(0), c.getString(2));
    }

    private ThemePair cursorToThemePair(Cursor c) {
        return new ThemePair(c.getString(2), c.getString(3));
    }

}
