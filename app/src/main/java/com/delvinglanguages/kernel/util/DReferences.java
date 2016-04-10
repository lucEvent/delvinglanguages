package com.delvinglanguages.kernel.util;

import com.delvinglanguages.kernel.DReference;

import java.util.ArrayList;
import java.util.TreeSet;

public class DReferences extends ArrayList<DReference> {

    public DReferences() {
        super();
    }

    public DReferences(TreeSet<DReference> collection) {
        super(collection);
    }

    public DReferences(int capacity) {
        super(capacity);
    }

    public boolean contains(String ref) {
        for (DReference dref : this)
            if (ref.equals(dref.name))
                return true;

        return false;
    }

}
