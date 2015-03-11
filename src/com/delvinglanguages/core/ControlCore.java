package com.delvinglanguages.core;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.util.Log;

import com.delvinglanguages.data.ControlDB;
import com.delvinglanguages.data.ControlDisco;

public class ControlCore {

	private static final String DEBUG = "##ControlCore##";

	protected static ControlDB database;
	private static ControlDisco disco;

	private static ArrayList<IDDelved> idiomas;
	protected static IDDelved actualLang;

	//
	public static ArrayList<Word> integrateWords;
	public static IDDelved integrateLanguage;

	public ControlCore(Context context) {
		initializeAll(context);
	}

	private static void initializeAll(Context context) {
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
			initializeAll(context);
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

	public static ArrayList<Word> getPalabras() {
		loadwords();
		return actualLang.getPalabras();
	}

	public static ArrayList<DReference> getReferences() {
		return actualLang.getReferences();
	}

	public static Word getPalabra(int id) {
		return actualLang.getPalabra(id);
	}

	public static DReference getReference(String name) {
		return actualLang.getReference(name);
	}

	public static ArrayList<Word> getPapelera() {
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
	}

	public static void switchDictionary() {
		if (actualLang.isIdiomaNativo()) {
			actualLang = actualLang.getDelved();
		} else {
			actualLang = actualLang.getNativo();
		}
	}

	public static int addIdioma(String name, int settings) {
		IDDelved newLang = database.insertLanguage(name, settings);
		idiomas.add(newLang);
		return idiomas.size() - 1;
	}

	public static void addPalabra(String nom, String trad, String spell,
			int type) {
		nom = Word.format(nom);
		trad = Word.format(trad);
		if (actualLang.isIdiomaNativo()) {
			String temp = nom;
			nom = trad;
			trad = temp;
			spell = "";
		}
		actualLang.addPalabra(database.insertWord(nom, trad,
				actualLang.getID(), spell, type));
	}

	public static void updatePalabra(Word pal, String name, String trad,
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

	public static void throwPalabra(Word thrown) {
		database.throworrestoreWord(thrown);
		actualLang.tirarPalabra(thrown);
	}

	public static void restorePalabra(int binposition) {
		database.throworrestoreWord(actualLang.getPapelera().get(binposition));
		actualLang.restorePalabra(binposition);
	}

	public static void clearPapelera() {
		ArrayList<Word> bin = actualLang.getPapelera();
		for (int i = 0; i < bin.size(); i++) {
			Word p = bin.get(i);
			if (p.isVerb()) {
				database.removeAllTenses(p.id);
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

	public static void addWordFromStore(Nota nota, String nombre, String trad,
			String spell, int type) {
		actualLang.removePalabraFromAlmacen(nota);
		addPalabra(nombre, trad, spell, type);
		database.removeStoredWord(nota.id);
	}

	public static void removeFromStore(Nota nota) {
		actualLang.removePalabraFromAlmacen(nota);
		database.removeStoredWord(nota.id);
	}

	public static void renameLanguage(String newname) {
		actualLang.setName(newname);
		database.updateLanguage(actualLang);
	}

	public static void setLangSettings(boolean status, int mask) {
		actualLang.setSettings(status, mask);
		database.updateLanguage(actualLang);
	}

	public static ArrayList<Word> integrateLanguage(int langdestinoposition) {
		integrateWords = new ArrayList<Word>();
		integrateLanguage = idiomas.get(langdestinoposition);
		IDDelved destino = integrateLanguage;
		loadwords(destino);
		while (!destino.datos.isDictionaryCreated()) {
		}

		// Copiando el Diccionario
		ArrayList<Word> fuentes = actualLang.getPalabras(); // Necesario
		for (int i = 0; i < fuentes.size(); ++i) {
			Word pinsert = fuentes.get(i);
			Word porig = destino.getPalabra(pinsert.getName());// Necesario
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

	public static void ejercicio(DReference ref, int intento) {
		actualLang.getEstadisticas().nuevoIntento(intento);
		if (intento == 1) {
			ref.priority += -1;
		}
		for (Link link : ref.links) {
			link.owner.updatePriority(ref.priority);
			database.updatePriority(link.owner);
		}
	}

}
