package com.delvinglanguages.kernel.theme;

import com.delvinglanguages.kernel.util.ThemePairs;

public class Theme {

    public final int id;

    private String name;
    private ThemePairs pairs;

    public Theme(int id, String name) {
        this(id, name, new ThemePairs());
    }

    public Theme(int id, String name, ThemePairs pairs) {
        this.id = id;
        this.name = name;
        this.pairs = pairs;
    }

    public String getName() {
        return name;
    }

    public ThemePairs getPairs() {
        return pairs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPairs(ThemePairs pairs) {
        this.pairs = pairs;
    }

    public boolean hasContent(CharSequence s) {
        if (name.contains(s)) return true;
        for (ThemePair pair : pairs)
            if (pair.inDelved.contains(s) || pair.inNative.contains(s))
                return true;
        return false;
    }

}
