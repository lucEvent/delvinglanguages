package com.delvinglanguages.kernel;

import com.delvinglanguages.kernel.set.Inflexions;
import com.delvinglanguages.settings.Settings;

import java.util.Comparator;
import java.util.TreeSet;

public class DReference {

    /**
     * ***************** Variables ******************
     **/
    public String name;
    public Inflexions inflexions;
    public String pronunciation;
    public int priority;
    private int type;

    public TreeSet<Word> appearances;

    /**
     * ***************** Creadoras ******************
     **/
    public DReference(String name) {
        this(name, "", -1);
    }

    /*public DReference(String name, String pronunciation, int priority) {
        this(name, pronunciation, priority);
    }*/

    public DReference(String name, String pronunciation, int priority) {
        this.name = name;
        this.pronunciation = pronunciation;
        this.priority = priority;
        this.type = -1;
        appearances = new TreeSet<Word>(new Comparator<Word>() {
            @Override
            public int compare(Word lhs, Word rhs) {
                return lhs.id < rhs.id ? -1 : (lhs.id == rhs.id ? 0 : 1);
            }
        });
        inflexions = new Inflexions();
    }

    /**
     * ***************** Getters ******************
     **/
    public int getType() {
        if (type == -1) {
            type = 0;
            for (Inflexion inf : inflexions)
                type |= inf.getType();
        }
        return type;
    }

    public String getTranslationsAsString() {
        return inflexions.getTranslationsAsString();
    }

    public Inflexions getInflexions() {
        return inflexions;
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
    public void addTranslation(Word word, Inflexions inflexions) {
        this.type = -1;
        this.appearances.add(word);
        this.inflexions.addAll(inflexions);
    }

    public void addTranslation(Word word, Inflexion inflexion) {
        this.type = -1;
        appearances.add(word);
        inflexions.add(inflexion);
    }

    public void removeTranslation(Word word, Inflexions inflexions) {
        this.type = -1;
        this.appearances.remove(word);
        this.inflexions.remove(inflexions);
    }

    public void removeTranslation(Word word, Inflexion inflexion) {
        this.type = -1;
        this.appearances.remove(word);
        for (Inflexion inf : this.inflexions) {
            if (inf.getInflexions() == inflexion.getInflexions()) {
                this.inflexions.remove(inf);
                break;
            }
        }
    }

    private void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##DReference##", text);
    }

}
