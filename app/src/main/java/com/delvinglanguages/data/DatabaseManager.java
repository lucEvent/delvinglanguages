package com.delvinglanguages.data;

import android.content.Context;
import android.database.Cursor;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.data.Database.DBReference;
import com.delvinglanguages.data.Database.DBRemovedReference;
import com.delvinglanguages.data.Database.DBTest;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.kernel.util.Languages;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.Tests;
import com.delvinglanguages.kernel.util.ThemePairs;
import com.delvinglanguages.kernel.util.Themes;

import java.util.Random;
import java.util.TreeSet;

public class DatabaseManager extends BaseDatabaseManager {

    private Random idGenerator;

    public DatabaseManager(Context context)
    {
        super(context);
        idGenerator = new Random();
    }

    // **************************************************** \\
    /////////////////////// Reads \\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public Languages readLanguages()
    {
        Languages result;
        synchronized (this) {
            openReadableDatabase();
            result = super.readLanguages();
            closeReadableDatabase();
        }
        AppSettings.printlog("# Languages in DB: " + result.size());
        return result;
    }

    @Override
    public DReferences readReferences(int lang_id)
    {
        DReferences result;
        synchronized (this) {
            openReadableDatabase();
            result = super.readReferences(lang_id);
            closeReadableDatabase();
        }
        AppSettings.printlog("# References in DB: " + result.size());
        return result;
    }

    @Override
    public DrawerReferences readDrawerReferences(int lang_id)
    {
        DrawerReferences result;
        synchronized (this) {
            openReadableDatabase();
            result = super.readDrawerReferences(lang_id);
            closeReadableDatabase();
        }
        AppSettings.printlog("# DrawerReferences in DB: " + result.size());
        return result;
    }

    public DReferences readRemovedReferences(int lang_id)
    {
        DReferences result = new DReferences();

        synchronized (this) {
            openReadableDatabase();

            TreeSet<Integer> referenceIds = readRemovedReferencesIds(lang_id);

            for (Integer reference_id : referenceIds) {
                Cursor cursor = db.query(DBReference.db, DBReference.cols, Database.id + " = " + reference_id, null, null, null, null);

                if (cursor.moveToFirst())
                    result.add(DBReference.parse(cursor));

                cursor.close();
            }
            closeReadableDatabase();
        }
        AppSettings.printlog("# RemovedReferences in DB: " + result.size());
        return result;
    }

    @Override
    public Themes readThemes(int lang_id)
    {
        Themes result;
        synchronized (this) {
            openReadableDatabase();
            result = super.readThemes(lang_id);
            closeReadableDatabase();
        }
        AppSettings.printlog("# Themes in DB: " + result.size());
        return result;
    }

    @Override
    public Tests readTests(int lang_id)
    {
        Tests result;
        synchronized (this) {
            openReadableDatabase();
            result = super.readTests(lang_id);
            closeReadableDatabase();
        }
        AppSettings.printlog("# Tests in DB: " + result.size());
        return result;
    }

    public Test readTestFromTheme(int theme_id)
    {
        Test res = null;
        synchronized (this) {
            openReadableDatabase();
            Cursor cursor = db.query(DBTest.db, DBTest.cols, Database.theme_id + "=" + theme_id, null, null, null, Database.name + " ASC");

            if (cursor.moveToFirst())
                res = DBTest.parse(cursor);

            cursor.close();
            closeReadableDatabase();
        }
        return res;
    }

