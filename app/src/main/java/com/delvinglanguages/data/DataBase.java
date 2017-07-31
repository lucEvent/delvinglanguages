package com.delvinglanguages.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.kernel.DReference;
import com.delvinglanguages.kernel.DelvingList;
import com.delvinglanguages.kernel.DrawerReference;
import com.delvinglanguages.kernel.subject.Subject;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.util.RemovedItem;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.net.utils.SyncWrapper;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "delving.db";
    private static final int DATABASE_VERSION = 2;

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
    public static final String list_id = "lang_id";
    public static final String inflexions = "inflexions";
    public static final String pronunciation = "pronunciation";
    public static final String priority = "priority";
    public static final String runtimes = "runtimes";
    public static final String content = "content";
    public static final String subject_id = "theme_id";
    public static final String pairs = "pairs";
    public static final String type = "type";
    public static final String synced = "synced";
    public static final String wrappedContent = "wrappedContent";

    public static final int SYNCED = 1;
    public static final int NOT_SYNCED = 0;

    public static final class DBDelvingList {

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

        public static DelvingList parse(Cursor c)
        {
            int codes = c.getInt(1);
            return new DelvingList(c.getInt(0), codes & 0xFF, codes >> 16, c.getString(2), c.getInt(4));
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

        public static String[] cols = {id, list_id, name, pronunciation, inflexions, priority};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER," +
                        list_id + " INTEGER," +
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

    public static final class DBDrawerReference {

        public static String db = "drawerreference";

        public static String[] cols = {id, list_id, name};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER," +
                        list_id + " INTEGER," +
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

        public static String[] cols = {id, list_id, name, runtimes, content, subject_id};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER," +
                        list_id + " INTEGER," +
                        name + " TEXT NOT NULL," +
                        runtimes + " INTEGER," +
                        content + " TEXT NOT NULL," +
                        subject_id + " INTEGER," +
                        synced + " INTEGER" +
                        ");";

        public static Test parse(Cursor c)
        {
            return new Test(c.getInt(0), c.getString(2), c.getInt(3), c.getString(4), c.getInt(5));
        }

    }

    public static final class DBSubject {

        public static String db = "theme";

        public static String[] cols = {id, list_id, name, pairs};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER," +
                        list_id + " INTEGER," +
                        name + " TEXT NOT NULL," +
                        pairs + " TEXT NOT NULL," +
                        synced + " INTEGER" +
                        ");";

        public static Subject parse(Cursor c)
        {
            return new Subject(c.getInt(0), c.getString(2), c.getString(3));
        }

    }

    public static final class DBRemovedItem {

        public static String db = "removed_item";

        public static String[] cols = {id, list_id, type, wrappedContent};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER," +
                        list_id + " INTEGER," +
                        type + " INTEGER," +
                        wrappedContent + " TEXT NOT NULL," +
                        synced + " INTEGER" +
                        ");";

        public static RemovedItem parse(Cursor c)
        {
            return new RemovedItem(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3));
        }

    }

    public static final class DBDeletedItem {

        public static String db = "deleted_item";

        public static String[] cols = {id, list_id, type};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        id + " INTEGER," +
                        list_id + " INTEGER," +
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
        db.execSQL(DBDelvingList.creator);
        db.execSQL(DBStatistics.creator);
        db.execSQL(DBReference.creator);
        db.execSQL(DBDrawerReference.creator);
        db.execSQL(DBTest.creator);
        db.execSQL(DBSubject.creator);
        db.execSQL(DBRemovedItem.creator);
        db.execSQL(DBDeletedItem.creator);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        AppSettings.printlog("Upgrading DB from version: " + oldVersion + " to " + newVersion);

        switch (oldVersion) {
            case 1:
                db.execSQL("DROP TABLE removedreference");
                db.execSQL(DBRemovedItem.creator);
                db.execSQL(DBDeletedItem.creator);
        }
        // Delete all needed tables
        // db.execSQL("DROP TABLE "+ "db_languages");
        // Create all needed tables
        // db.execSQL(CREATE_SUBJECT);
    }

}