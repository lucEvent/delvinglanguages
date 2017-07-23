package com.delvinglanguages.kernel;

import android.support.annotation.NonNull;

import com.delvinglanguages.kernel.util.Wrapper;

public class DrawerReference implements Wrapper {

    public final int id;

    public String name;

    public DrawerReference(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public static DrawerReference fromWrapper(int id, @NonNull String wrapper)
    {
        return new DrawerReference(id, wrapper);
    }

    @Override
    public String wrap()
    {
        return name;
    }

    @Override
    public int wrapType()
    {
        return Wrapper.TYPE_DRAWER_REFERENCE;
    }

}
