package com.delvinglanguages.kernel.util;

import android.support.annotation.NonNull;

import com.delvinglanguages.kernel.theme.ThemePair;

import java.util.ArrayList;

public class ThemePairs extends ArrayList<ThemePair> implements Wrapper<ThemePairs> {

    private static final String SEP = "%Th";

    public ThemePairs()
    {
        super();
    }

    public ThemePairs(int capacity)
    {
        super(capacity);
    }

    @Override
    public String wrap()
    {
        StringBuilder res = new StringBuilder();
        res.append(size());
        for (ThemePair pair : this)
            res.append(SEP).append(pair.inDelved)
                    .append(SEP).append(pair.inNative);

        return res.toString();
    }

    @Override
    public ThemePairs unWrap(@NonNull String wrapper)
    {
        clear();

        int index = 1;

        String[] parts = wrapper.split(SEP);

        int size = Integer.parseInt(parts[0]);
        for (int i = 0; i < size; i++) {
            String delved = parts[index++];
            String _native = parts[index++];
            add(new ThemePair(delved, _native));
        }
        return this;
    }

    @Override
    public int wrapType()
    {
        return Wrapper.TYPE_THEME_PAIRS;
    }
}
