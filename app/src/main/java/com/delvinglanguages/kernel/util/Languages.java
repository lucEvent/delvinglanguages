package com.delvinglanguages.kernel.util;

import com.delvinglanguages.kernel.Language;

import java.util.ArrayList;
import java.util.Collection;

public class Languages extends ArrayList<Language> {

    public Languages() {
        super();
    }

    public Languages(Collection<? extends Language> collection) {
        super(collection);
    }

    public Language getLanguageById(int id) {
        for (Language l : this) {
            if (id == l.id) {
                return l;
            }
        }
        return null;
    }

}
