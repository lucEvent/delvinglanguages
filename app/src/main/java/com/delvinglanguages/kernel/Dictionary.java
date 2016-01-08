package com.delvinglanguages.kernel;

import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.kernel.util.AppFormat;
import com.delvinglanguages.settings.Settings;

import java.text.Collator;
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

        String[] refsD = AppFormat.formatArray(entry.getName());
        for (String refDS : refsD) {
            if (refDS.length() == 0) {
                continue;
            }
            DReference refD = new DReference(refDS, entry.pronunciation, entry.priority);
            if (!dictionary.add(refD)) {
                refD = dictionary.ceiling(refD);
            }

            refD.addTranslation(entry, entry.getInflexions());
        }

        for (Inflexion inflexion : entry.getInflexions()) {
            Inflexion infN = new Inflexion(inflexion.getInflexions(), refsD, inflexion.getType());
            for (String refNS : inflexion.getTranslations()) {
                if (refNS.length() == 0) {
                    continue;
                }
                DReference refN = new DReference(refNS, "", entry.priority);
                if (!dictionary_inverse.add(refN)) {
                    refN = dictionary_inverse.ceiling(refN);
                }
                refN.addTranslation(entry, infN);

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

        String[] refsD = AppFormat.formatArray(entry.getName());
        for (String refDS : refsD) {
            DReference refD = dictionary.ceiling(new DReference(refDS));

            refD.removeTranslation(entry, entry.getInflexions());

            if (refD.inflexions.isEmpty()) {
                dictionary.remove(refD);
            }
        }

        for (Inflexion inflexion : entry.getInflexions()) {
            for (String refNS : inflexion.getTranslations()) {
                DReference refN = dictionary_inverse.ceiling(new DReference(refNS));

                refN.removeTranslation(entry, inflexion);

                if (refN.inflexions.isEmpty()) {
                    dictionary_inverse.remove(refN);
                }

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
        DReference ceiling = dictionary.ceiling(new DReference("" + at));
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
        DReference floor = dictionary.floor(new DReference(next));
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
        DReference candidate = dictionary.ceiling(new DReference(name));
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
