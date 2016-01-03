package com.delvinglanguages.kernel.set;

import com.delvinglanguages.kernel.Translation;

import java.util.ArrayList;

public class Translations extends ArrayList<Translation> {

    private static final String SEP = "%T";

    public Translations() {
        super();
    }

    public Translations(ArrayList<Translation> value) {
        super(value);
    }

    public Translations(String wrapper) {
        String[] elems = wrapper.split(SEP);
        for (String elem : elems) {
            add(new Translation(elem));
        }
    }

    public String toSavingString() {
        if (!this.isEmpty()) {
            StringBuilder res = new StringBuilder(get(0).toSavingString());
            for (int i = 1; i < this.size(); ++i) {
                res.append(SEP).append(get(i).toSavingString());
            }
            return res.toString();
        }
        return "";
    }
}
