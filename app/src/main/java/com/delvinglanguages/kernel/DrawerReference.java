package com.delvinglanguages.kernel;

import android.support.annotation.NonNull;

import com.delvinglanguages.kernel.util.Wrapper;

public class DrawerReference implements Wrapper<DrawerReference> {

    public final int id;

    public String name;

    public DrawerReference(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    @Override
    public String wrap()
    {
        return name;
    }

    @Override
    public DrawerReference unWrap(@NonNull String wrapper)
    {
        return new DrawerReference(-1, wrapper);
    }

    @Override
    public int wrapType()
    {
        return Wrapper.TYPE_DRAWER_REFERENCE;
    }

}
