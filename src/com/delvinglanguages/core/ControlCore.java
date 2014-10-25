package com.delvinglanguages.core;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.util.Log;

import com.delvinglanguages.data.ControlDB;
import com.delvinglanguages.data.ControlDisco;
import com.delvinglanguages.debug.Recover;

public class ControlCore {

	private static final String DEBUG = "##ControlCore##";

	// Codes to send data between Activities
	public static final String sendDReference = "sendDR";
	public static final String sendPalabra = "sendW";
	public static final String sendCharacter = "sendChar";
	public static final String sendType = "sendTy";

	// ---------------------------------------------
	private static ControlDB database;
	private static ControlDisco disco;

	private static ArrayList<IDDelved> idiomas;
	private static IDDelved actualLang;

	// public static Character subdiccionario;
	public static ArrayList<Palabra> integrateWords;
	public static IDDelved integrateLanguage;

	// Variables para PalabraActivity
	public static Nota notaToModify;

	public static Test testActual;

	private static Context context;

	public ControlCore(Context context) {
		this.context = context;
		initializeAll();

		// Recover rec = new Recover();
		// if (getIdiomas().size() == 0) {
		// rec.recoverDatafromCopy();
		// }
		// rec.makeCopy();
	}

	private static void initializeAll() {
		if (database == null) {
			database = new ControlDB(context);
		}
		if (idiomas == null) {
			idiomas = database.readLanguages();
		}
		if (disco == null) {
			disco = new ControlDisco();
		}
	}

	public static IDDelved getIdiomaActual(Context context) {
		if (actualLang == null) {
			new ControlCore(context);
			actualLang = idiomas.get(disco.getLastLanguage());
		}
		return actualLang;
	}

	public static void setIdiomaActual(int position) {
		actualLang = idiomas.get(position);
		disco.saveLastLaguage(position);
	}

	public static void setIdiomaActual(IDDelved idioma) {
		actualLang = idioma;
		disco.saveLastLaguage(idiomas.indexOf(idioma));
	}

	public static int getCount() {
		return idiomas.size();
	}

	public static ArrayList<IDDelved> getIdiomas() {
		return idiomas;
	}

	public static ArrayList<Palabra> getPalabras() {
		loadwords();
		return actualLang.getPalabras();
	}

	public static ArrayList<DReference> getReferences() {
		return actualLang.getReferences();
	}

	public static Palabra getPalabra(int id) {
		return actualLang.getPalabra(id);
	}

	public static DReference getReference(String name) {
		return actualLang.getReference(name);
	}

	public static ArrayList<Palabra> getPapelera() {
		loadwords();
		return actualLang.getPapelera();
	}

	public static ArrayList<Nota> getStore() {
		loadwords();
		return actualLang.getAlmacen();
	}

	public static TreeSet<DReference> getSubdiccionario(Character cap) {
		return actualLang.getDiccionario().get(cap);
	}

	public static ArrayList<Test> getTests() {
		ArrayList<Test> result = actualLang.getTests();
		if (result == null) {
			result = database.readTests(actualLang.getID());
			actualLang.setTests(result);
		}
		return result;
	}

	private static void loadwords() {
		if (!actualLang.isLoaded()) {
			actualLang.setPalabras(database.readWords(actualLang.getID()));
			actualLang.setStore(database.readStore(actualLang.getID()));
		}
	}

	private static void loadwords(IDDelved idioma) {
		IDDelved temp = actualLang;
		actualLang = idioma;
		loadwords();
		actualLang = temp;
	}

	public static void loadLanguage(boolean withDictionary) {
		loadwords();
		actualLang.createDictionaries();
	}

	public static void switchDictionary() {
		if (actualLang.isIdiomaNativo()) {
			actualLang = actualLang.getDelved();
		} else {
			actualLang = actualLang.getNativo();
		}
	}

	public static void addIdioma(String name) {
		IDDelved newLang = database.insertLanguage(name);
		idiomas.add(newLang);
	}

	public static void addPalabra(String nom, String trad, String spell,
			int type) {
		nom = Palabra.format(nom);
		trad = Palabra.format(trad);
		if (actualLang.isIdiomaNativo()) {
			String temp = nom;
			nom = trad;
			trad = temp;
			spell = "";
		}
		actualLang.addPalabra(database.insertWord(nom, trad,
				actualLang.getID(), spell, type));
	}

	public static void addTest(ArrayList<DReference> words) {
		testActual = new Test(words);
	}

	public static void saveTestActual() {
		database.updateTest(testActual);
	}

	public static void saveTestActual(String name) {
		int tid = database.insertTest(name, actualLang.getID(),
				testActual.encapsulate());
		testActual.id = tid;
		testActual.name = name;
		actualLang.addTest(testActual);
	}

