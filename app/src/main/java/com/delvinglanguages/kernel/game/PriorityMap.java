package com.delvinglanguages.kernel.game;

import com.delvinglanguages.kernel.util.DReferences;

import java.util.HashMap;

public class PriorityMap extends HashMap<Integer, DReferences> {

    public PriorityMap()
    {
        super();
    }

    public Integer getMaxKey()
    {
        int max = Integer.MIN_VALUE;
        for (int priority : keySet())
            if (priority > max)
                max = priority;
        return max;
    }

}
