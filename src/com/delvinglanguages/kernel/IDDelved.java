package com.delvinglanguages.kernel;

import java.text.Collator;
import java.util.Locale;
import java.util.TreeMap;
import java.util.TreeSet;

import com.delvinglanguages.R;
import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.Notas;
import com.delvinglanguages.kernel.set.Tests;
import com.delvinglanguages.kernel.set.Themes;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;

public class IDDelved {

	private static final String DEBUG = "##IDDelved##";

	private static final Character CAPS[][] = {
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' },// ES
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' },// EN
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Ä',
					'Ö', 'Å' },// SV
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Ä',
					'Ö', 'Å' },// FI
			{ 'A', 'B', 'C', 'Ç', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' } // CA
	};

	public static Collator collator;

	// Language ID
	public static final int NOTDETECTED = 0;
	public static final int EN = 0;
	public static final int ES = 1;
	public static final int SV = 2;
	public static final int FI = 3;
	public static final int CA = 4;

	// Setting masks
	public static final int MASK_PH = 0x1;
	public static final int MASK_ADJ = 0x2;
	public static final int MASK_ESP_CHARS = 0x4;

	protected Locale locale;

	public int CODE;

	protected Datos datos;

	protected Notas drawer_words;

	protected String[] LANGUAGES = { "español", "spanish", "spanska", "inglés", "ingles", "english", "engelska", "sueco", "swedish", "svenska",
			"finlandés", "finnish", "suomi", "finska" };
	protected int[] CODES = { ES, ES, ES, EN, EN, EN, EN, SV, SV, SV, FI, FI, FI, FI };

	public IDDelved(Datos data) {
		datos = data;

		if (datos.delved == null) {

			datos.delved = this;
			setCodeAndLocale(datos.language_delved_name);

			datos.nativo = new IDNativo(datos);
		}

	}

	protected void setCodeAndLocale(String langName) {
		String lang_name = langName.toLowerCase();
		CODE = NOTDETECTED;
		for (int i = 0; i < LANGUAGES.length; i++) {
			if (lang_name.equals(LANGUAGES[i])) {
				CODE = CODES[i];
				break;
			}
		}
		switch (CODE) {
		case EN:
			locale = new Locale("en", "GB");
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

	protected Word getPalabra(String name, TreeSet<DReference> sub) {
		if (sub != null) {
			for (DReference temp : sub) {
				if (temp.getName().equals(name)) {
					return temp.links.get(0).owner;
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

	public Notas getDrawerWords() {
		return drawer_words;
	}

	public int[] getTypeCounter() {
		return datos.dictionary.getTypeCounter();
	}

	public Tests getTests() {
		return datos.tests;
	}

	public String getSettings() {
		return Integer.toString(datos.settings);
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

	public DReferences getVerbs() {
		return datos.dictionary.getVerbs(false);
	}

	public int getTensesArrayResId() {
		int res;
		if (CODE == IDDelved.ES) {
			res = R.array.es_tenses;
		} else if (CODE == IDDelved.EN) {
			res = R.array.en_tenses;
		} else if (CODE == IDDelved.SV) {
			res = R.array.sv_tenses;
		} else if (CODE == IDDelved.FI) {
			res = R.array.fi_tenses;
		} else {
			res = R.array.en_tenses;
		}
		return res;
	}

	public int getSubjectArrayResId() {
		int res;
		if (CODE == IDDelved.ES) {
			res = R.array.es_subjects;
		} else if (CODE == IDDelved.EN) {
			res = R.array.en_subjects;
		} else if (CODE == IDDelved.SV) {
			res = R.array.sv_subjects;
		} else if (CODE == IDDelved.FI) {
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
		datos.createDictionary(CAPS[CODE], CAPS[datos.nativo.CODE]);
	}

	public void setDrawerWords(Notas drawer_words) {
		this.drawer_words = new Notas();
		datos.nativo.drawer_words = new Notas();
		for (Nota nota : drawer_words) {
			if (nota.forIdioma()) {
				this.drawer_words.add(nota);
			} else {
				datos.nativo.drawer_words.add(nota);
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

	public void initCollator() {
		collator = Collator.getInstance(locale);
		collator.setStrength(Collator.PRIMARY);
	}

	public static int configure(boolean phrasal, boolean adjective, boolean special) {
		int settings = 0;
		if (phrasal)
			settings |= MASK_PH;
		if (adjective)
			settings |= MASK_ADJ;
		if (special)
			settings |= MASK_ESP_CHARS;
		return settings;
	}

	/** *********************** Adders *********************** **/

	public void addWord(Word word) {
		datos.indexWord(word);
	}

	public void addWord(Nota nota) {
		drawer_words.add(0, nota);
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

	public void deleteDrawerWord(Nota nota) {
		drawer_words.remove(nota);
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

	public IDNativo getNative() {
		return datos.nativo;
	}

	public IDDelved getDelved() {
		return datos.delved;
	}

	public boolean isDictionaryCreated() {
		return datos.dictionary.dictionaryCreated;
	}

	/** ******************* NUEVAS A DEBUGGAR **********************/

	private PhrasalVerbs phrasalverbs;

	public static String[] preps = { "About", "Across", "After", "Against", "Ahead", "Along", "Apart", "Around", "As", "Aside", "At", "Away", "Back",
			"By", "Down", "For", "Forth", "Forward", "From", "In", "Into", "It", "Of", "Off", "On", "Onto", "Out", "Over", "Round", "Through", "To",
			"Together", "Towards", "Under", "Up", "Upon", "With" };

	public void analizePhrasals() {
		if (phrasalverbs != null)
			return;
		new Thread(new Runnable() {

			@Override
			public void run() {
				phrasalverbs = new PhrasalVerbs(datos.words, preps);
			}
		}).start();
	}

	public TreeMap<String, Boolean[]> getPhrasals() {
		return phrasalverbs.phrasals;
	}

	public DReferences getPhrasalVerbs() {
		return datos.dictionary.getPhrasalVerbs(false);
	}

}
