package com.delvinglanguages.kernel;

import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.settings.Settings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

public class DReference {

    /**
     * ***************** Variables ******************
     **/
    public String name;
    public String pronunciation;
    public int priority;

    private int type;

    public TreeSet<Word> appearances;
    public DReferences translations;

    /**
     * ***************** Creadoras ******************
     **/
    public DReference(String name, int type) {
        this(name, "", -1, type);
    }

    public DReference(String name, String pronunciation, int priority) {
        this(name, pronunciation, priority, -1);
    }

    public DReference(String name, String pronunciation, int priority, int type) {
        this.name = name;
        this.pronunciation = pronunciation;
        this.priority = priority;
        this.type = type;
        appearances = new TreeSet<Word>(new Comparator<Word>() {
            @Override
            public int compare(Word lhs, Word rhs) {
                return lhs.id < rhs.id ? -1 : (lhs.id == rhs.id ? 0 : 1);
            }
        });
        translations = new DReferences();
    }

    /**
     * ***************** Getters ******************
     **/
    public int getType() {
        int t = this.type;
        if (t == -1) {
            t = 0;
            for (DReference ref : translations) {
                t |= ref.type;
            }
        }
        return t;
    }

    public Character getCap() {
        return name.charAt(0);
    }

    public String getTranslation() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < translations.size(); i++) {
            if (i != 0) {
                res.append(", ");
            }
            res.append(translations.get(i).name);
        }
        return res.toString();
    }

    public ArrayList<String> getTranslationArray(ArrayList<String> list) {
        if (list == null) {
            list = new ArrayList<String>(translations.size());
        } else {
            list.clear();
        }
        for (DReference ref : translations) {
            list.add(ref.name);
        }
        return list;
    }

    public Translations getTranslations() {
        Translations res = new Translations();
        for (DReference ref : translations) {
            res.add(new Translation(ref.name, ref.type));
        }
        return res;
    }

    /**
     * ***************** Askers ******************
     **/
    public boolean isNoun() {
        return (getType() & 0x1) != 0;
    }

    public boolean isVerb() {
        return (getType() & 0x2) != 0;
    }

    public boolean isAdjective() {
        return (getType() & 0x4) != 0;
    }

    public boolean isAdverb() {
        return (getType() & 0x8) != 0;
    }

    public boolean isPhrasalVerb() {
        return (getType() & 0x10) != 0;
    }

    public boolean isExpression() {
        return (getType() & 0x20) != 0;
    }

    public boolean isOther() {
        return (getType() & 0x40) != 0;
    }

    /**
     * ***************** Modificadoras ******************
     **/
    public void linkTo(Word word, DReference ref) {
        appearances.add(word);
        translations.add(ref);
    }

    public void unlink(Word word, DReference ref) {
        boolean w = appearances.remove(word);
        boolean b = translations.remove(ref);
    }

    private void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##DReference##", text);
    }

}
