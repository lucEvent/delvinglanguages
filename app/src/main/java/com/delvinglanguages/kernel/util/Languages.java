package com.delvinglanguages.kernel.util;

import com.delvinglanguages.kernel.Language;

import java.util.ArrayList;
import java.util.Collection;

public class Languages extends ArrayList<Language> {

    public Languages()
    {
        super();
    }

    public Languages(Collection<? extends Language> collection)
    {
        super(collection);
    }

    public Language getLanguageById(int id)
    {
        for (Language l : this)
            if (id == l.id)
                return l;
        return null;
    }

    public Language first()
    {
        if (isEmpty()) return null;
        return get(0);
    }

    public Language last()
    {
        if (isEmpty()) return null;
        return get(size() - 1);
    }

}
