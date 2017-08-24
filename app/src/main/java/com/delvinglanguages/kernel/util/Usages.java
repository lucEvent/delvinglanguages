package com.delvinglanguages.kernel.util;

import android.support.annotation.NonNull;

import com.delvinglanguages.kernel.Usage;

import java.util.Collection;
import java.util.HashMap;

public class Usages extends HashMap<String, Usage> {

    public Usages()
    {
    }

    public Usages(@NonNull String[] translations)
    {
        super(translations.length);

        for (int i = 0; i < translations.length; i++)
            super.put(translations[i], new Usage(translations[i], ""));
    }

    public Usages(@NonNull Collection<Usage> c)
    {
        for (Usage u : c)
            put(u.translation, u);
    }

    public boolean add(@NonNull Usage usage)
    {
        put(usage.translation, usage);
        return true;
    }

}
