package com.delvinglanguages.kernel;

import com.delvinglanguages.kernel.set.Inflexions;
import com.delvinglanguages.settings.Settings;

import java.util.ArrayList;

public class Word implements Comparable<Word> {

    public static final int INITIAL_PRIORITY = 100;

    public static final int NOUN = 0;
    public static final int VERB = 1;
    public static final int ADJECTIVE = 2;
    public static final int ADVERB = 3;
    public static final int PHRASAL = 4;
    public static final int EXPRESSION = 5;
    public static final int OTHER = 6;

    public final int id;

    protected String name;
    protected Inflexions inflexions;
    protected String pronunciation;
    protected int priority;

    public Word(int id, String name, String inflexions, String pronunciation, int priority) {
        this(id, name, new Inflexions(inflexions), pronunciation, priority);
    }

    public Word(int id, String name, Inflexions inflexions, String pronunciation, int priority) {
        this.id = id;
        this.name = name;
        this.inflexions = inflexions;
        this.pronunciation = pronunciation;
        this.priority = priority;
    }

    /**
     * ****************************** Getters *******************************
     **/
    public String getName() {
        return name;
    }

    public String getTranslationsAsString() {
        return inflexions.getTranslationsAsString();
    }

    public ArrayList<String> getTranslationsAsArray() {
        ArrayList<String> res = new ArrayList<String>();
        for (Inflexion i : inflexions) {
            for (String t : i.getTranslations()) {
                res.add(t);
            }
        }
        return res;
    }

    public Inflexions getInflexions() {
        return inflexions;
    }

    public String getInflexionsAsString() {
        return inflexions.toString();
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public int getType() {
        int res = 0;
        for (Inflexion i : inflexions) {
            res |= i.getType();
        }
        return res;
    }

    public int getPriority() {
        return priority;
    }

    public Word getInverse() {
        //// TODO: 06/01/2016
        return null;
    }

    /**
     * ****************************** Askers *******************************
     **/
    @Override
    public boolean equals(Object word) {
        return word instanceof Word && this.id == ((Word) word).id;
    }

    /**
     * ****************************** Setters *******************************
     **/
    public void setInflexions(Inflexions inflexions) {
        this.inflexions = inflexions;
    }

    public void update(String name, Inflexions inflexions, String pronunciation) {
        this.name = name;
        this.inflexions = inflexions;
        this.pronunciation = pronunciation;
    }

    public void updatePriority(int priority) {
        this.priority = priority;
    }

    /**
     * ************************** Interfaces *******************************
     **/
    @Override
    public int compareTo(Word another) {
        return name.compareToIgnoreCase(another.name);
    }

    private static void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##Word##", text);
    }

}
