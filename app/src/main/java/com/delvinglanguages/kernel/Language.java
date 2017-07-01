package com.delvinglanguages.kernel;

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

        locale = LanguageCode.getLocale(code);
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
        this.locale = LanguageCode.getLocale(code);
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
