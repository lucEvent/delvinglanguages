package com.delvinglanguages.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import com.delvinglanguages.R;

public class IDDelved {

	private static final String DEBUG = "##IDDelved##";

	private static final Character CAPS[][] = {
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
					'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
					'Z' },// NOTDETECTED
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
					'N', 'Ñ', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
					'Y', 'Z' },// ES
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
					'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
					'Z' },// EN
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
					'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
					'Z', 'Ä', 'Ö', 'Å' },// SV
			{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
					'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
					'Z', 'Ä', 'Ö', 'Å' },// FI
			{ 'A', 'B', 'C', 'Ç', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
					'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
					'Y', 'Z' } // CA

	};

	// Language ID
	public static final int NOTDETECTED = 0;
	public static final int ES = 1;
	public static final int EN = 2;
	public static final int SV = 3;
	public static final int FI = 4;
	public static final int CA = 5;
	// public static final int IT = 4;
	// public static final int FR = 6;
	// public static final int PO = 7;

	// Setting masks
	public static final int MASK_PH = 0x1;
	public static final int MASK_ADJ = 0x2;
	public static final int MASK_ESP_CHARS = 0x4;

	public final int CODE;

	protected Datos datos;

	protected ArrayList<Nota> almacen;

	/** **************** CREADORAS **************** **/
	public IDDelved(Datos data) {
		datos = data;
		if (datos.delved == null) {
			datos.delved = this;
			datos.nativo = new IDNativo(data);
		}
		String tn = datos.nombre.toLowerCase();
		if (tn.equals("español") || tn.equals("spanish")
				|| tn.equals("spanska")) {
			CODE = ES;
		} else if (tn.equals("inglés") || tn.equals("ingles")
				|| tn.equals("english") || tn.equals("engelska")) {
			CODE = EN;
		} else if (tn.equals("sueco") || tn.equals("swedish")
				|| tn.equals("svenska")) {
			CODE = SV;
		} else if (tn.equals("finlandés") || tn.equals("finnish")
				|| tn.equals("suomi") || tn.equals("finska")) {
			CODE = FI;
		} else {
			CODE = NOTDETECTED;
		}
	}

	/** *************** CONSULTORAS **************** **/
	public int getID() {
		return datos.id;
	}

	public String getName() {
		return datos.nombre;
	}

	public Word getPalabra(String name) {
		return getPalabra(name,
				datos.dictionary.dictionary_D_to_N.get(name.charAt(0)));
	}

	protected Word getPalabra(String name, TreeSet<DReference> sub) {
		if (sub != null) {
			for (DReference temp : sub) {
				if (temp.name.equals(name)) {
					return temp.links.get(0).owner;
				}
			}
		}
		return null;
	}

	public Word getPalabra(int id) {
		Iterator<Word> it = datos.palabrasDelved.iterator();
		while (it.hasNext()) {
			Word temp = it.next();
			if (temp.id == id) {
				return temp;
			}
		}
		return null;
	}

	public DReference getReference(String name) {
		TreeSet<DReference> sub = datos.dictionary.dictionary_D_to_N.get(name
				.charAt(0));
		if (sub != null) {
			Iterator<DReference> it = sub.iterator();
			while (it.hasNext()) {
				DReference temp = it.next();
				if (temp.name.equals(name)) {
					return temp;
				}
			}
		}
		return null;
	}

	public boolean contains(Word p) {
		return datos.palabrasDelved.contains(p);
	}

	public ArrayList<Word> getPalabras() {
		return datos.palabrasDelved;
	}

	public ArrayList<DReference> getReferences() {
		ArrayList<DReference> res = new ArrayList<DReference>();
		TreeSet<Character> keys = new TreeSet<Character>(
				datos.dictionary.dictionary_D_to_N.keySet());
		for (Character key : keys) {
			TreeSet<DReference> sub = datos.dictionary.dictionary_D_to_N
					.get(key);
			for (DReference ref : sub) {
				res.add(ref);
			}
		}
		return res;
	}

	public Estadisticas getEstadisticas() {
		return datos.estadistics;
	}

	public ArrayList<Word> getPapelera() {
		return datos.papelera;
	}

	public ArrayList<Nota> getAlmacen() {
		return almacen;
	}

	public HashMap<Character, TreeSet<DReference>> getDiccionario() {
		return datos.dictionary.dictionary_D_to_N;
	}

	public int[] getNumTypes() {
		return datos.dictionary.types;
	}

	public ArrayList<Test> getTests() {
		return datos.tests;
	}

	public String getSettings() {
		return Integer.toString(datos.settings);
	}

	public Datos getDatos() {
		return datos;
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

	public ArrayList<DReference> getVerbs() {
		ArrayList<DReference> verbs = new ArrayList<DReference>();
		TreeSet<Character> keys = new TreeSet<Character>(
				datos.dictionary.dictionary_D_to_N.keySet());
		for (Character key : keys) {
			TreeSet<DReference> sub = datos.dictionary.dictionary_D_to_N
					.get(key);
			for (DReference ref : sub) {
				if (ref.isVerb()) {
					verbs.add(ref);
				}
			}
		}
		return verbs;
	}

	public int getTensesArray() {
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

	public int getSubjectArray() {
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

	public boolean isLoaded() {
		return !(datos.palabrasDelved == null || almacen == null);
	}

	public boolean hasEntries() {
		return !datos.palabrasDelved.isEmpty();
	}

	/** *************** MODIFICADORAS **************** **/
	// Totales
	public void setEstadisticas(Estadisticas est) {
		datos.estadistics = est;
	}

	public void setPalabras(ArrayList<Word> words) {
		datos.papelera = new ArrayList<Word>();
		for (int i = 0; i < words.size(); i++) {
			Word temp = words.get(i);
			if (temp.isThrown()) {
				datos.papelera.add(temp);
				words.remove(i);
				i--;
			}
		}
		datos.palabrasDelved = words;
		datos.createDictionary(CAPS[CODE], CAPS[datos.nativo.CODE]);
		datos.nativo.setPalabras(words);
	}

	public void setStore(ArrayList<Nota> store) {
		almacen = new ArrayList<Nota>();
		for (int i = 0; i < store.size(); i++) {
			Nota nota = store.get(i);
			if (nota.forIdioma()) {
				almacen.add(nota);
				store.remove(i);
				i--;
			}
		}
		datos.nativo.setStore(store);
	}

	public void setName(String newname) {
		datos.nombre = newname;
	}

	public void setTests(ArrayList<Test> tests) {
		datos.tests = tests;
	}

	// Parciales
	public void setSettings(boolean state, int mask) {
		if (state) {
			datos.settings |= mask;
		} else {
			if ((datos.settings & mask) != 0) {
				datos.settings ^= mask;
			}
		}
	}

	public static int configure(boolean phrasal, boolean adjective,
			boolean special) {
		int settings = 0;
		if (phrasal)
			settings |= MASK_PH;
		if (adjective)
			settings |= MASK_ADJ;
		if (special)
			settings |= MASK_ESP_CHARS;
		return settings;
	}

	public void addPalabra(Word p) {
		datos.indexa(p);
	}

	public void addPalabra(Nota nota) {
		almacen.add(0, nota);
	}

	public void tirarPalabra(Word thrown) {
		datos.desindexa(thrown);
		datos.papelera.add(thrown);
	}

	public void addTest(Test t) {
		datos.tests.add(t);
	}

	// Parciales remove
	public void removePalabra(int position) {
		datos.papelera.remove(position);
	}

	public void removePalabraFromAlmacen(Nota nota) {
		almacen.remove(nota);
	}

	public void vaciarPapelera() {
		datos.papelera.clear();
	}

	// Restores
	public void restorePalabra(int binposition) {
		Word p = datos.papelera.remove(binposition);
		datos.indexa(p);
	}

	public void reindexar(Word p, String name, String trad, String spell,
			int type) {
		datos.desindexa(p);
		p.setChanges(name, trad, spell, type);
		datos.indexa(p);
	}

	// De clase
	public boolean isIdiomaNativo() {
		return false;
	}

	public IDNativo getNativo() {
		return datos.nativo;
	}

	public IDDelved getDelved() {
		return datos.delved;
	}

	public boolean isbusy() {
		return !datos.dictionary.dictionariesCreated;
	}
	/** ******************* NUEVAS A DEBUGGAR **********************/

	private PhrasalVerbs phrasalverbs;

	public static String[] preps = { "About", "Across", "After", "Against",
			"Ahead", "Along", "Apart", "Around", "As", "Aside", "At", "Away",
			"Back", "By", "Down", "For", "Forth", "Forward", "From", "In",
			"Into", "It", "Of", "Off", "On", "Onto", "Out", "Over", "Round",
			"Through", "To", "Together", "Towards", "Under", "Up", "Upon",
			"With" };

	public void analizePhrasals() {
		if (phrasalverbs != null)
			return;
		new Thread(new Runnable() {

			@Override
			public void run() {
				phrasalverbs = new PhrasalVerbs(datos.palabrasDelved, preps);
			}
		}).start();
	}

	public TreeMap<String, Boolean[]> getPhrasals() {
		return phrasalverbs.phrasals;
	}

	public ArrayList<DReference> getPhrasalVerbs() {
		ArrayList<DReference> phvs = new ArrayList<DReference>();
		TreeSet<Character> keys = new TreeSet<Character>(
				datos.dictionary.dictionary_D_to_N.keySet());
		for (Character key : keys) {
			TreeSet<DReference> sub = datos.dictionary.dictionary_D_to_N
					.get(key);
			for (DReference ref : sub) {
				if (ref.isPhrasalVerb()) {
					phvs.add(ref);
				}
			}
		}
		return phvs;
	}

}
