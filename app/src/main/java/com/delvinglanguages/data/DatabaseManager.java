package com.delvinglanguages.data;

import android.content.Context;
import android.database.Cursor;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.data.Database.DBTest;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.Usage;
import com.delvinglanguages.kernel.subject.Subject;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DelvingLists;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.kernel.util.RemovedItem;
import com.delvinglanguages.kernel.util.RemovedItems;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.Subjects;
import com.delvinglanguages.kernel.util.Tests;
import com.delvinglanguages.kernel.util.Usages;
import com.delvinglanguages.kernel.util.Wrapper;

import java.util.Random;

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

    @Override
    public DelvingLists readLists()
    {
        DelvingLists result;
        synchronized (this) {
            openReadableDatabase();
            result = super.readLists();
            closeReadableDatabase();
        }
        AppSettings.printlog("# DelvingLists in DB: " + result.size());
        return result;
    }

    @Override
    public DReferences readReferences(int list_id)
    {
        DReferences result;
        synchronized (this) {
            openReadableDatabase();
            result = super.readReferences(list_id);
            closeReadableDatabase();
        }
        AppSettings.printlog("# References in DB: " + result.size());
        return result;
    }

    public DReferences readReferences(int list_id, int[] ids)
    {
        DReferences result = new DReferences(ids.length);
        synchronized (this) {
            openReadableDatabase();

            for (int i = 0; i < ids.length; i++) {
                DReference ref = super.readReference(list_id, ids[i]);
                if (ref != null)
                    result.add(ref);
            }

            closeReadableDatabase();
        }
        return result;
    }

    @Override
    public DrawerReferences readDrawerReferences(int list_id)
    {
        DrawerReferences result;
        synchronized (this) {
            openReadableDatabase();
            result = super.readDrawerReferences(list_id);
            closeReadableDatabase();
        }
        AppSettings.printlog("# DrawerReferences in DB: " + result.size());
        return result;
    }

    @Override
    public RemovedItems readRemovedItems(int list_id)
    {
        RemovedItems result;
        synchronized (this) {
            openReadableDatabase();
            result = super.readRemovedItems(list_id);
            closeReadableDatabase();
        }
        AppSettings.printlog("# RemovedReferences in DB: " + result.size());
        return result;
    }

    @Override
    public Subjects readSubjects(int list_id)
    {
        Subjects result;
        synchronized (this) {
            openReadableDatabase();
            result = super.readSubjects(list_id);
            closeReadableDatabase();
        }
        AppSettings.printlog("# Subjects in DB: " + result.size());
        return result;
    }

    @Override
    public Tests readTests(int list_id)
    {
        Tests result;
        synchronized (this) {
            openReadableDatabase();
            result = super.readTests(list_id);
            closeReadableDatabase();
        }
        AppSettings.printlog("# Tests in DB: " + result.size());
        return result;
    }

    public Test readTestFrom(int from_id)
    {
        Test res = null;
        synchronized (this) {
            Cursor cursor = openReadableDatabase().query(DBTest.db, DBTest.cols, Database.from_id + "=" + from_id, null, null, null, Database.name + " ASC");

            if (cursor.moveToFirst())
                res = DBTest.parse(cursor);

            cursor.close();
            closeReadableDatabase();
        }
        return res;
    }

    @Override
    public Usages readUsages(int list_id, int reference_id, Usages usages)
    {
        Usages res;
        synchronized (this) {
            openReadableDatabase();
            res = super.readUsages(list_id, reference_id, usages);
            closeReadableDatabase();
        }
        return res;
    }

    // **************************************************** \\
    ////////////////////// Inserts \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public DelvingList insertDelvingList(int from_code, int to_code, String name, int settings)
    {
        int random_id = idGenerator.nextInt();

        synchronized (this) {
            openWritableDatabase();
            random_id = super.insertDelvingList(random_id, from_code, to_code, name, settings, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
        return new DelvingList(random_id, from_code, to_code, name, settings, new Statistics(random_id));
    }

    public DReference insertReference(int list_id, String name, Inflexions inflexions, String pronunciation)
    {
        int random_id = idGenerator.nextInt();

        synchronized (this) {
            openWritableDatabase();
            super.insertReference(random_id, list_id, name, inflexions.wrap(), pronunciation, DReference.INITIAL_PRIORITY, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
        return new DReference(random_id, name, pronunciation, inflexions, DReference.INITIAL_PRIORITY);
    }

    public DrawerReference insertDrawerReference(int list_id, String note)
    {
        int random_id = idGenerator.nextInt();

        synchronized (this) {
            openWritableDatabase();
            super.insertDrawerReference(random_id, list_id, note, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
        return new DrawerReference(random_id, note);
    }

    public Subject insertSubject(int list_id, String name, DReferences references)
    {
        int random_id = idGenerator.nextInt();
        Subject res = new Subject(random_id, name, references);

        synchronized (this) {
            openWritableDatabase();
            super.insertSubject(random_id, list_id, name, res.wrapReferencesIds(), Database.NOT_SYNCED);
            closeWritableDatabase();
        }
        return res;
    }

    public Test insertTest(int list_id, String name, DReferences references, int from_id)
    {
        int random_id = idGenerator.nextInt();
        Test test = new Test(random_id, name, references, from_id);

        synchronized (this) {
            openWritableDatabase();
            super.insertTest(random_id, list_id, name, 0, Test.wrapContent(test), from_id, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
        return test;
    }

    public void insertUsages(int list_id, int reference_id, Usages usages)
    {
        synchronized (this) {
            openWritableDatabase();
            for (Usage usage : usages.values())
                super.insertUsage(list_id, reference_id, usage, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
    }

    // **************************************************** \\
    ////////////////////// Updates \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void updateDelvingList(DelvingList l)
    {
        synchronized (this) {
            openWritableDatabase();
            super.updateDelvingList(l.id, l.from_code, l.to_code, l.name, l.settings, Database.NOT_SYNCED);
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

    public void updateReference(DReference reference, int list_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.updateReference(reference.id, list_id, reference.name, reference.getInflexions().wrap(),
                    reference.pronunciation, reference.priority, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
    }

    public void updateReferencePriority(DReference reference, int list_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.updateReferencePriority(reference.id, list_id, reference.priority);
            closeWritableDatabase();
        }
    }

    public void updateSubject(Subject subject, int list_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.updateSubject(subject.id, list_id, subject.getName(), subject.wrapReferencesIds(), Database.NOT_SYNCED);
            closeWritableDatabase();
        }
    }

    public void updateTest(Test test, int list_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.updateTest(test.id, list_id, test.name, test.getRunTimes(), Test.wrapContent(test), Database.NOT_SYNCED);
            closeWritableDatabase();
        }
    }

    // **************************************************** \\
    ////////////////////// Deletes \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void deleteDelvingList(DelvingList list)
    {
        synchronized (this) {
            openWritableDatabase();
            super.deleteDelvingList(list.id);
            closeWritableDatabase();
        }
    }

    public void removeReference(int list_id, DReference reference)
    {
        synchronized (this) {
            openWritableDatabase();
            super.deleteReference(list_id, reference.id);
            super.insertRemovedItem(reference.id, list_id, reference, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
    }

    @Override
    public void deleteDrawerReference(int id, int list_id)
    {
        synchronized (this) {
            openWritableDatabase();
            super.deleteDrawerReference(list_id, id);
            closeWritableDatabase();
        }
    }

    public void removeSubject(int list_id, Subject subject)
    {
        synchronized (this) {
            openWritableDatabase();
            super.deleteSubject(list_id, subject.id);
            super.insertRemovedItem(subject.id, list_id, subject, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
    }

    public void removeTest(int list_id, Test test)
    {
        synchronized (this) {
            openWritableDatabase();
            super.deleteTest(list_id, test.id);
            super.insertRemovedItem(test.id, list_id, test, Database.NOT_SYNCED);
            closeWritableDatabase();
        }
    }

    @Override
    public void deleteRemovedItem(int list_id, int item_id, int type)
    {
        synchronized (this) {
            openWritableDatabase();
            super.deleteRemovedItem(list_id, item_id, type);
            if (type == Wrapper.TYPE_REFERENCE)
                super.deleteAllUsagesOf(list_id, item_id);
            closeWritableDatabase();
        }
    }

    public void deleteAllRemovedItems(int list_id, RemovedItems removedItems)
    {
        synchronized (this) {
            openWritableDatabase();
            for (RemovedItem ri : removedItems)
                if (ri.wrap_type == Wrapper.TYPE_REFERENCE)
                    super.deleteAllUsagesOf(list_id, ri.id);
            super.deleteAllRemovedItems(list_id);
            closeWritableDatabase();
        }
    }

    public void deleteUsages(int list_id, int reference_id, Usages usages)
    {
        synchronized (this) {
            openWritableDatabase();
            for (Usage usage : usages.values())
                super.deleteUsages(list_id, reference_id, usage.translation);
            closeWritableDatabase();
        }
    }

    // **************************************************** \\
    ////////////////////// Restores \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void restoreRemovedItem(int list_id, RemovedItem removedItem)
    {
        synchronized (this) {
            openWritableDatabase();
            super.deleteRemovedItem(list_id, removedItem.id, removedItem.wrap_type);

            switch (removedItem.wrap_type) {
                case Wrapper.TYPE_REFERENCE:
                    DReference reference = removedItem.castToReference();
                    super.insertReference(reference.id, list_id, reference.name, reference.getInflexions().wrap(), reference.pronunciation, reference.priority, Database.NOT_SYNCED);
                    break;
                case Wrapper.TYPE_SUBJECT:
                    Subject subject = removedItem.castToSubject();
                    super.insertSubject(subject.id, list_id, subject.getName(), subject.wrapReferencesIds(), Database.NOT_SYNCED);
                    break;
                case Wrapper.TYPE_TEST:
                    Test test = removedItem.castToTest();
                    super.insertTest(test.id, list_id, test.name, test.getRunTimes(), Test.wrapContent(test), test.from_id, Database.NOT_SYNCED);
                    break;
            }
            closeWritableDatabase();
        }
    }

}