	public static void removeTestActual() {
		actualLang.getTests().remove(testActual);
		database.removeTest(testActual.id);
	}

	public static void updatePalabra(Palabra pal, String name, String trad,
			String spell, int type) {
		actualLang.reindexar(pal, name, trad, spell, type);
		if (actualLang.isIdiomaNativo()) {
			pal = pal.cloneReverse();
		}
		database.updateWord(pal);
	}

	public static void removeLanguage() {
		database.removeLanguage(actualLang.getID());
		if (!actualLang.isIdiomaNativo()) {
			idiomas.remove(actualLang);
		} else {
			int langId = actualLang.getID();
			for (int i = 0; i < idiomas.size(); i++) {
				if (idiomas.get(i).getID() == langId) {
					idiomas.remove(i);
					break;
				}
			}
		}
		actualLang = null;
	}

	public static void saveStatistics() {
		database.updateStatistics(actualLang.getEstadisticas());
	}

	public static void clearStatistics() {
		actualLang.getEstadisticas().clear();
		saveStatistics();
	}

	public static void throwPalabra(Palabra thrown) {
		database.throworrestoreWord(thrown);
		actualLang.tirarPalabra(thrown);
	}

	public static void restorePalabra(int binposition) {
		database.throworrestoreWord(actualLang.getPapelera().get(binposition));
		actualLang.restorePalabra(binposition);
	}

	public static void clearPapelera() {
		ArrayList<Palabra> bin = actualLang.getPapelera();
		for (int i = 0; i < bin.size(); i++) {
			Palabra p = bin.get(i);
			if (p.isVerb()) {
				database.removeTenses(p.id);
			}
		}
		actualLang.vaciarPapelera();
		database.clearTrash(actualLang.getID());
	}

	public static void addToStore(String s) {
		int type = actualLang.isIdiomaNativo() ? 1 : 0;

		actualLang.addPalabra(database.insertStoreWord(s, actualLang.getID(),
				type));
	}

	public static void addWordFromStore(String nombre, String trad,
			String spell, int type) {
		actualLang.removePalabraFromAlmacen(notaToModify);
		addPalabra(nombre, trad, spell, type);
		database.removeStoredWord(notaToModify.id);
	}

	public static void removeFromStore() {
		actualLang.removePalabraFromAlmacen(notaToModify);
		database.removeStoredWord(notaToModify.id);
	}

	public static void renameLanguage(String newname) {
		actualLang.setName(newname);
		database.updateLanguage(actualLang);
	}

	public static void setLangSettings(boolean status, int mask) {
		actualLang.setSettings(status, mask);
		database.updateLanguage(actualLang);
	}

	public static ArrayList<Palabra> integrateLanguage(int langdestinoposition) {
		integrateWords = new ArrayList<Palabra>();
		integrateLanguage = idiomas.get(langdestinoposition);
		IDDelved destino = integrateLanguage;
		loadwords(destino);
		destino.createDictionaries();
		while (!destino.datos.dictionariesCreated) {
		}

		// Copiando el Diccionario
		ArrayList<Palabra> fuentes = actualLang.getPalabras(); // Necesario
		for (int i = 0; i < fuentes.size(); ++i) {
			Palabra pinsert = fuentes.get(i);
			Palabra porig = destino.getPalabra(pinsert.getName());// Necesario
			if (porig == null) {
				destino.addPalabra(pinsert);
				database.integrateWord(pinsert, destino.getID());
			} else {
				integrateWords.add(porig);
				integrateWords.add(pinsert);
			}
		}

		// Copiando la warehouse
		ArrayList<Nota> notafuente = actualLang.getAlmacen();
		for (int i = 0; i < notafuente.size(); ++i) {
			destino.addPalabra(notafuente.get(i));
		}
		database.integrateStore(actualLang.getID(), destino.getID());
		return integrateWords;
	}

	public static ArrayList<DReference> getVerbs() {
		return actualLang.getVerbs();
	}

	public static Tense getTense(int verbId, int tense) {
		return database.getTense(verbId, tense);
	}

	public static void addNewTense(int verbId, int tenseId, String form,
			String pron) {
		database.insertTense(actualLang.getID(), verbId, form, pron, tenseId);
	}

	public static void ejercicio(DReference ref, int intento) {
		actualLang.getEstadisticas().nuevoIntento(intento);
		if (intento == 1) {
			ref.priority += -5;
		} else {
			ref.priority += intento;
		}
		for (Palabra p : ref.owners) {
			p.updatePriority(ref.priority);
			database.updatePriority(p);
		}
	}

}
