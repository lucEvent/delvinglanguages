package com.delvinglanguages.kernel.record;

import java.util.Arrays;

public class DelvingListRecord extends Record {

    public final int list_id;

    public int language_code;

    public int[] item_ids;

    public DelvingListRecord(int type, int list_id, int language_code, int[] item_ids, long time)
    {
        super(type, time);

        this.list_id = list_id;
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