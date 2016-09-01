package com.delvinglanguages.kernel;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.Tests;
import com.delvinglanguages.kernel.util.Themes;

import java.text.Collator;
import java.util.Locale;
import java.util.TreeSet;

public class Language {

    // Language CODE
    public static final int ENGLISH_UK = 0;
    public static final int ENGLISH_US = 1;
    public static final int SWEDISH = 2;
    public static final int FINNISH = 3;
    public static final int SPANISH = 4;
    public static final int CATALAN = 5;
    public static final int BASQUE = 6;
    public static final int CZECH = 7;
    public static final int DANISH = 8;
    public static final int DUTCH = 9;
    public static final int ESTONIAN = 10;
    public static final int FRENCH = 11;
    public static final int GERMAN = 12;
    public static final int GREEK = 13;
    public static final int ITALIAN = 14;
    public static final int NORWEGIAN = 15;
    public static final int PORTUGUESE = 16;
    public static final int RUSSIAN = 17;

    // Settings masks
    public static final int MASK_PHRASAL_VERBS = 0x1;
    public static final int MASK_PUBLIC = 0x2;

    private Locale locale;

    public final int id;

    public int code, settings;

    public String language_name;

    public Dictionary dictionary;
    public DReferences removed_references;
    public DrawerReferences drawer_references;
    public Themes themes;
    public Tests tests;
    public Statistics statistics;

    public Language(int id, int code, String language_name, int settings)
    {
        this(id, code, language_name, settings, null);
    }

    public Language(int id, int code, String language_name, int settings, Statistics statistics)
    {
        this.id = id;
        this.code = code;
        this.language_name = language_name;
        this.settings = settings;
        this.statistics = statistics;

        locale = getLocale(code);
    }

    public static Locale getLocale(int language_code)
    {
        switch (language_code) {
            case ENGLISH_UK:
                return Locale.UK;
            case ENGLISH_US:
                return Locale.US;
            case SWEDISH:
                return new Locale("sv", "SE", "SE");
            case FINNISH:
                return new Locale("fi", "FI", "FI");
            case SPANISH:
                return new Locale("es", "ES", "ES");
            case CATALAN:
                return new Locale("ca", "ES");
            case BASQUE:
                return new Locale("eu", "ES");
            case CZECH:
                return new Locale("cs", "CZ");
            case DANISH:
                return new Locale("da", "DK");
            case DUTCH:
                return new Locale("nl", "NL");
            case ESTONIAN:
                return new Locale("et", "EE");
            case FRENCH:
                return Locale.FRANCE;
            case GERMAN:
                return Locale.GERMANY;
            case GREEK:
                return new Locale("el", "GR");
            case ITALIAN:
                return Locale.ITALY;
            case NORWEGIAN:
                return new Locale("nb", "NO");
            case PORTUGUESE:
                return new Locale("pl", "PL");
            case RUSSIAN:
                return new Locale("ru", "RU");
        }
        return null;
    }

    /**
     * ********************** Getters ***********************
     **/
    public DReference getReference(String name)
    {
        return dictionary.getReference(false, name);
    }

    public DReferences getReferences()
    {
        return dictionary.getReferences(false);
    }

    public int[] getTypeCounter()
    {
        return dictionary.getTypeCounter();
    }

    public TreeSet<DReference> getDictionary()
    {
        return dictionary.getDictionary();
    }

    public TreeSet<DReference> getDictionaryInverse()
    {
        return dictionary.getDictionaryInverse();
    }

    public boolean getSetting(int mask)
    {
        return (settings & mask) != 0;
    }

    public Locale getLocale()
    {
        return locale;
    }

    public DReferences getVerbs()
    {
        return dictionary.getVerbs(false);
    }

    public int getTensesArrayResId()
    {
        int res;
        switch (code) {
            case SPANISH:
                res = R.array.es_tenses;
                break;
            case ENGLISH_UK:
            case ENGLISH_US:
                res = R.array.en_tenses;
                break;
            case SWEDISH:
                res = R.array.sv_tenses;
                break;
            default:
                res = R.array.en_tenses;
                break;
        }
        return res;
    }

    public int getSubjectArrayResId()
    {
        int res;
        switch (code) {
            case SPANISH:
                res = R.array.es_subjects;
                break;
            case ENGLISH_UK:
            case ENGLISH_US:
                res = R.array.en_subjects;
                break;
            case SWEDISH:
                res = R.array.sv_subjects;
                break;
            default:
                res = R.array.en_subjects;
                break;
        }
        return res;
    }

    public boolean isLoaded()
    {
        return dictionary != null && drawer_references != null && removed_references != null;
    }

    /**
     * ********************** Setters ***********************
     **/

    public void setReferences(DReferences references)
    {
        this.dictionary = new Dictionary(Collator.getInstance(locale), references);
    }

    public void setDrawerReferences(DrawerReferences drawer_references)
    {
        this.drawer_references = drawer_references;
    }

    public void setRemovedReferences(DReferences removed_references)
    {
        this.removed_references = removed_references;
    }

    public void setName(String name)
    {
        this.language_name = name;
    }

    public void setTests(Tests tests)
    {
        this.tests = tests;
    }

    public void setThemes(Themes themes)
    {
        this.themes = themes;
    }

    public void setSetting(boolean state, int mask)
    {
        if (state) {
            this.settings |= mask;
        } else {
            if ((this.settings & mask) != 0) {
                this.settings ^= mask;
            }
        }
    }

    public DReferences getPhrasalVerbs()
    {
        return dictionary.getPhrasalVerbs(false);
    }

    public static int configure(boolean phrasal)
    {
        int settings = 0;
        if (phrasal)
            settings |= MASK_PHRASAL_VERBS;
        return settings;
    }

    public void setCode(int code)
    {
        this.code = code;
        this.locale = getLocale(code);
    }

    /**
     * ********************** Adders ***********************
     **/

    public void addReference(DReference reference)
    {
        dictionary.addEntry(reference);
    }

    public void addReference(DrawerReference drawerReference)
    {
        drawer_references.add(0, drawerReference);
    }

    public void addTest(Test t)
    {
        tests.add(t);
    }

    public void addTheme(Theme theme)
    {
        themes.add(theme);
    }

    /**
     * ********************** Deletes ***********************
     **/

    public void removeReference(DReference reference)
    {
        dictionary.removeEntry(reference);

        removed_references.add(reference);
    }

    public void deleteReference(int position)
    {
        removed_references.remove(position);
    }

    public void deleteDrawerReference(DrawerReference drawerReference)
    {
        drawer_references.remove(drawerReference);
    }

    public void deleteAllRemovedReferences()
    {
        removed_references.clear();
    }

    /**
     * ********************** Restores ***********************
     **/
    public void restoreReference(DReference reference)
    {
        removed_references.remove(reference);
        dictionary.addEntry(reference);
    }

    public void updateReference(DReference reference, String name, String pronunciation, Inflexions inflexions)
    {
        dictionary.removeEntry(reference);
        reference.update(name, pronunciation, inflexions);
        dictionary.addEntry(reference);
    }

    public boolean isDictionaryCreated()
    {
        return dictionary != null && dictionary.dictionaryCreated;
    }

    public void clear()
    {
        dictionary = null;
        drawer_references = null;
        removed_references = null;
        themes = null;
        tests = null;
    }

}
