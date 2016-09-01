package com.delvinglanguages.server.db;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ItemHistoryRecord implements Comparable<ItemHistoryRecord> {

    public static final int EVENT_CREATED = 1;
    public static final int EVENT_UPDATED = 2;
    public static final int EVENT_DELETED = 3;

    @Id
    Long dbid;

    @Index
    public String user_id;

    @Index
    public long time;

    public int language_id;

    public int id;

    public int event;

    public int type;

    public ItemHistoryRecord()
    {
    }

    @Override
    public int compareTo(ItemHistoryRecord another)
    {
        return Long.compare(this.time, another.time);
    }

}
