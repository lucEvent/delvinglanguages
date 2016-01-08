package com.delvinglanguages.kernel;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.DrawerWords;
import com.delvinglanguages.kernel.set.Inflexions;
import com.delvinglanguages.kernel.set.Tests;
import com.delvinglanguages.kernel.set.Themes;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.settings.Settings;

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

    // Setting masks
    public static final int MASK_PH = 0x1;
    public static final int MASK_ADJ = 0x2;
    public static final int MASK_ESP_CHARS = 0x4;

    private Locale locale;

    public int CODE;

    public final int id;

    public String language_delved_name;
    public String language_native_name;

    public Words words;
    public Words removed_words;
    public DrawerWords drawer_words;

    public Dictionary dictionary;

    public Estadisticas statistics;

    public Tests tests;

    public Themes themes;

    public int settings;

    public Language(int id, int code, String delved_name, String native_name, int settings) {
        this.id = id;
        this.CODE = code;
        this.language_delved_name = delved_name;
        this.language_native_name = native_name;
        this.settings = settings;

        setLocale();
    }

    protected void setLocale() {
        switch (CODE) {
            case UK:
                locale = new Locale("en", "GB");
                break;
            case US:
                locale = new Locale("en", "US");
                break;
            case SV:
                locale = new Locale("sv", "SE", "SE");
                break;
            case ES:
                locale = new Locale("es", "ES", "ES");
                break;
            case FI:
                locale = new Locale("fi", "FI", "FI");
                break;
            default:
                locale = new Locale("en", "GB");
        }
    }

    /**
     * ********************** Getters ***********************
     **/

    public Words getWords(String word) {
        return getWords(word, dictionary.getDictionaryAt(false, word.charAt(0)));
    }

    private Words getWords(String name, TreeSet<DReference> subD) {
        if (subD != null) {
            for (DReference ref : subD) {
                if (ref.name.equals(name)) {
                    return new Words(ref.appearances);
                }
            }
        }
        return null;
    }

    public Word getWordById(int id) {
        for (Word word : words) {
            if (word.id == id) {
                return word;
            }
        }
        return null;
    }

    public DReference getReference(String name) {
        return dictionary.getReference(false, name);
    }

    public DReferences getReferences() {
        return dictionary.getReferences(false);
    }

    public boolean contains(Word word) {
        return words.contains(word);
    }

    public int[] getTypeCounter() {
        return dictionary.getTypeCounter();
    }

    public TreeSet<DReference> getDiccionary() {
        return dictionary.getDictionary(false);
    }

    public TreeSet<DReference> getDiccionaryAt(char charAt) {
        return dictionary.getDictionaryAt(false, charAt);
    }

    public Character[] getDictionaryIndexes() {
        return CAPS[CODE];
    }

    public boolean getSettings(int mask) {
        boolean res;
        if ((settings & mask) != 0) {
            res = true;
        } else {
            res = false;
        }
        return res;
    }

    public Locale getLocale() {
        return locale;
    }

    public DReferences getVerbs() {
        return dictionary.getVerbs(false);
    }

    public int getTensesArrayResId() {
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

    public int getSubjectArrayResId() {
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

    public boolean isLoaded() {
        return words != null && drawer_words != null && removed_words != null;
    }

    public boolean hasEntries() {
        return !words.isEmpty();
    }

    /**
     * ********************** Setters ***********************
     **/

    public void setStatistics(Estadisticas e) {
        statistics = e;
    }

    public void setWords(Words words) {
        this.words = words;
        this.dictionary = new Dictionary(Collator.getInstance(locale), words);
    }

    public void setDrawerWords(DrawerWords drawer_words) {
        this.drawer_words = drawer_words;
    }

    public void setRemovedWords(Words removed_words) {
        this.removed_words = removed_words;
    }

    public void setName(String newname) {
        this.language_delved_name = newname;
    }

    public void setTests(Tests tests) {
        this.tests = tests;
    }

    public void setThemes(Themes themes) {
        this.themes = themes;
    }

    public void setSettings(boolean state, int mask) {
        if (state) {
            this.settings |= mask;
        } else {
            if ((this.settings & mask) != 0) {
                this.settings ^= mask;
            }
        }
    }

    public DReferences getPhrasalVerbs() {
        return dictionary.getPhrasalVerbs(false);
    }

    public static int configure(boolean phrasal, boolean adjective, boolean specialCharacters) {
        int settings = 0;
        if (phrasal)
            settings |= MASK_PH;
        if (adjective)
            settings |= MASK_ADJ;
        if (specialCharacters)
            settings |= MASK_ESP_CHARS;
        return settings;
    }

    public void updateCode(int code) {
        this.CODE = code;
        setLocale();
    }

    /**
     * ********************** Adders ***********************
     **/

    public void addWord(Word word) {
        words.add(word);
        dictionary.addEntry(word);
    }

    public void addWord(DrawerWord word) {
        drawer_words.add(0, word);
    }

    public void addTest(Test t) {
        tests.add(t);
    }

    public void addTheme(Theme theme) {
        themes.add(theme);
    }

    /**
     * ********************** Deletes ***********************
     **/

    public void removeWord(Word word) {
        words.remove(word);
        dictionary.removeEntry(word);

        removed_words.add(word);
    }

    public void deleteWord(int position) {
        removed_words.remove(position);
    }

    public void deleteDrawerWord(DrawerWord word) {
        drawer_words.remove(word);
    }

    public void deleteAllRemovedWords() {
        removed_words.clear();
    }

    /**
     * ********************** Restores ***********************
     **/
    public void restoreWord(int position) {
        Word word = removed_words.remove(position);

        words.add(word);
        dictionary.addEntry(word);
    }

    public void updateWord(Word word, String name, Inflexions inflexions, String pronunciation) {
        words.remove(word);
        dictionary.removeEntry(word);
        word.update(name, inflexions, pronunciation);
        words.add(word);
        dictionary.addEntry(word);
    }

    public boolean isDictionaryCreated() {
        return dictionary.dictionaryCreated;
    }

    private void debug(String text) {
        if (Settings.DEBUG)
            android.util.Log.d("##Language##", text);
    }

}
