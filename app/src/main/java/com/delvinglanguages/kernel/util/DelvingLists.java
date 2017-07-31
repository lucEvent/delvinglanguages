package com.delvinglanguages.kernel.util;

import com.delvinglanguages.kernel.DelvingList;

import java.util.ArrayList;
import java.util.Collection;

public class DelvingLists extends ArrayList<DelvingList> {

    public DelvingLists()
    {
        super();
    }

    public DelvingLists(Collection<? extends DelvingList> collection)
    {
        super(collection);
    }

    public DelvingList getListById(int id)
    {
        for (DelvingList l : this)
            if (id == l.id)
                return l;
        return null;
    }

    public DelvingList first()
    {
        if (isEmpty()) return null;
        return get(0);
    }

    public DelvingList last()
    {
        if (isEmpty()) return null;
        return get(size() - 1);
    }

}
