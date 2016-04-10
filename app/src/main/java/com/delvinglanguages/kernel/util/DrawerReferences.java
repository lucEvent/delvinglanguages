package com.delvinglanguages.kernel.util;

import com.delvinglanguages.kernel.DrawerReference;

import java.util.ArrayList;

public class DrawerReferences extends ArrayList<DrawerReference> {

    public DrawerReferences() {
        super();
    }

    public DrawerReference getReferenceById(int id) {
        for (DrawerReference dref : this)
            if (dref.id == id)
                return dref;

        return null;
    }

    public boolean contains(String ref) {
        for (DrawerReference dref : this)
            if (ref.equalsIgnoreCase(dref.name))
                return true;

        return false;
    }

}
