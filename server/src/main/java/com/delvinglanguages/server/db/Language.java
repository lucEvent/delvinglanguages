package com.delvinglanguages.server.db;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Language {

    @Id
    public Long dbid;

    @Index
    public String user_id;

    @Index
    public int id;

    @Index
    public int code;

    public int settings;

    public String name;

    public int nWords, nDrawerWords, nThemes, nTests;

    @Index
    public boolean isPublic;

}
