package com.delvinglanguages.kernel;

public class Inflexion {

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

    public boolean hasContent(CharSequence content) {
        for (String i : inflexions)
            if (i.toLowerCase().contains(content)) return true;
        for (String t : translations)
            if (t.toLowerCase().contains(content)) return true;
        return false;
    }

    @Override
    public Inflexion clone() {
        return new Inflexion(inflexions.clone(), translations.clone(), type);
    }

}
