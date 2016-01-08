package com.delvinglanguages.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.delvinglanguages.kernel.Estadisticas;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.kernel.theme.ThemePair;
import com.delvinglanguages.settings.Settings;

public class DatabaseBackUpManager {

    private static final String EQ = " = ";

    private DataBase sqlite;

    private SQLiteDatabase db;

    private ContentValues values;

    public DatabaseBackUpManager(Context context) {
        sqlite = new DataBase(context);
        values = new ContentValues();
    }

    public void openWritableDatabase() {
        db = sqlite.getWritableDatabase();
        db.beginTransactionNonExclusive();
    }

    public void openReadableDatabase() {
        db = sqlite.getReadableDatabase();
        db.beginTransactionNonExclusive();
    }

    public void closeAndCancelDatabase() {
        db.endTransaction();
        db.close();
    }

    public void closeAndRollDatabase() {
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

        Language language = new Language(lang_id, code, name, Settings.NativeLanguage, settings);
        language.setStatistics(new Estadisticas((int) stats_id));
        return language;
    }

    public void insertStatistics(Estadisticas S) {
        values.put(DataBase.DBStatistics.tries, S.intentos);
        values.put(DataBase.DBStatistics.hits1, S.aciertos1);
        values.put(DataBase.DBStatistics.hits2, S.aciertos2);
        values.put(DataBase.DBStatistics.hits3, S.aciertos3);
        values.put(DataBase.DBStatistics.misses, S.fallos);
        db.update(DataBase.DBStatistics.db, values, DataBase.DBStatistics.id + EQ + S.id, null);
        values.clear();
    }

    public void insertWord(String name, String inflexions, int lang_id, String pronunciation, int priority) {
        values.put(DataBase.DBWord.name, name);
        values.put(DataBase.DBWord.lang_id, lang_id);
        values.put(DataBase.DBWord.inflexions, inflexions);
        values.put(DataBase.DBWord.pronunciation, pronunciation);
        values.put(DataBase.DBWord.priority, priority);
        db.insert(DataBase.DBWord.db, null, values);
        values.clear();
    }

    public void insertStoreWord(String note, int lang_id) {
        values.put(DataBase.DBDrawerWord.name, note);
        values.put(DataBase.DBDrawerWord.lang_id, lang_id);
        db.insert(DataBase.DBDrawerWord.db, null, values);
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

    public void insertTest(String name, String content, int lang_id) {
        values.put(DataBase.DBTest.name, name);
        values.put(DataBase.DBTest.lang_id, lang_id);
        values.put(DataBase.DBTest.content, content);
        db.insert(DataBase.DBTest.db, null, values);
        values.clear();
    }

}
