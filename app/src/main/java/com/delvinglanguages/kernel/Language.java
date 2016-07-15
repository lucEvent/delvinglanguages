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

    private static final Character CAPS[][] = {
            /** UK **/
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'},
            /** US **/
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'},
            /** SV **/
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Ä', 'Ö', 'Å'},
            /** FI **/
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Ä', 'Ö', 'Å'},
            /** ES **/
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'},
            /** CA **/
            {'A', 'B', 'C', 'Ç', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}, // CA
            /** BA **/
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'},
            /** CZ **/
            {'A', 'B', 'C', 'Č', 'D', 'Ď', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ň', 'O', 'P', 'Q', 'R', 'Ř', 'S', 'Š', 'T', 'Ť', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Ž'},
            /** DA **/
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Æ', 'Ø', 'Å'},
            /** DU **/
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'},
            /** EST **/
            {'A', 'B', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'R', 'S', 'Š', 'Z', 'Ž', 'T', 'U', 'V', 'Õ', 'Ä', 'Ö', 'Ü'},
            /** FR **/
            {'A', 'B', 'C', 'Ç', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}, // CA
            /** GE **/
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'},
            /** GR **/
            {'Α', 'Β', 'Γ', 'Δ', 'Ε', 'Ζ', 'Η', 'Θ', 'Ι', 'Κ', 'Λ', 'Μ', 'Ν', 'Ξ', 'Ο', 'Π', 'Ρ', 'Σ', 'Τ', 'Υ', 'Φ', 'Χ', 'Ψ', 'Ω'},
            /** IT **/
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'Z'},
            /** NO **/
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Æ', 'Ø', 'Å'},
            /** PO **/
            {'A', 'Ą', 'B', 'C', 'Ć', 'D', 'E', 'Ę', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'Ł', 'M', 'N', 'Ń', 'O', 'Ó', 'P', 'R', 'S', 'Ś', 'T', 'U', 'W', 'Y', 'Z', 'Ź', 'Ż'},
            /** RU **/
            {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'}
    };

    // Language CODE
    public static final int UK = 0;
    public static final int US = 1;
    public static final int SV = 2;
    public static final int FI = 3;
    public static final int ES = 4;
    public static final int CA = 5;
    public static final int BA = 6;
    public static final int CZ = 7;
    public static final int DA = 8;
    public static final int DU = 9;
    public static final int EST = 10;
    public static final int FR = 11;
    public static final int GE = 12;
    public static final int GR = 13;
    public static final int IT = 14;
    public static final int NO = 15;
    public static final int PO = 16;
    public static final int RU = 17;

    // Settings masks
    public static final int MASK_PHRASAL_VERBS = 0x1;


    private Locale locale;

    public int CODE;

    public final int id;

    public String language_name;

    public int settings;

    public Dictionary dictionary;
    public DReferences removed_references;
    public DrawerReferences drawer_references;
    public Themes themes;
    public Tests tests;
    public Statistics statistics;

    public Language(int id, int code, String language_name, int settings)
    {
        this.id = id;
        this.CODE = code;
        this.language_name = language_name;
        this.settings = settings;

        locale = getLocale(code);
    }

    public static Locale getLocale(int language_code)
    {
        switch (language_code) {
            case UK:
                return Locale.UK;
            case US:
                return Locale.US;
            case SV:
                return new Locale("sv", "SE", "SE");
            case FI:
                return new Locale("fi", "FI", "FI");
            case ES:
                return new Locale("es", "ES", "ES");
            case CA:
                return new Locale("ca", "ES");
            case BA:
                return new Locale("eu", "ES");
            case CZ:
                return new Locale("cs", "CZ");
            case DA:
                return new Locale("da", "DK");
            case DU:
                return new Locale("nl", "NL");
            case EST:
                return new Locale("et", "EE");
            case FR:
                return Locale.FRANCE;
            case GE:
                return Locale.GERMANY;
            case GR:
                return new Locale("el", "GR");
            case IT:
                return Locale.ITALY;
            case NO:
                return new Locale("nb", "NO");
            case PO:
                return new Locale("pl", "PL");
            case RU:
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

    public Character[] getDictionaryIndexes()
    {
        return CAPS[CODE];
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
        if (CODE == Language.ES) {
            res = R.array.es_tenses;
        } else if (CODE == Language.UK || CODE == Language.US) {
            res = R.array.en_tenses;
        } else if (CODE == Language.SV) {
            res = R.array.sv_tenses;
        } else if (CODE == Language.FI) {
            res = R.array.fi_tenses;
        } else {
            res = R.array.en_tenses;
        }
        return res;
    }

    public int getSubjectArrayResId()
    {
        int res;
        if (CODE == Language.ES) {
            res = R.array.es_subjects;
        } else if (CODE == Language.UK || CODE == Language.US) {
            res = R.array.en_subjects;
        } else if (CODE == Language.SV) {
            res = R.array.sv_subjects;
        } else {
            res = R.array.en_subjects;
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

    public void setStatistics(Statistics s)
    {
        statistics = s;
    }

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
        this.CODE = code;
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
