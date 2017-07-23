package com.delvinglanguages.kernel.record;

import java.util.Arrays;

public class LanguageRecord extends Record {

    public final int language_id;

    public int language_code;

    public int[] item_ids;

    public LanguageRecord(int type, int language_id, int language_code, int[] item_ids, long time)
    {
        super(type, time);

        this.language_id = language_id;
        this.language_code = language_code;
        this.item_ids = item_ids;
    }

    public int getNumber()
    {
        return item_ids.length;
    }

    public void addItem(int item_id)
    {
        item_ids = Arrays.copyOf(item_ids, item_ids.length + 1);
        item_ids[item_ids.length - 1] = item_id;
    }

}