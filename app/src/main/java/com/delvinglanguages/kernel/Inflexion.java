package com.delvinglanguages.kernel;

import java.io.Serializable;

public class Inflexion implements Serializable {

    private String[] inflexions;

    private String[] translations;

    private int type;

    public Inflexion(String[] inflexions, String[] translations, int type) {
        this.inflexions = inflexions;
        this.translations = translations;
        this.type = type;
    }

    public String[] getInflexions() {
        return inflexions;
    }

    public String[] getTranslations() {
        return translations;
    }

    public int getType() {
        return type;
    }

    /**
     * Askers
     */
    public boolean hasInflexions() {
        return inflexions.length != 0;
    }

    /**
     * Setters
     */
    public void setInflexions(String[] inflexions) {
        this.inflexions = inflexions;
    }

    public void setTranslations(String[] translations) {
        this.translations = translations;
    }

    public void setType(int type) {
        this.type = type;
    }
}
