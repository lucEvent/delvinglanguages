package com.delvinglanguages.net.utils;

public class SyncWrapper {

    public int id;

    public int language_id;

    public int wrap_type;

    public String wrapper;

    public SyncWrapper(int id, int lang_id, int wrap_type, String wrapper)
    {
        this.id = id;
        this.language_id = lang_id;
        this.wrap_type = wrap_type;
        this.wrapper = wrapper;
    }

}
