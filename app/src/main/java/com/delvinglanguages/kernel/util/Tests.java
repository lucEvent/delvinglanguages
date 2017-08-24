package com.delvinglanguages.kernel.util;

import com.delvinglanguages.kernel.test.Test;

import java.util.ArrayList;

public class Tests extends ArrayList<Test> {

    public Tests()
    {
        super();
    }

    public Tests(int capacity)
    {
        super(capacity);
    }

    public Test getTestById(int id)
    {
        for (Test t : this)
            if (t.id == id)
                return t;

        return null;
    }

    public Test getTestFrom(int from_id)
    {
        for (Test t : this)
            if (t.from_id == from_id)
                return t;

        return null;
    }

}
