package com.delvinglanguages.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.delvinglanguages.data.RecordDatabase.DBAppSettingsRecord;
import com.delvinglanguages.data.RecordDatabase.DBLanguageRecord;
import com.delvinglanguages.data.RecordDatabase.DBLanguageSettingsRecord;
import com.delvinglanguages.kernel.record.AppSettingsRecord;
import com.delvinglanguages.kernel.record.LanguageRecord;
import com.delvinglanguages.kernel.record.LanguageSettingsRecord;
import com.delvinglanguages.kernel.record.Record;
import com.delvinglanguages.kernel.record.Records;

import java.util.Arrays;
import java.util.TreeSet;

public class RecordDatabaseManager {

    private static final String EQ = "=";
    private static final String AND = " AND ";

    private RecordDatabase scheme;

    private SQLiteDatabase db;

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
        Cursor cursor = db.query(DBLanguageRecord.db, DBLanguageRecord.cols, null, null, null, null, null);

        if (cursor.moveToFirst())
            do {
                result.add(DBLanguageRecord.parse(cursor));
            } while (cursor.moveToNext());

        cursor.close();

        cursor = db.query(DBLanguageSettingsRecord.db, DBLanguageSettingsRecord.cols, null, null, null, null, null);

        if (cursor.moveToFirst())
            do {
                result.add(DBLanguageSettingsRecord.parse(cursor));
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

    public LanguageRecord insertLanguageRecord(int type, int language_id, int language_code, int[] item_ids)
    {
        long time = System.currentTimeMillis();

        values.put(RecordDatabase.type, type);
        values.put(RecordDatabase.language_id, language_id);
        values.put(RecordDatabase.language_code, language_code);
        values.put(RecordDatabase.item_ids, Arrays.toString(item_ids));
        values.put(RecordDatabase.time, time);
        insert(DBLanguageRecord.db, values);
        values.clear();

        return new LanguageRecord(type, language_id, language_code, item_ids, time);
    }

    public LanguageSettingsRecord insertLanguageSettingsRecord(int type, int language_id, int language_code, Object oldValue, Object newValue)
    {
        long time = System.currentTimeMillis();

        values.put(RecordDatabase.type, type);
        values.put(RecordDatabase.language_id, language_id);
        values.put(RecordDatabase.language_code, language_code);
        values.put(RecordDatabase._class, oldValue.getClass().getSimpleName());
        values.put(RecordDatabase.oldValue, oldValue.toString());
        values.put(RecordDatabase.newValue, newValue.toString());
        values.put(RecordDatabase.time, time);
        insert(DBLanguageSettingsRecord.db, values);
        values.clear();

        return new LanguageSettingsRecord<>(type, language_id, language_code, oldValue, newValue, time);
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

    public void updateLanguageRecord(LanguageRecord record)
    {
        long newTime = System.currentTimeMillis();

        values.put(RecordDatabase.language_code, record.language_code);
        values.put(RecordDatabase.item_ids, Arrays.toString(record.item_ids));
        values.put(RecordDatabase.time, newTime);
        update(DBLanguageRecord.db, values, RecordDatabase.time + EQ + record.time);
        values.clear();

        record.time = newTime;
    }

    public void updateLanguageSettingsRecord(LanguageSettingsRecord record)
    {
        long newTime = System.currentTimeMillis();

        values.put(RecordDatabase.oldValue, record.oldValue.toString());
        values.put(RecordDatabase.newValue, record.newValue.toString());
        values.put(RecordDatabase.time, newTime);
        update(DBLanguageSettingsRecord.db, values, RecordDatabase.time + EQ + record.time);
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
        db.delete(DBLanguageRecord.db, null, null);
        db.delete(DBLanguageSettingsRecord.db, null, null);
        db.delete(DBAppSettingsRecord.db, null, null);
        db.close();
    }

}
