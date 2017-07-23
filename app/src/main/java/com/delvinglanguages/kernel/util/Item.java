package com.delvinglanguages.kernel.util;

public abstract class Item {

    public static final int DREFERENCE = 0;
    public static final int THEME = 1;
    public static final int TEST = 2;
    public static final int TYPES_DATA = 3;
    public static final int STATS_DATA = 4;
    public static final int WEB_SEARCH = 5;

    public final int id;
    public final int itemType;

    public Item(int id, int itemType)
    {
        this.id = id;
        this.itemType = itemType;
    }

}
