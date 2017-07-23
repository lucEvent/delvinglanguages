package com.delvinglanguages.kernel.record;

import android.support.annotation.NonNull;

public abstract class Record implements Comparable<Record> {

    public static final int LANGUAGE_CREATED = 9000;
    public static final int LANGUAGE_REMOVED = 9001;
    public static final int LANGUAGE_INTEGRATED = 9002;
    public static final int LANGUAGE_CODE_CHANGED = 9003;
    public static final int LANGUAGE_NAME_CHANGED = 9004;
    public static final int LANGUAGE_PHRASAL_STATE_CHANGED = 9005;
    public static final int LANGUAGE_STATISTICS_CLEARED = 9006;

    public static final int DRAWERWORD_ADDED = 9010;
    public static final int DRAWERWORD_DELETED = 9011;

    public static final int REFERENCE_CREATED = 9020;
    public static final int REFERENCE_MODIFIED = 9021;
    public static final int REFERENCE_REMOVED = 9022;
    public static final int REFERENCE_RECOVERED = 9023;

    public static final int THEME_CREATED = 9030;
    public static final int THEME_MODIFIED = 9031;
    public static final int THEME_REMOVED = 9032;
    public static final int THEME_RECOVERED = 9033;

    public static final int TEST_CREATED = 9040;
    public static final int TEST_DONE = 9041;
    public static final int TEST_REMOVED = 9042;
    public static final int TEST_RECOVERED = 9043;

    public static final int PRACTISED_MATCH = 9050;
    public static final int PRACTISED_COMPLETE = 9051;
    public static final int PRACTISED_WRITE = 9052;
    public static final int PRACTISED_LISTENING = 9053;

    public static final int RECYCLE_BIN_CLEARED = 9060;

    public static final int APPSET_LANGUAGE_CHANGED = 9070;
    public static final int APPSET_KBVIBRATION_STATE_CHANGED = 9071;
    public static final int APPSET_THEME_CHANGED = 9072;
    public static final int APPSET_ONLINE_BACKUP_STATE_CHANGED = 9073;
    public static final int APPSET_IMPORT = 9074;
    public static final int APPSET_EXPORT = 9075;

    // Record fields
    public final int type;

    public long time;

    Record(int type, long time)
    {
        this.type = type;
        this.time = time;
    }

    @Override
    public int compareTo(@NonNull Record o)
    {
        return -Long.compare(this.time, o.time);
    }

}
