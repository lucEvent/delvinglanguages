package com.delvinglanguages.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.kernel.record.AppSettingsRecord;
import com.delvinglanguages.kernel.record.DelvingListRecord;
import com.delvinglanguages.kernel.record.DelvingListSettingsRecord;

public class RecordDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "delving_records.db";
    private static final int DATABASE_VERSION = 1;

    public static final String type = "type";
    public static final String list_id = "list_id";
    public static final String language_code = "language_code";
    public static final String item_ids = "item_ids";
    public static final String time = "time";
    public static final String oldValue = "oldValue";
    public static final String newValue = "newValue";
    public static final String _class = "_class";

    public static final class DBDelvingListRecord {

        public static String db = "lanrecord";

        public static String[] cols = {type, list_id, language_code, item_ids, time};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        type + " INTEGER," +
                        list_id + " INTEGER," +
                        language_code + " INTEGER," +
                        item_ids + " TEXT NOT NULL," +
                        time + " INTEGER" +
                        ");";

        public static DelvingListRecord parse(Cursor c)
        {
            return new DelvingListRecord(c.getInt(0), c.getInt(1), c.getInt(2), fromString(c.getString(3)), c.getLong(4));
        }

        private static int[] fromString(String string)
        {
            String[] strings = string.replace("[", "").replace("]", "").split(", ");
            int result[] = new int[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Integer.parseInt(strings[i]);

            return result;
        }

    }

    public static final class DBDelvingListSettingsRecord {

        public static String db = "setsrecord";

        public static String[] cols = {type, list_id, language_code, _class, oldValue, newValue, time};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        type + " INTEGER," +
                        list_id + " INTEGER," +
                        language_code + " INTEGER," +
                        _class + " TEXT NOT NULL," +
                        oldValue + " TEXT NOT NULL," +
                        newValue + " TEXT NOT NULL," +
                        time + " INTEGER" +
                        ");";

        public static DelvingListSettingsRecord parse(Cursor c)
        {
            String _class = c.getString(3);

            switch (_class) {
                case "String":
                    return new DelvingListSettingsRecord<String>(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(4), c.getString(5), c.getLong(6));
                case "Integer":
                    return new DelvingListSettingsRecord<Integer>(c.getInt(0), c.getInt(1), c.getInt(2), c.getInt(4), c.getInt(5), c.getLong(6));
                case "Boolean":
                    return new DelvingListSettingsRecord<Boolean>(c.getInt(0), c.getInt(1), c.getInt(2), Boolean.parseBoolean(c.getString(4)), Boolean.parseBoolean(c.getString(5)), c.getLong(6));
            }
            return null;
        }

    }

    public static final class DBAppSettingsRecord {

        public static String db = "appsetsrecord";

        public static String[] cols = {type, _class, newValue, time};

        public static String creator =
                "CREATE TABLE " + db + " (" +
                        type + " INTEGER," +
                        _class + " TEXT NOT NULL," +
                        newValue + " TEXT NOT NULL," +
                        time + " INTEGER" +
                        ");";

        public static AppSettingsRecord parse(Cursor c)
        {
            String _class = c.getString(1);

            switch (_class) {
                case "String":
                    return new AppSettingsRecord<String>(c.getInt(0), c.getString(2), c.getLong(3));
                case "Integer":
                    return new AppSettingsRecord<Integer>(c.getInt(0), c.getInt(2), c.getLong(3));
                case "Boolean":
                    return new AppSettingsRecord<Boolean>(c.getInt(0), Boolean.parseBoolean(c.getString(2)), c.getLong(3));
            }
            return null;
        }

    }

    public RecordDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DBDelvingListRecord.creator);
        db.execSQL(DBDelvingListSettingsRecord.creator);
        db.execSQL(DBAppSettingsRecord.creator);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        AppSettings.printlog("Upgrading RecordDB from version: " + oldVersion + " to " + newVersion);

        switch (oldVersion) {
            case 1:
                //     db.execSQL("DROP TABLE removedreference");
                //     db.execSQL(DBRemovedItem.creator);
        }
    }

}