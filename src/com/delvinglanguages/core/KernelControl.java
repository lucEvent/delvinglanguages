package com.delvinglanguages.core;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;

import com.delvinglanguages.data.ControlDB;
import com.delvinglanguages.data.ControlDisco;

public class KernelControl {

	protected static ControlDB database;
	private static ControlDisco sdcard;
	private static ArrayList<IDDelved> languages;
	protected static IDDelved currentLanguage;

	//
	public static ArrayList<Word> integrateWords;
	public static IDDelved integrateLanguage;

	public KernelControl(Context context) {
		initializeAll(context);
	}

	private static void initializeAll(Context context) {
		if (database == null) {
			database = new ControlDB(context);
		}
		if (languages == null) {
			languages = database.readLanguages();
		}
		if (sdcard == null) {
			sdcard = new ControlDisco();
		}
	}

	public static IDDelved getCurrentLanguage(Context context) {
		if (currentLanguage == null) {
			initializeAll(context);
			currentLanguage = languages.get(sdcard.getLastLanguage());
		}
		return currentLanguage;
	}

	public static void setCurrentLanguage(int position) {
		currentLanguage = languages.get(position);
		sdcard.saveLastLaguage(position);
	}

	public static void setCurrentLanguage(IDDelved language) {
		setCurrentLanguage(languages.indexOf(language));
	}

	public static int getNumLanguages() {
		return languages.size();
	}

	public static ArrayList<IDDelved> getLanguages() {
		return languages;
	}

	public static ArrayList<Word> getWords() {
		loadwords();
		return currentLanguage.getPalabras();
	}

	public static ArrayList<DReference> getReferences() {
		return currentLanguage.getReferences();
	}

	public static Word getWord(int id) {
		return currentLanguage.getPalabra(id);
	}

	public static DReference getReference(String name) {
		return currentLanguage.getReference(name);
	}

	public static ArrayList<Word> getBin() {
		loadwords();
		return currentLanguage.getPapelera();
	}

	public static ArrayList<Nota> getStore() {
		loadwords();
		return currentLanguage.getAlmacen();
	}

	public static TreeSet<DReference> getSubdictionary(Character cap) {
		return currentLanguage.getDiccionario().get(cap);
	}

	private static void loadwords() {
		if (!currentLanguage.isLoaded()) {
			currentLanguage.setPalabras(database.readWords(currentLanguage
					.getID()));
			currentLanguage
					.setStore(database.readStore(currentLanguage.getID()));
		}
	}

	private static void loadwords(IDDelved language) {
		IDDelved temp = currentLanguage;
		currentLanguage = language;
		loadwords();
		currentLanguage = temp;
	}

	public static void loadLanguage() {
		loadwords();
	}

	public static void switchDictionary() {
		if (currentLanguage.isIdiomaNativo()) {
			currentLanguage = currentLanguage.getDelved();
		} else {
			currentLanguage = currentLanguage.getNativo();
		}
	}

	public static int addLanguage(String name, int settings) {
		IDDelved newLang = database.insertLanguage(name, settings);
		languages.add(newLang);
		return languages.size() - 1;
	}

	public static void addWord(String name, String translation,
			String pronunciation, int type) {
		name = Word.format(name);
		translation = Word.format(translation);
		if (currentLanguage.isIdiomaNativo()) {
			String temp = name;
			name = translation;
			translation = temp;
			pronunciation = "";
		}
		currentLanguage.addPalabra(database.insertWord(name, translation,
				currentLanguage.getID(), pronunciation, type));
	}

	public static void updateWord(Word word, String name, String translation,
			String pronunciation, int type) {
		currentLanguage.reindexar(word, name, translation, pronunciation, type);
		if (currentLanguage.isIdiomaNativo()) {
			word = word.cloneReverse();
		}
		database.updateWord(word);
	}

	public static void deleteLanguage() {
		database.removeLanguage(currentLanguage.getID());
		if (!currentLanguage.isIdiomaNativo()) {
			languages.remove(currentLanguage);
		} else {
			int langId = currentLanguage.getID();
			for (int i = 0; i < languages.size(); i++) {
				if (languages.get(i).getID() == langId) {
					languages.remove(i);
					break;
				}
			}
		}
		currentLanguage = null;
	}

	public static void saveStatistics() {
		database.updateStatistics(currentLanguage.getEstadisticas());
	}

	public static void clearStatistics() {
		currentLanguage.getEstadisticas().clear();
		saveStatistics();
	}

	public static void deleteWord(Word word) {
		database.throworrestoreWord(word);
		currentLanguage.tirarPalabra(word);
	}

	public static void restoreWord(int position) {
		database.throworrestoreWord(currentLanguage.getPapelera().get(position));
		currentLanguage.restorePalabra(position);
	}

	public static void clearBin() {
		for (Word word : currentLanguage.getPapelera()) {
			database.removeAllTenses(word.id);
		}
		currentLanguage.vaciarPapelera();
		database.clearTrash(currentLanguage.getID());
	}

	public static void addToStore(String note) {
		int type = currentLanguage.isIdiomaNativo() ? 1 : 0;

		currentLanguage.addPalabra(database.insertStoreWord(note,
				currentLanguage.getID(), type));
	}

	public static void addWordFromStore(Nota sword, String name,
			String translation, String pronuntiation, int type) {
		currentLanguage.removePalabraFromAlmacen(sword);
		addWord(name, translation, pronuntiation, type);
		database.removeStoredWord(sword.id);
	}

	public static void removeFromStore(Nota sword) {
		currentLanguage.removePalabraFromAlmacen(sword);
		database.removeStoredWord(sword.id);
	}

	public static void renameLanguage(String newname) {
		currentLanguage.setName(newname);
		database.updateLanguage(currentLanguage);
	}

	public static void setLanguageSettings(boolean status, int mask) {
		currentLanguage.setSettings(status, mask);
		database.updateLanguage(currentLanguage);
	}

	public static ArrayList<Word> integrateLanguage(int destinyLanguagePosition) {
		integrateWords = new ArrayList<Word>();
		integrateLanguage = languages.get(destinyLanguagePosition);
		IDDelved destino = integrateLanguage;
		loadwords(destino);
		while (!destino.datos.isDictionaryCreated()) {
		}

		// Copiando el Diccionario
		ArrayList<Word> fuentes = currentLanguage.getPalabras(); // Necesario
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
		ArrayList<Nota> notafuente = currentLanguage.getAlmacen();
		for (int i = 0; i < notafuente.size(); ++i) {
			destino.addPalabra(notafuente.get(i));
		}
		database.integrateStore(currentLanguage.getID(), destino.getID());
		return integrateWords;
	}

	public static ArrayList<DReference> getVerbs() {
		return currentLanguage.getVerbs();
	}

	public static void exercise(DReference ref, int intento) {
		currentLanguage.getEstadisticas().nuevoIntento(intento);
		if (intento == 1) {
			ref.priority += -1;
		}
		for (Link link : ref.links) {
			link.owner.updatePriority(ref.priority);
			database.updatePriority(link.owner);
		}
	}

}
