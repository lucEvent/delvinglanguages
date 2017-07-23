package com.delvinglanguages.kernel.record;

import java.util.ArrayList;
import java.util.Collection;

public class Records extends ArrayList<Record> {

    public Records()
    {
        super();
    }

    public Records(Collection<? extends Record> collection)
    {
        super(collection);
    }

}
