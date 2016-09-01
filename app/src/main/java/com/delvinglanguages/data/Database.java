package com.delvinglanguages.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.net.utils.SyncWrapper;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "delving.db";
    private static final int DATABASE_VERSION = 1;

    public static final String id = "_id";
    public static final String code = "code";
    public static final String name = "name";
    public static final String statistics_id = "statistics";
    public static final String settings = "settings";
    public static final String attempts = "attempts";
    public static final String hits1 = "hits1";
    public static final String hits2 = "hits2";
    public static final String hits3 = "hits3";
    public static final String misses = "misses";
    public static final String lang_id = "lang_id";
    public static final String inflexions = "inflexions";
    public static final String pronunciation = "pronunciation";
    public static final String priority = "priority";
    public static final String reference_id = "reference_id";
    public static final String runtimes = "runtimes";
    public static final String content = "content";
    public static final String theme_id = "theme_id";
    public static final String pairs = "pairs";
    public static final String type = "type";
    public static final String synced = "synced";

    public static final int SYNCED = 1;
    public static final int NOT_SYNCED = 0;


    public static final class DBLanguage {

        public static String db = "language";

        public static String[] cols = {id, code, name, statistics_id, settings};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER," +
                        code + " INTEGER," +
                        name + " TEXT NOT NULL," +
                        statistics_id + " INTEGER," +
                        settings + " INTEGER," +
                        synced + " INTEGER" +
                        ");";

        public static Language parse(Cursor c)
        {
            return new Language(c.getInt(0), c.getInt(1), c.getString(2), c.getInt(4));
        }

    }

    public static final class DBStatistics {

        public static String db = "statistics";

        public static String[] cols = {id, attempts, hits1, hits2, hits3, misses};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER," +
                        attempts + " INTEGER," +
                        hits1 + " INTEGER," +
                        hits2 + " INTEGER," +
                        hits3 + " INTEGER," +
                        misses + " INTEGER," +
                        synced + " INTEGER" +
                        ");";

        public static Statistics parse(Cursor c)
        {
            return new Statistics(c.getInt(0), c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));
        }

    }

    public static final class DBReference {

        public static String db = "reference";

        public static String[] cols = {id, lang_id, name, pronunciation, inflexions, priority};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER," +
                        lang_id + " INTEGER," +
                        name + " TEXT NOT NULL," +
                        pronunciation + " TEXT NOT NULL," +
                        inflexions + " TEXT NOT NULL," +
                        priority + " INTEGER," +
                        synced + " INTEGER" +
                        ");";

        public static DReference parse(Cursor c)
        {
            return new DReference(c.getInt(0), c.getString(2), c.getString(3), c.getString(4), c.getInt(5));
        }

    }

    public static final class DBRemovedReference {

        public static String db = "removedreference";

        public static String[] cols = {lang_id, reference_id};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        lang_id + " INTEGER," +
                        reference_id + " INTEGER," +
                        synced + " INTEGER" +
                        ");";

        public static int parse(Cursor c)
        {
            return c.getInt(1);
        }

    }

    public static final class DBDrawerReference {

        public static String db = "drawerreference";

        public static String[] cols = {id, lang_id, name};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER," +
                        lang_id + " INTEGER," +
                        name + " TEXT NOT NULL," +
                        synced + " INTEGER" +
                        ");";

        public static DrawerReference parse(Cursor c)
        {
            return new DrawerReference(c.getInt(0), c.getString(2));
        }

    }

    public static final class DBTest {

        public static String db = "test";

        public static String[] cols = {id, lang_id, name, runtimes, content, theme_id};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER," +
                        lang_id + " INTEGER," +
                        name + " TEXT NOT NULL," +
                        runtimes + " INTEGER," +
                        content + " TEXT NOT NULL," +
                        theme_id + " INTEGER," +
                        synced + " INTEGER" +
                        ");";

        public static Test parse(Cursor c)
        {
            return new Test(c.getInt(0), c.getString(2), c.getInt(3), c.getString(4), c.getInt(5));
        }

    }

    public static final class DBTheme {

        public static String db = "theme";

        public static String[] cols = {id, lang_id, name, pairs};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER," +
                        lang_id + " INTEGER," +
                        name + " TEXT NOT NULL," +
                        pairs + " TEXT NOT NULL," +
                        synced + " INTEGER" +
                        ");";

        public static Theme parse(Cursor c)
        {
            return new Theme(c.getInt(0), c.getString(2), c.getString(3));
        }

    }

    public static final class DBItemeRemoved {

        public static String db = "item_removed";

        public static String[] cols = {id, lang_id, type};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER," +
                        lang_id + " INTEGER," +
                        type + " INTEGER" +
                        ");";

        public static SyncWrapper parse(Cursor c)
        {
            return new SyncWrapper(c.getInt(0), c.getInt(1), c.getInt(2), null);
        }

    }

    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DBLanguage.creator);
        db.execSQL(DBStatistics.creator);
        db.execSQL(DBReference.creator);
        db.execSQL(DBRemovedReference.creator);
        db.execSQL(DBDrawerReference.creator);
        db.execSQL(DBTest.creator);
        db.execSQL(DBTheme.creator);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        AppSettings.printlog("Upgrading DB from version: " + oldVersion + " to " + newVersion);

        // Delete all needed tables
        // db.execSQL("DROP TABLE "+ "db_languages");
        // Create all needed tables
        // db.execSQL(CREATE_THEME);
    }

}