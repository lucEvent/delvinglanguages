package com.delvinglanguages.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.delvinglanguages.data.Database.DBDrawerReference;
import com.delvinglanguages.data.Database.DBReference;
import com.delvinglanguages.data.Database.DBSubject;
import com.delvinglanguages.data.Database.DBTest;

public class MergeDatabaseManager {

    private static final String EQ = "=";
    private static final String AND = " AND ";

    private Database sqlite;

    private SQLiteDatabase db;
    private ContentValues values;

    public MergeDatabaseManager(Context context)
    {
        sqlite = new Database(context);
        values = new ContentValues();
    }

    public void openWritableDatabase()
    {
        db = sqlite.getWritableDatabase();
        db.beginTransactionNonExclusive();
    }

    public void closeWritableDatabase()
    {
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void updateReferenceLanguage(int id, int new_list_id, int old_list_id)
    {
        values.put(Database.list_id, new_list_id);
        db.update(DBReference.db, values, Database.list_id + EQ + old_list_id + AND + Database.id + EQ + id, null);
        values.clear();
    }

    public void updateDrawerReferenceLanguage(int id, int new_list_id, int old_list_id)
    {
        values.put(Database.list_id, new_list_id);
        db.update(DBDrawerReference.db, values, Database.list_id + EQ + old_list_id + AND + Database.id + EQ + id, null);
        values.clear();
    }

    public void updateSubjectLanguage(int id, int new_list_id, int old_list_id)
    {
        values.put(Database.list_id, new_list_id);
        db.update(DBSubject.db, values, Database.list_id + EQ + old_list_id + AND + Database.id + EQ + id, null);
        values.clear();
    }

    public void updateTestLanguage(int id, int new_list_id, int old_list_id)
    {
        values.put(Database.list_id, new_list_id);
        db.update(DBTest.db, values, Database.list_id + EQ + old_list_id + AND + Database.id + EQ + id, null);
        values.clear();
    }

}
