package com.delvinglanguages.server.db;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class LanguageItem {

    public static final int TYPE_LANGUAGE = 1;
    public static final int TYPE_REFERENCE = 2;
    public static final int TYPE_DRAWER_REFERENCE = 3;
    public static final int TYPE_THEME = 4;
    public static final int TYPE_TEST = 5;
    public static final int TYPE_STATISTICS = 6;
    public static final int TYPE_THEME_PAIRS = 7;
    public static final int TYPE_INFLEXIONS = 8;

    @Id
    public Long dbid;

    @Index
    public String user_id;

    @Index
    public int language_id;

    @Index
    public int id;

    @Index
    public int type;

    public String wrapper;

}
