package com.delvinglanguages.kernel;

import com.delvinglanguages.AppSettings;
import com.delvinglanguages.kernel.util.DReferences;
import com.delvinglanguages.kernel.util.Inflexions;

import java.text.Collator;
import java.util.Comparator;
import java.util.TreeSet;

public class Dictionary implements Comparator<DReference> {

    private Collator collator;

    private int[] type_counter;

    private TreeSet<DReference> dictionary, dictionary_inverse;

    public boolean dictionaryCreated;

    public Dictionary(Collator collator, final DReferences references)
    {
        this.collator = collator;
        this.collator.setStrength(Collator.PRIMARY);

        type_counter = new int[AppSettings.NUMBER_OF_TYPES];
        for (int i = 0; i < type_counter.length; i++) type_counter[i] = 0;

        dictionary = new TreeSet<>(this);
        dictionary_inverse = new TreeSet<>(this);

        dictionaryCreated = false;

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                for (DReference reference : references) {
                    addEntry(reference);
                }
                dictionaryCreated = true;
            }
        }).start();
    }

    public void addEntry(DReference entry)
    {
        int type = entry.type;
        for (int i = 0; i < type_counter.length; i++)
            if ((type & (1 << i)) != 0)
                type_counter[i]++;

        if (!dictionary.add(entry))
            AppSettings.printlog("Could not add reference [" + entry.name + "] :(");

        for (Inflexion inflexion : entry.getInflexions()) {
            Inflexion infD = new Inflexion(inflexion.getInflexions(), new String[]{entry.name}, inflexion.getType());

            for (String refNS : inflexion.getTranslations()) {
                if (refNS.length() == 0) {
                    continue;
                }
                DReference refN = new DReference(-1, refNS, "", new Inflexions(), entry.priority);
                if (!dictionary_inverse.add(refN)) {
                    refN = dictionary_inverse.ceiling(refN);
                }
                refN.addInflexion(infD);
            }
        }
    }

    public void removeEntry(DReference entry)
    {
        int type = entry.type;
        for (int i = 0; i < type_counter.length; i++)
            if ((type & (1 << i)) != 0)
                type_counter[i]--;

        dictionary.remove(entry);

        for (Inflexion inflexion : entry.getInflexions()) {
            for (String refNS : inflexion.getTranslations()) {
                DReference refN = dictionary_inverse.ceiling(DReference.createBait(refNS));

                refN.removeInflexion(inflexion);

                if (refN.getInflexions().isEmpty()) {
                    dictionary_inverse.remove(refN);
                }

            }
        }
    }

    public int[] getTypeCounter()
    {
        return type_counter;
    }

    public DReferences getReferences(boolean inverse)
    {
        TreeSet<DReference> dictionary = this.dictionary;
        if (inverse) {
            dictionary = this.dictionary_inverse;
        }
        return new DReferences(dictionary);
    }

    public DReferences getPhrasalVerbs(boolean inverse)
    {
        TreeSet<DReference> dictionary = this.dictionary;
        if (inverse) {
            dictionary = this.dictionary_inverse;
        }
        DReferences res = new DReferences();
        for (DReference ref : dictionary) {
            if (ref.isPhrasalVerb()) {
                res.add(ref);
            }
        }
        return res;
    }

    public DReferences getVerbs(boolean inverse)
    {
        TreeSet<DReference> dictionary = this.dictionary;
        if (inverse) {
            dictionary = this.dictionary_inverse;
        }
        DReferences res = new DReferences();
        for (DReference ref : dictionary) {
            if (ref.isVerb()) {
                res.add(ref);
            }
        }
        return res;
    }

    public TreeSet<DReference> getDictionary()
    {
        return dictionary;
    }

    public TreeSet<DReference> getDictionaryInverse()
    {
        return dictionary_inverse;
    }

    public DReference getReference(boolean inverse, String name)
    {
        TreeSet<DReference> dictionary = this.dictionary;
        if (inverse) {
            dictionary = this.dictionary_inverse;
        }
        DReference candidate = dictionary.ceiling(DReference.createBait(name));
        if (candidate != null && candidate.name.equals(name)) {
            return candidate;
        }
        return null;
    }

    @Override
    public int compare(DReference lhs, DReference rhs)
    {
        return collator.compare(lhs.name, rhs.name);
    }

    public int size()
    {
        return dictionary.size();
    }

}
