package com.delvinglanguages.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.delvinglanguages.data.Database.DBDrawerReference;
import com.delvinglanguages.data.Database.DBReference;
import com.delvinglanguages.data.Database.DBTest;
import com.delvinglanguages.data.Database.DBTheme;

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

    public void updateReferenceLanguage(int id, int new_lang_id, int old_lang_id)
    {
        values.put(Database.lang_id, new_lang_id);
        db.update(DBReference.db, values, Database.lang_id + EQ + old_lang_id + AND + Database.id + EQ + id, null);
        values.clear();
    }

    public void updateDrawerReferenceLanguage(int id, int new_lang_id, int old_lang_id)
    {
        values.put(Database.lang_id, new_lang_id);
        db.update(DBDrawerReference.db, values, Database.lang_id + EQ + old_lang_id + AND + Database.id + EQ + id, null);
        values.clear();
    }

    public void updateThemeLanguage(int id, int new_lang_id, int old_lang_id)
    {
        values.put(Database.lang_id, new_lang_id);
        db.update(DBTheme.db, values, Database.lang_id + EQ + old_lang_id + AND + Database.id + EQ + id, null);
        values.clear();
    }

    public void updateTestLanguage(int id, int new_lang_id, int old_lang_id)
    {
        values.put(Database.lang_id, new_lang_id);
        db.update(DBTest.db, values, Database.lang_id + EQ + old_lang_id + AND + Database.id + EQ + id, null);
        values.clear();
    }

}
