package com.delvinglanguages.kernel.util;

import java.util.ArrayList;
import java.util.TreeSet;

public class RemovedItems extends ArrayList<RemovedItem> {

    public RemovedItems()
    {
        super();
    }

    public RemovedItems(TreeSet<RemovedItem> collection)
    {
        super(collection);
    }

    public RemovedItems(int capacity)
    {
        super(capacity);
    }

}
