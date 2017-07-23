package com.delvinglanguages.kernel.theme;

import android.support.annotation.NonNull;

import com.delvinglanguages.kernel.util.Item;
import com.delvinglanguages.kernel.util.ThemePairs;
import com.delvinglanguages.kernel.util.Wrapper;

public class Theme extends Item implements Wrapper {

    private static final String SEP = "%Th";

    private String name;
    private ThemePairs pairs;

    public Theme(int id, String name, String pairsWrapper)
    {
        this(id, name, ThemePairs.fromWrapper(pairsWrapper));
    }

    public Theme(int id, String name, ThemePairs pairs)
    {
        super(id, Item.THEME);
        this.name = name;
        this.pairs = pairs;
    }

    public static Theme fromWrapper(int id, @NonNull String wrapper)
    {
        int index = 2;
        ThemePairs pairs;

        String[] parts = wrapper.split(SEP);

        String name = parts[0];
        int size = Integer.parseInt(parts[1]);
        pairs = new ThemePairs(size);
        for (int i = 0; i < size; i++) {
            String delved = parts[index++];
            String _native = parts[index++];
            pairs.add(new ThemePair(delved, _native));
        }
        return new Theme(id, name, pairs);
    }

    @Override
    public String wrap()
    {
        StringBuilder res = new StringBuilder();
        res.append(name).append(SEP)
                .append(pairs.size());

        for (ThemePair thp : pairs)
            res.append(SEP).append(thp.inDelved)
                    .append(SEP).append(thp.inNative);

        return res.toString();
    }

    @Override
    public int wrapType()
    {
        return TYPE_THEME;
    }

    public String getName()
    {
        return name;
    }

    public ThemePairs getPairs()
    {
        return pairs;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPairs(ThemePairs pairs)
    {
        this.pairs = pairs;
    }

    public boolean hasContent(CharSequence s)
    {
        if (name.toLowerCase().contains(s)) return true;
        for (ThemePair pair : pairs)
            if (pair.inDelved.toLowerCase().contains(s) || pair.inNative.toLowerCase().contains(s))
                return true;
        return false;
    }

}
