package com.delvinglanguages.kernel;

import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.settings.Settings;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

public class Dictionary implements Comparator<DReference> {

    private Collator collator;

    private int[] type_counter;

    private Words words;
    private TreeSet<DReference> dictionary, dictionary_inverse;

    public boolean dictionaryCreated;

    public Dictionary(Collator collator, Words words) {
        this.collator = collator;
        this.collator.setStrength(Collator.PRIMARY);
        this.words = words;

        type_counter = new int[7];
        for (int i = 0; i < type_counter.length; i++) type_counter[i] = 0;

        dictionary = new TreeSet<DReference>(this);
        dictionary_inverse = new TreeSet<DReference>(this);

        dictionaryCreated = false;

        new Thread(createDictionary).start();
    }

    private Runnable createDictionary = new Runnable() {
        @Override
        public void run() {
            for (Word word : words) {
                addEntry(word);
            }
            dictionaryCreated = true;
        }
    };

    public void addEntry(Word entry) {
        int type = entry.getType();
        for (int i = 0; i < type_counter.length; i++) {
            if ((type & (1 << i)) != 0) {
                type_counter[i]++;
            }
        }

        ArrayList<String> refsD = entry.getNameArray();
        Translations refsN = entry.getTranslations();
        for (String refDS : refsD) {
            if (refDS.length() == 0) {
                continue;
            }
            DReference refD = new DReference(refDS, entry.pronunciacion, entry.prioridad);
            if (!dictionary.add(refD)) {
                refD = dictionary.ceiling(refD);
            }

            for (Translation transN : refsN) {
                for (String refNS : transN.getItems()) {
                    if (refNS.length() == 0) {
                        continue;
                    }
                    DReference refN = new DReference(refNS, "", entry.prioridad, transN.type);
                    if (!dictionary_inverse.add(refN)) {
                        refN = dictionary_inverse.ceiling(refN);
                    }
                    refD.linkTo(entry, refN);
                    refN.linkTo(entry, refD);
                }
            }
        }
    }

    public void removeEntry(Word entry) {
        int type = entry.getType();
        for (int i = 0; i < type_counter.length; i++) {
            if ((type & (1 << i)) != 0) {
                type_counter[i]--;
            }
        }

        ArrayList<String> refsD = entry.getNameArray();
        Translations refsN = entry.getTranslations();
        for (String refDS : refsD) {
            DReference refD = dictionary.ceiling(new DReference(refDS, -1));

            for (Translation transN : refsN) {
                for (String refNS : transN.getItems()) {
                    DReference refN = dictionary_inverse.ceiling(new DReference(refNS, transN.type));

                    refD.unlink(entry, refN);
                    refN.unlink(entry, refD);

                    if (refN.translations.isEmpty()) {
                        dictionary_inverse.remove(refN);
                    }

                }
            }

            if (refD.translations.isEmpty()) {
                dictionary.remove(refD);
            }

        }
    }

    public int[] getTypeCounter() {
        return type_counter;
    }

    public DReferences getReferences(boolean inverse) {
        TreeSet<DReference> dictionary = this.dictionary;
        if (inverse) {
            dictionary = this.dictionary_inverse;
        }
        return new DReferences(dictionary);
    }

    public DReferences getPhrasalVerbs(boolean inverse) {
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

    public DReferences getVerbs(boolean inverse) {
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

    public TreeSet<DReference> getDictionary(boolean inverse) {
        if (inverse) {
            return dictionary_inverse;
        }
        return dictionary;
    }

    public TreeSet<DReference> getDictionaryAt(boolean inverse, char at) {
        TreeSet<DReference> dictionary = this.dictionary;
        if (inverse) {
            dictionary = this.dictionary_inverse;
        }
        DReference ceiling = dictionary.ceiling(new DReference("" + at, -1));
        if (ceiling == null || ceiling.name.charAt(0) != at) {
            return new TreeSet<DReference>();
        }
        String next = "" + (char) (at + 1);
        if (at >= 'Z') {
            switch (at) {
                case 'Z':
                    next = "ZÖÖ";
                    break;
                case 'Ä':
                    next = "ÄÖÖ";
                    break;
                case 'Å':
                    next = "ÅÖÖ";
                    break;
                case 'Ñ':
                    next = "ÑÖÖ";
                    break;
                case 'Ö':
                    next = "ÖÖÖ";
                    break;
            }
        }
        DReference floor = dictionary.floor(new DReference(next, -1));
        if (floor == null) {
            return new TreeSet<DReference>();
        }
        return (TreeSet<DReference>) dictionary.subSet(ceiling, true, floor, true);
    }

    public DReference getReference(boolean inverse, String name) {
        TreeSet<DReference> dictionary = this.dictionary;
        if (inverse) {
            dictionary = this.dictionary_inverse;
        }
        DReference candidate = dictionary.ceiling(new DReference(name, -1));
        if (candidate != null && candidate.name.equals(name)) {
            return candidate;
        }
        return null;
    }

    @Override
    public int compare(DReference lhs, DReference rhs) {
        int comp = collator.compare(lhs.name, rhs.name);
        return comp != 0 ? comp : Integer.valueOf(lhs.getType()).compareTo(rhs.getType());
    }

    private void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##Dictionary##", text);
    }

}
