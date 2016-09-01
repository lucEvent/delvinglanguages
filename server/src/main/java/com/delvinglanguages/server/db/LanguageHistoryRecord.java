package com.delvinglanguages.server.db;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import javax.annotation.Nonnull;

@Entity
public class LanguageHistoryRecord implements Comparable<LanguageHistoryRecord> {

    @Id
    Long dbid;

    @Index
    public String user_id;

    @Index
    public long time;

    public int language_id;

    public int event;

    public LanguageHistoryRecord()
    {
    }

    @Override
    public int compareTo(@Nonnull LanguageHistoryRecord another)
    {
        return Long.compare(this.time, another.time);
    }

}
