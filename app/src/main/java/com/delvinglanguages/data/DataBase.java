package com.delvinglanguages.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.delvinglanguages.settings.Settings;

public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "delving.db";
    private static final int DATABASE_VERSION = 1;

    // Database SQL CREATES
    public static final class DBStatistics {
        public static String db = "statistics";

        public static String id = "_id";
        public static String tries = "tries";
        public static String hits1 = "hits1";
        public static String hits2 = "hits2";
        public static String hits3 = "hits3";
        public static String misses = "misses";

        public static String[] cols = {id, tries, hits1, hits2, hits3, misses};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        tries + " INTEGER," +
                        hits1 + " INTEGER," +
                        hits2 + " INTEGER," +
                        hits3 + " INTEGER," +
                        misses + " INTEGER" +
                ");";
    }

    public static final class DBLanguage {
        public static String db = "language";

        public static String id = "_id";
        public static String code = "code";
        public static String name = "name";
        public static String statistics = "statistics";
        public static String settings = "settings";

        public static String[] cols = {id, code, name, statistics, settings};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        code + " INTEGER," +
                        name + " TEXT NOT NULL," +
                        statistics + " INTEGER," +
                        settings + " INTEGER," +
                        "FOREIGN KEY(" + statistics + ") REFERENCES " + DBStatistics.db + "(" + DBStatistics.id + ")" +
                ");";
    }

    public static final class DBWord {
        public static String db = "word";

        public static String id = "_id";
        public static String name = "name";
        public static String lang_id = "language";
        public static String inflexions = "inflexions";
        public static String pronunciation = "pronunciation";
        public static String priority = "priority";

        public static String[] cols = {id, name, lang_id, inflexions, pronunciation, priority};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        name + " TEXT NOT NULL," +
                        lang_id + " INTEGER," +
                        inflexions + " TEXT NOT NULL," +
                        pronunciation + " TEXT NOT NULL," +
                        priority + " INTEGER," +
                        "FOREIGN KEY(" + lang_id + ") REFERENCES " + DBLanguage.db + "(" + DBLanguage.id + ")" +
                ");";
    }

    public static final class DBRemovedWord {
        public static String db = "removedword";

        public static String id = "_id";
        public static String lang_id = "language";
        public static String word_id = "word_id";

        public static String[] cols = {id, lang_id, word_id};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        lang_id + " INTEGER," +
                        word_id + " INTEGER," +
                        "FOREIGN KEY(" + word_id + ") REFERENCES " + DBWord.db + "(" + DBWord.id + ")" +
                        "FOREIGN KEY(" + lang_id + ") REFERENCES " + DBLanguage.db + "(" + DBLanguage.id + ")" +
                ");";
    }

    public static final class DBDrawerWord {
        public static String db = "drawerword";

        public static String id = "_id";
        public static String lang_id = "lang_id";
        public static String name = "name";

        public static String[] cols = {id, lang_id, name};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        lang_id + " INTEGER," +
                        name + " TEXT NOT NULL," +
                        "FOREIGN KEY(" + lang_id + ") REFERENCES " + DBLanguage.db + "(" + DBLanguage.id + ")" +
                ");";
    }

    public static final class DBTest {
        public static String db = "test";

        public static String id = "_id";
        public static String name = "name";
        public static String lang_id = "lang_id";
        public static String content = "content";

        public static String[] cols = {id, name, lang_id, content};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        name + " TEXT NOT NULL," +
                        lang_id + " INTEGER," +
                        content + " TEXT NOT NULL," +
                        "FOREIGN KEY(" + lang_id + ") REFERENCES " + DBLanguage.db + "(" + DBLanguage.id + ")" +
                ");";
    }

    public static final class DBTheme {
        public static String db = "theme";

        public static String id = "_id";
        public static String lang_id = "lang_id";
        public static String name = "name";

        public static String[] cols = {id, lang_id, name};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        lang_id + " INTEGER," +
                        name + " TEXT NOT NULL," +
                        "FOREIGN KEY(" + lang_id + ") REFERENCES " + DBLanguage.db + "(" + DBLanguage.id + ")" +
                ");";
    }

    public static final class DBThemePair {
        public static String db = "themepair";

        public static String id = "_id";
        public static String theme_id = "theme_id";
        public static String in_delv = "in_delv";
        public static String in_nativ = "in_nativ";

        public static String[] cols = {id, theme_id, in_delv, in_nativ};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        theme_id + " INTEGER," +
                        in_delv + " TEXT NOT NULL," +
                        in_nativ + " TEXT NOT NULL," +
                        "FOREIGN KEY(" + theme_id + ") REFERENCES " + DBTheme.db + "(" + DBTheme.id + ")" +
                ");";
    }

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        debug("En onCreate");

        db.execSQL(DBStatistics.creator);
        db.execSQL(DBLanguage.creator);
        db.execSQL(DBWord.creator);
        db.execSQL(DBRemovedWord.creator);
        db.execSQL(DBDrawerWord.creator);
        db.execSQL(DBTest.creator);
        db.execSQL(DBTheme.creator);
        db.execSQL(DBThemePair.creator);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        debug("Upgrading DB from VERS: " + oldVersion + " to " + newVersion);

        // Delete all needed tables
        // db.execSQL("DROP TABLE "+ "db_tiempo_verbal");
        // Create all needed tables
        // db.execSQL(CREATE_THEME);
    }

    private void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##DataBase##", text);
    }
}