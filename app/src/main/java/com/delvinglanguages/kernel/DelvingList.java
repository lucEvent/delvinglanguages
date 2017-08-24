package com.delvinglanguages.kernel;

import com.delvinglanguages.kernel.subject.Subject;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.DrawerReferences;
import com.delvinglanguages.kernel.util.Inflexions;
import com.delvinglanguages.kernel.util.RemovedItem;
import com.delvinglanguages.kernel.util.RemovedItems;
import com.delvinglanguages.kernel.util.Statistics;
import com.delvinglanguages.kernel.util.Subjects;
import com.delvinglanguages.kernel.util.Tests;
import com.delvinglanguages.kernel.util.Wrapper;

import java.text.Collator;
import java.util.TreeSet;

public class DelvingList {

    // Settings masks
    private static final int SETTING_PHRASAL_VERBS = 0x1;
    private static final int SETTING_PUBLIC = 0x2;

    public final int id;

    public int from_code, to_code, settings;

    public String name;

    public Dictionary dictionary;
    public RemovedItems removed_items;
    public DrawerReferences drawer_references;
    public Subjects subjects;
    public Tests tests;
    public Statistics statistics;

    public DelvingList(int id, int from_code, int to_code, String list_name, int settings)
    {
        this(id, from_code, to_code, list_name, settings, null);
    }

    public DelvingList(int id, int from_code, int to_code, String list_name, int settings, Statistics statistics)
    {
        this.id = id;
        this.from_code = from_code;
        this.to_code = to_code;
        this.name = list_name;
        this.settings = settings;
        this.statistics = statistics;
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

    public int getNumPhrasalVerbs()
    {
        return dictionary.getTypeCounter()[4];
    }

    public TreeSet<DReference> getDictionary()
    {
        return dictionary.getDictionary();
    }

    public TreeSet<DReference> getDictionaryInverse()
    {
        return dictionary.getDictionaryInverse();
    }

    public boolean arePhrasalVerbsEnabled()
    {
        return (settings & SETTING_PHRASAL_VERBS) != 0;
    }

    public boolean isPublic()
    {
        return (settings & SETTING_PUBLIC) != 0;
    }

    public DReferences getVerbs()
    {
        return dictionary.getVerbs(false);
    }

    public boolean isLoaded()
    {
        return dictionary != null && drawer_references != null;
    }

    public int getCodes()
    {
        return from_code | (to_code << 16);
    }

    /**
     * ********************** Setters ***********************
     **/

    public void setReferences(DReferences references)
    {
        this.dictionary = new Dictionary(Collator.getInstance(LanguageCode.getLocale(from_code)), references);
    }

    public void setDrawerReferences(DrawerReferences drawer_references)
    {
        this.drawer_references = drawer_references;
    }

    public void setRemovedItems(RemovedItems removed_items)
    {
        this.removed_items = removed_items;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setTests(Tests tests)
    {
        this.tests = tests;
    }

    public void setSubjects(Subjects subjects)
    {
        this.subjects = subjects;
    }

    public void setPhrasalVerbsState(boolean state)
    {
        setSetting(state, SETTING_PHRASAL_VERBS);
    }

    public void setPublicState(boolean state)
    {
        setSetting(state, SETTING_PUBLIC);
    }

    private void setSetting(boolean state, int setting)
    {
        if (state)
            this.settings |= setting;
        else if ((this.settings & setting) != 0)
            this.settings ^= setting;
    }

    public DReferences getPhrasalVerbs()
    {
        return dictionary.getPhrasalVerbs(false);
    }

    public static int parseSettings(boolean phrasal)
    {
        int settings = 0;
        if (phrasal)
            settings |= SETTING_PHRASAL_VERBS;
        return settings;
    }

    public void setFromCode(int from_code)
    {
        this.from_code = from_code;
    }

    public void setToCode(int to_code)
    {
        this.to_code = to_code;
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

    public void addSubject(Subject subject)
    {
        subjects.add(subject);
    }

    /**
     * ********************** Deletes ***********************
     **/

    public void removeReference(DReference reference)
    {
        dictionary.removeEntry(reference);
        removed_items = null;
    }

    public void removeSubject(Subject subject)
    {
        subjects.remove(subject);
        removed_items = null;
    }

    public void removeTest(Test test)
    {
        tests.remove(test);
        removed_items = null;
    }

    public void deleteItem(int position)
    {
        removed_items.remove(position);
    }

    public void deleteDrawerReference(DrawerReference drawerReference)
    {
        drawer_references.remove(drawerReference);
    }

    public void deleteAllRemovedItems()
    {
        removed_items = null;
    }

    /**
     * ********************** Restores ***********************
     **/
    public void restoreItem(RemovedItem item)
    {
        removed_items.remove(item);

        switch (item.wrap_type) {
            case Wrapper.TYPE_REFERENCE:
                dictionary.addEntry(item.castToReference());
                break;
            case Wrapper.TYPE_SUBJECT:
                subjects.add(item.castToSubject());
                break;
            case Wrapper.TYPE_TEST:
                tests.add(item.castToTest());
                break;
        }
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
        removed_items = null;
        subjects = null;
        tests = null;
    }

}