    // **************************************************** \\
    ////////////////////// Inserts \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public Language insertLanguage(int code, String name, int settings)
    {
        int random_id = idGenerator.nextInt();

        synchronized (this) {
            openWritableDatabase();
            random_id = super.insertLanguage(random_id, code, name, settings, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
        return new Language(random_id, code, name, settings, new Statistics(random_id));
    }

    public DReference insertReference(int lang_id, String name, Inflexions inflexions, String pronunciation)
    {
        int random_id = idGenerator.nextInt();

        synchronized (this) {
            openWritableDatabase();
            super.insertReference(random_id, lang_id, name, inflexions.wrap(), pronunciation, DReference.INITIAL_PRIORITY, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
        return new DReference(random_id, name, pronunciation, inflexions, DReference.INITIAL_PRIORITY);
    }

    public void insertRemovedReference(int lang_id, int reference_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.insertRemovedReference(lang_id, reference_id, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
    }

    public DrawerReference insertDrawerReference(int lang_id, String note)
    {
        int random_id = idGenerator.nextInt();

        synchronized (this) {
            openWritableDatabase();
            super.insertDrawerReference(random_id, lang_id, note, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
        return new DrawerReference(random_id, note);
    }

    public Theme insertTheme(int lang_id, String name, ThemePairs pairs)
    {
        int random_id = idGenerator.nextInt();

        synchronized (this) {
            openWritableDatabase();
            super.insertTheme(random_id, lang_id, name, pairs, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
        return new Theme(random_id, name, pairs);
    }

    public Test insertTest(int lang_id, String name, DReferences references, int theme_id)
    {
        int random_id = idGenerator.nextInt();
        Test test = new Test(random_id, name, references, theme_id);

        synchronized (this) {
            openWritableDatabase();
            super.insertTest(random_id, lang_id, name, 0, Test.wrapContent(test), theme_id, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
        return test;
    }

    // **************************************************** \\
    ////////////////////// Updates \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void updateLanguage(Language lang)
    {
        synchronized (this) {
            openWritableDatabase();
            super.updateLanguage(lang.id, lang.code, lang.language_name, lang.settings, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
    }

    public void updateStatistics(Statistics statistics)
    {
        synchronized (this) {
            openWritableDatabase();
            super.updateStatistics(statistics.id, statistics, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
    }

    public void updateReference(DReference reference, int lang_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.updateReference(reference.id, lang_id, reference.name, reference.getInflexions().wrap(),
                    reference.pronunciation, reference.priority, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
    }

    public void updateReferencePriority(DReference reference, int lang_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.updateReferencePriority(reference.id, lang_id, reference.priority);
            closeWritableDatabase();
        }
    }

    public void updateTheme(Theme theme, int lang_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.updateTheme(theme.id, lang_id, theme.getName(), theme.getPairs(), Database.NOT_SYNCED);
            closeWritableDatabase();
        }
    }

    public void updateTest(Test test, int lang_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.updateTest(test.id, lang_id, test.name, test.getRunTimes(), Test.wrapContent(test), Database.NOT_SYNCED);
            closeWritableDatabase();
        }
    }

    // **************************************************** \\
    ////////////////////// Deletes \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void deleteLanguage(Language lang)
    {
        synchronized (this) {
            openWritableDatabase();
            super.deleteLanguage(lang.id);
            closeWritableDatabase();
        }
    }

    public void deleteReferenceTemporarily(int lang_id, int reference_id)
    {
        insertRemovedReference(lang_id, reference_id);
    }

    public void deleteAllRemovedReferences(int lang_id)
    {
        synchronized (this) {
            openWritableDatabase();

            TreeSet<Integer> ids = readRemovedReferencesIds(lang_id);

            db.delete(DBRemovedReference.db, Database.lang_id + " = " + lang_id, null);
            for (Integer reference_id : ids)
                deleteReference(lang_id, reference_id);

            closeWritableDatabase();
        }
    }

    public void deleteDrawerReference(int id, int lang_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.deleteDrawerReference(lang_id, id);
            closeWritableDatabase();
        }
    }

    public void deleteTheme(int id, int lang_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.deleteTheme(lang_id, id);
            closeWritableDatabase();
        }
    }

    public void deleteTest(int id, int lang_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.deleteTest(lang_id, id);
            closeWritableDatabase();
        }
    }

    // **************************************************** \\
    ////////////////////// Restores \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void restoreReference(int reference_id, int lang_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.deleteRemovedReference(lang_id, reference_id);
            closeWritableDatabase();
        }
    }

}
