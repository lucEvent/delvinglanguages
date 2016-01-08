package com.delvinglanguages.kernel.set;

import com.delvinglanguages.kernel.Inflexion;

import java.util.ArrayList;

public class Inflexions extends ArrayList<Inflexion> {

    private static final String SEP = "%I";

    public Inflexions() {
        super();
    }

    public Inflexions(String wrapper) {
        super();
        String[] parts = wrapper.split(SEP);

        int index = 1;
        int num = Integer.parseInt(parts[0]);
        for (int i = 0; i < num; i++) {
            int nInfs = Integer.parseInt(parts[index++]);
            String[] inflexions = new String[nInfs];
            for (int j = 0; j < nInfs; j++) {
                inflexions[j] = parts[index++];
            }

            int nTrans = Integer.parseInt(parts[index++]);
            String[] translations = new String[nTrans];
            for (int j = 0; j < nTrans; j++) {
                translations[j] = parts[index++];
            }

            int type = Integer.parseInt(parts[index++]);

            this.add(new Inflexion(inflexions, translations, type));
        }
    }

    public Inflexions(ArrayList<Inflexion> value) {
        super(value);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder().append(this.size());

        for (Inflexion inf : this) {

            String[] inflexions = inf.getInflexions();
            res.append(SEP).append(inflexions.length);
            for (String s : inflexions) res.append(SEP).append(s);

            String[] translations = inf.getTranslations();
            res.append(SEP).append(translations.length);
            for (String s : translations) res.append(SEP).append(s);

            res.append(SEP).append(inf.getType());
        }
        return res.toString();
    }

    public String getTranslationsAsString() {
        StringBuilder res = new StringBuilder();
        boolean first = true;
        for (Inflexion i : this) {
            for (String t : i.getTranslations()) {
                if (!first) {
                    res.append(", ").append(t);
                } else {
                    res.append(t);
                    first = false;
                }
            }
        }
        return res.toString();
    }

}
