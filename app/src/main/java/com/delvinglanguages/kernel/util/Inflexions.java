package com.delvinglanguages.kernel.util;

import android.support.annotation.NonNull;

import com.delvinglanguages.kernel.Inflexion;

import java.util.ArrayList;

public class Inflexions extends ArrayList<Inflexion> implements Wrapper {

    private static final String SEP = "%I";

    public Inflexions()
    {
        super();
    }

    public Inflexions(int size)
    {
        super(size);
    }

    public Inflexions(ArrayList<Inflexion> values)
    {
        super(values);
    }

    public static Inflexions fromWrapper(@NonNull String wrapper)
    {
        String[] parts = wrapper.split(SEP);

        int size = Integer.parseInt(parts[0]);
        int index = 1;
        Inflexions res = new Inflexions(size);
        for (int i = 0; i < size; i++) {
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

            res.add(new Inflexion(inflexions, translations, type));
        }
        return res;
    }

    @Override
    public String wrap()
    {
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

    @Override
    public int wrapType()
    {
        return Wrapper.TYPE_INFLEXIONS;
    }

    public String[] getTranslations()
    {
        int translation_counter = 0;
        for (Inflexion i : this) {
            translation_counter += i.getTranslations().length;
        }

        String[] res = new String[translation_counter];
        int copy_index = 0;
        for (Inflexion i : this) {
            String[] translations = i.getTranslations();
            System.arraycopy(translations, 0, res, copy_index, translations.length);
            copy_index += translations.length;
        }
        return res;
    }

    public String getTranslationsAsString()
    {
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

    public String getInflexionsAsString()
    {
        StringBuilder res = new StringBuilder();
        boolean first = true;
        for (Inflexion i : this) {
            for (String e : i.getInflexions()) {
                if (!first) {
                    res.append(", ").append(e);
                } else {
                    res.append(e);
                    first = false;
                }
            }
        }
        return res.toString();
    }

    public boolean hasContent(CharSequence content)
    {
        for (Inflexion i : this) {
            if (i.hasContent(content)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Inflexions clone()
    {
        Inflexions clone = new Inflexions(this.size());
        for (Inflexion inflexion : this)
            clone.add(inflexion.clone());

        return clone;
    }

}
