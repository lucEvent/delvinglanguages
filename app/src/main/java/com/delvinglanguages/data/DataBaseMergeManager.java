package com.delvinglanguages.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.delvinglanguages.kernel.util.Inflexions;

public class DataBaseMergeManager {

    private static final String EQ = "=";

    private DataBase sqlite;

    private SQLiteDatabase db;

    private ContentValues values;

    public DataBaseMergeManager(Context context) {
        sqlite = new DataBase(context);
        values = new ContentValues();
    }

    public void openWritableDatabase() {
        db = sqlite.getWritableDatabase();
        db.beginTransactionNonExclusive();
    }

    public void closeWritableDatabase() {
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void updateReferenceLanguage(int reference_id, int language_id) {
        values.put(DataBase.DBReference.lang_id, language_id);

        db.update(DataBase.DBReference.db, values, DataBase.DBReference.id + EQ + reference_id, null);

        values.clear();
    }

    public void updateReferenceInflexions(int reference_id, Inflexions inflexions) {
        values.put(DataBase.DBReference.inflexions, inflexions.toString());

        db.update(DataBase.DBReference.db, values, DataBase.DBReference.id + EQ + reference_id, null);

        values.clear();
    }

    public void updateDrawerReferenceLanguage(int dreference_id, int language_id) {
        values.put(DataBase.DBDrawerReference.lang_id, language_id);

        db.update(DataBase.DBDrawerReference.db, values, DataBase.DBDrawerReference.id + EQ + dreference_id, null);

        values.clear();
    }

    public void updateThemeLanguage(int theme_id, int language_id) {
        values.put(DataBase.DBTheme.lang_id, language_id);

        db.update(DataBase.DBTheme.db, values, DataBase.DBTheme.id + EQ + theme_id, null);

        values.clear();
    }

    public void updateTestLanguage(int test_id, int language_id) {
        values.put(DataBase.DBTest.lang_id, language_id);

        db.update(DataBase.DBTest.db, values, DataBase.DBTest.id + EQ + test_id, null);

        values.clear();
    }
}
