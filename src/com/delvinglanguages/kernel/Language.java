package com.delvinglanguages.kernel;

import java.text.Collator;
import java.util.Locale;
import java.util.TreeMap;
import java.util.TreeSet;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.DrawerWords;
import com.delvinglanguages.kernel.set.Tests;
import com.delvinglanguages.kernel.set.Themes;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.settings.Settings;

public class Language {

	private static final Character CAPS[][] = {
			/** UK **/
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' },
			/** US **/
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' },
			/** SV **/
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Ä',
					'Ö', 'Å' },
			/** FI **/
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Ä',
					'Ö', 'Å' },
			/** ES **/
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' },
			/** CA **/
			{ 'A', 'B', 'C', 'Ç', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' }, // CA
			/** BA **/
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' },
			/** CZ **/
			{ 'A', 'B', 'C', 'Č', 'D', 'Ď', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ň', 'O', 'P', 'Q', 'R', 'Ř', 'S', 'Š', 'T', 'Ť', 'U',
					'V', 'W', 'X', 'Y', 'Z', 'Ž' },
			/** DA **/
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Æ',
					'Ø', 'Å' },
			/** DU **/
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' },
			/** EST **/
			{ 'A', 'B', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'R', 'S', 'Š', 'Z', 'Ž', 'T', 'U', 'V', 'Õ', 'Ä', 'Ö', 'Ü' },
			/** FR **/
			{ 'A', 'B', 'C', 'Ç', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' }, // CA
			/** GE **/
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' },
			/** GR **/
			{ 'Α', 'Β', 'Γ', 'Δ', 'Ε', 'Ζ', 'Η', 'Θ', 'Ι', 'Κ', 'Λ', 'Μ', 'Ν', 'Ξ', 'Ο', 'Π', 'Ρ', 'Σ', 'Τ', 'Υ', 'Φ', 'Χ', 'Ψ', 'Ω' },
			/** IT **/
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'Z' },
			/** NO **/
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Æ',
					'Ø', 'Å' },
			/** PO **/
			{ 'A', 'Ą', 'B', 'C', 'Ć', 'D', 'E', 'Ę', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'Ł', 'M', 'N', 'Ń', 'O', 'Ó', 'P', 'R', 'S', 'Ś', 'T', 'U',
					'W', 'Y', 'Z', 'Ź', 'Ż' },
			/** RU **/
			{ 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ',
					'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я' } };

	// Language ID
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

	public static Collator collator;

	protected Locale locale;

	public int CODE;

	protected Datos datos;

	protected DrawerWords drawer_words;

	public Language(int code, Datos data) {
		this.CODE = code;
		datos = data;
		if (datos.delved == null) {
			datos.delved = this;
			setLocale();
			datos.nativo = new LanguageR(Settings.NativeLanguageCode, datos);
		}
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

	/** *********************** Getters *********************** **/
	public int getID() {
		return datos.id;
	}

	public String getName() {
		return datos.language_delved_name;
	}

	public Word getPalabra(String name) {
		return getPalabra(name, datos.dictionary.getDictionaryAt(false, name.charAt(0)));
	}

	protected Word getPalabra(String name, TreeSet<DReference> subD) {
		if (subD != null) {
			for (DReference ref : subD) {
				if (ref.name.equals(name)) {
					return ref.words.get(0);
				}
			}
		}
		return null;
	}

	public Word getPalabra(int id) {
		for (Word word : datos.words) {
			if (word.id == id) {
				return word;
			}
		}
		return null;
	}

	public DReference getReference(String name) {
		return datos.dictionary.getReference(false, name);
	}

	public DReferences getReferences() {
		return datos.dictionary.getReferences(false);
	}

	public boolean contains(Word word) {
		return datos.words.contains(word);
	}

	public Words getWords() {
		return datos.words;
	}

	public Estadisticas getStatistics() {
		return datos.statistics;
	}

	public Words getRemovedWords() {
		return datos.removed_words;
	}

	public DrawerWords getDrawerWords() {
		return drawer_words;
	}

	public int[] getTypeCounter() {
		return datos.dictionary.getTypeCounter();
	}

	public Tests getTests() {
		return datos.tests;
	}

	public int getSettings() {
		return datos.settings;
	}

	public Datos getData() {
		return datos;
	}

	public TreeSet<DReference> getDiccionary() {
		return datos.dictionary.getDictionary(false);
	}

	public TreeSet<DReference> getDiccionaryAt(char charAt) {
		return datos.dictionary.getDictionaryAt(false, charAt);
	}

	public Character[] getDictionaryIndexs() {
		return CAPS[CODE];
	}

	public boolean getSettings(int mask) {
		boolean res;
		if ((datos.settings & mask) != 0) {
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
		return datos.dictionary.getVerbs(false);
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
		} else if (CODE == Language.FI) {
			res = R.array.fi_subjects;
		} else {
			res = R.array.en_subjects;
		}
		return res;
	}

	public Themes getThemes() {
		return datos.themes;
	}

	public boolean isLoaded() {
		return !(datos.words == null || drawer_words == null || datos.removed_words == null);
	}

	public boolean hasEntries() {
		return !datos.words.isEmpty();
	}

	/** *********************** Setters *********************** **/

	public void setStatistics(Estadisticas e) {
		datos.statistics = e;
	}

	public void setWords(Words words) {
		datos.words = words;
		datos.createDictionary();
	}

	public void setDrawerWords(DrawerWords drawer_words) {
		this.drawer_words = new DrawerWords();
		datos.nativo.drawer_words = new DrawerWords();
		for (DrawerWord DrawerWord : drawer_words) {
			if (DrawerWord.forIdioma()) {
				this.drawer_words.add(DrawerWord);
			} else {
				datos.nativo.drawer_words.add(DrawerWord);
			}
		}
	}

	public void setRemovedWords(Words removed_words) {
		datos.removed_words = removed_words;
	}

	public void setName(String newname) {
		datos.language_delved_name = newname;
	}

	public void setTests(Tests tests) {
		datos.tests = tests;
	}

	public void setThemes(Themes themes) {
		datos.themes = themes;
	}

	public void setSettings(boolean state, int mask) {
		if (state) {
			datos.settings |= mask;
		} else {
			if ((datos.settings & mask) != 0) {
				datos.settings ^= mask;
			}
		}
	}

	public DReferences getPhrasalVerbs() {
		return datos.dictionary.getPhrasalVerbs(false);
	}

	public void initCollator() {
		collator = Collator.getInstance(locale);
		collator.setStrength(Collator.PRIMARY);
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

	/** *********************** Adders *********************** **/

	public void addWord(Word word) {
		datos.indexWord(word);
	}

	public void addWord(DrawerWord DrawerWord) {
		drawer_words.add(0, DrawerWord);
	}

	public void addTest(Test t) {
		datos.tests.add(t);
	}

	public void addTheme(Theme theme) {
		datos.themes.add(theme);
	}

	/** *********************** Deletes *********************** **/

	public void removeWord(Word word) {
		datos.unindexWord(word);
		datos.removed_words.add(word);
	}

	public void deleteWord(int position) {
		datos.removed_words.remove(position);
	}

	public void deleteDrawerWord(DrawerWord word) {
		drawer_words.remove(word);
	}

	public void deleteAllRemovedWords() {
		datos.removed_words.clear();
	}

	/** *********************** Restores *********************** **/
	public void restoreWord(int position) {
		datos.indexWord(datos.removed_words.remove(position));
	}

	public void updateWord(Word word, String name, Translations translations, String pronunciation) {
		datos.unindexWord(word);
		word.setChanges(name, translations, pronunciation);
		datos.indexWord(word);
	}

	public boolean isNativeLanguage() {
		return false;
	}

	public LanguageR getNative() {
		return datos.nativo;
	}

	public Language getDelved() {
		return datos.delved;
	}

	public boolean isDictionaryCreated() {
		return datos.dictionary.dictionaryCreated;
	}

	private void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##Language##", text);
	}

}
