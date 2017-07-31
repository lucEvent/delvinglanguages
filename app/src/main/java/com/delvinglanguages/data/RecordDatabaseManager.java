package com.delvinglanguages.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.delvinglanguages.data.RecordDatabase.DBAppSettingsRecord;
import com.delvinglanguages.data.RecordDatabase.DBDelvingListRecord;
import com.delvinglanguages.data.RecordDatabase.DBDelvingListSettingsRecord;
import com.delvinglanguages.kernel.record.AppSettingsRecord;
import com.delvinglanguages.kernel.record.DelvingListRecord;
import com.delvinglanguages.kernel.record.DelvingListSettingsRecord;
import com.delvinglanguages.kernel.record.Record;
import com.delvinglanguages.kernel.record.Records;

import java.util.Arrays;
import java.util.TreeSet;

public class RecordDatabaseManager {

    private static final String EQ = "=";

    private RecordDatabase scheme;

    protected ContentValues values;

    public RecordDatabaseManager(Context context)
    {
        scheme = new RecordDatabase(context);
        values = new ContentValues();
    }

    // **************************************************** \\
    /////////////////////// Reads \\\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public Records readRecords()
    {
        TreeSet<Record> result = new TreeSet<>();

        SQLiteDatabase db = scheme.getReadableDatabase();
        Cursor cursor = db.query(DBDelvingListRecord.db, DBDelvingListRecord.cols, null, null, null, null, null);

        if (cursor.moveToFirst())
            do {
                result.add(DBDelvingListRecord.parse(cursor));
            } while (cursor.moveToNext());

        cursor.close();

        cursor = db.query(DBDelvingListSettingsRecord.db, DBDelvingListSettingsRecord.cols, null, null, null, null, null);

        if (cursor.moveToFirst())
            do {
                result.add(DBDelvingListSettingsRecord.parse(cursor));
            } while (cursor.moveToNext());

        cursor.close();

        cursor = db.query(DBAppSettingsRecord.db, DBAppSettingsRecord.cols, null, null, null, null, null);

        if (cursor.moveToFirst())
            do {
                result.add(DBAppSettingsRecord.parse(cursor));
            } while (cursor.moveToNext());

        cursor.close();
        db.close();

        return new Records(result);
    }


    // **************************************************** \\
    ////////////////////// Inserts \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public DelvingListRecord insertDelvingListRecord(int type, int list_id, int language_code, int[] item_ids)
    {
        long time = System.currentTimeMillis();

        values.put(RecordDatabase.type, type);
        values.put(RecordDatabase.list_id, list_id);
        values.put(RecordDatabase.language_code, language_code);
        values.put(RecordDatabase.item_ids, Arrays.toString(item_ids));
        values.put(RecordDatabase.time, time);
        insert(DBDelvingListRecord.db, values);
        values.clear();

        return new DelvingListRecord(type, list_id, language_code, item_ids, time);
    }

    public DelvingListSettingsRecord insertDelvingListSettingsRecord(int type, int list_id, int language_code, Object oldValue, Object newValue)
    {
        long time = System.currentTimeMillis();

        values.put(RecordDatabase.type, type);
        values.put(RecordDatabase.list_id, list_id);
        values.put(RecordDatabase.language_code, language_code);
        values.put(RecordDatabase._class, oldValue.getClass().getSimpleName());
        values.put(RecordDatabase.oldValue, oldValue.toString());
        values.put(RecordDatabase.newValue, newValue.toString());
        values.put(RecordDatabase.time, time);
        insert(DBDelvingListSettingsRecord.db, values);
        values.clear();

        return new DelvingListSettingsRecord<>(type, list_id, language_code, oldValue, newValue, time);
    }

    public AppSettingsRecord insertAppSettingsRecord(int type, Object value)
    {
        long time = System.currentTimeMillis();

        values.put(RecordDatabase.type, type);
        values.put(RecordDatabase._class, value.getClass().getSimpleName());
        values.put(RecordDatabase.newValue, value.toString());
        values.put(RecordDatabase.time, time);
        insert(DBAppSettingsRecord.db, values);
        values.clear();

        return new AppSettingsRecord<>(type, value, time);
    }

    private void insert(String table, ContentValues values)
    {
        SQLiteDatabase db = scheme.getWritableDatabase();
        db.insert(table, null, values);
        db.close();
    }

    // **************************************************** \\
    ////////////////////// Updates \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void updateDelvingListRecord(DelvingListRecord record)
    {
        long newTime = System.currentTimeMillis();

        values.put(RecordDatabase.language_code, record.language_code);
        values.put(RecordDatabase.item_ids, Arrays.toString(record.item_ids));
        values.put(RecordDatabase.time, newTime);
        update(DBDelvingListRecord.db, values, RecordDatabase.time + EQ + record.time);
        values.clear();

        record.time = newTime;
    }

    public void updateDelvingListSettingsRecord(DelvingListSettingsRecord record)
    {
        long newTime = System.currentTimeMillis();

        values.put(RecordDatabase.oldValue, record.oldValue.toString());
        values.put(RecordDatabase.newValue, record.newValue.toString());
        values.put(RecordDatabase.time, newTime);
        update(DBDelvingListSettingsRecord.db, values, RecordDatabase.time + EQ + record.time);
        values.clear();

        record.time = newTime;
    }

    public void updateAppSettingsRecord(AppSettingsRecord record)
    {
        long newTime = System.currentTimeMillis();

        values.put(RecordDatabase.newValue, record.value.toString());
        values.put(RecordDatabase.time, newTime);
        update(DBAppSettingsRecord.db, values, RecordDatabase.time + EQ + record.time);
        values.clear();

        record.time = newTime;
    }

    private void update(String table, ContentValues values, String whereClause)
    {
        SQLiteDatabase db = scheme.getWritableDatabase();
        db.update(table, values, whereClause, null);
        db.close();
    }

    // **************************************************** \\
    ////////////////////// Deletes \\\\\\\\\\\\\\\\\\\\\\\\\\\
    // **************************************************** \\

    public void clearDatabase()
    {
        SQLiteDatabase db = scheme.getWritableDatabase();
        db.delete(DBDelvingListRecord.db, null, null);
        db.delete(DBDelvingListSettingsRecord.db, null, null);
        db.delete(DBAppSettingsRecord.db, null, null);
        db.close();
    }

}
