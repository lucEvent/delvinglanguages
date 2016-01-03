package com.delvinglanguages.kernel.game;

import com.delvinglanguages.kernel.set.DReferences;

import java.util.HashMap;

public class PriorityMap extends HashMap<Integer, DReferences> {

    public PriorityMap() {
    }

    public Integer getMaxKey() {
        int res = Integer.MIN_VALUE;
        for (int priority : keySet()) {
            if (priority > res) {
                res = priority;
            }
        }
        return res;
    }

}
