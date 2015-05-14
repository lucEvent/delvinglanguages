package com.delvinglanguages.kernel;

import android.content.Context;
import android.util.Log;

import com.delvinglanguages.data.ControlDB;
import com.delvinglanguages.data.ControlDisco;
import com.delvinglanguages.kernel.set.Languages;
import com.delvinglanguages.kernel.set.Notas;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.net.internal.BackgroundTaskMessenger;
import com.delvinglanguages.net.internal.ProgressHandler;

public class KernelControl {

	private static final String DEBUG = "##KernelControl##";
	private static Context context;
	protected static ControlDB database;
	private static ControlDisco sdcard;
	private static Languages languages;
	protected static IDDelved currentLanguage;

	//
	public static Words integrateWords;
	public static IDDelved integrateLanguage;

	public KernelControl(Context context) {
		KernelControl.context = context;
		initializeAll();
	}

	private static void initializeAll() {
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

	public static IDDelved getCurrentLanguage() {
		if (currentLanguage == null) {
			initializeAll();
			currentLanguage = languages.get(sdcard.getLastLanguage());
		}
		return currentLanguage;
	}

	public static void setCurrentLanguage(int position) {
		currentLanguage = languages.get(position);
		currentLanguage.initCollator();
		sdcard.saveLastLaguage(position);
	}

	public static void setCurrentLanguage(IDDelved language) {
		setCurrentLanguage(languages.indexOf(language));
	}

	public static int getNumLanguages() {
		return languages.size();
	}

	public static Languages getLanguages() {
		return languages;
	}

	public static void loadLanguage(IDDelved language) {
		if (!language.isLoaded()) {
			database.readLanguage(language, new ProgressHandler(null));
		}
	}

	public static void loadLanguage(final IDDelved language, final ProgressHandler progress, final BackgroundTaskMessenger comm) {
		if (!language.isLoaded()) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					database.readLanguage(language, progress);
					comm.onTaskDone(-1, BackgroundTaskMessenger.TASK_DONE, null);
				}
			}).start();
		}
	}

	public static void switchDictionary() {
		if (currentLanguage.isNativeLanguage()) {
			currentLanguage = currentLanguage.getDelved();
		} else {
			currentLanguage = currentLanguage.getNative();
		}
	}

	public static int addLanguage(String name, int settings) {
		IDDelved newLang = database.insertLanguage(name, settings);
		languages.add(newLang);
		return languages.size() - 1;
	}

	public static void addWord(String name, Translations translations, String pronunciation) {
		name = Word.format(name);
		if (currentLanguage.isNativeLanguage()) {
			Word temp = new Word(-1, name, translations, pronunciation, 0).getInverse(false);
			name = temp.nombre;
			translations = temp.traducciones;
			pronunciation = "";
		}
		currentLanguage.addWord(database.insertWord(name, translations, currentLanguage.getID(), pronunciation));
	}

	public static void updateWord(Word word, String name, Translations translation, String pronunciation) {
		currentLanguage.updateWord(word, name, translation, pronunciation);
		if (currentLanguage.isNativeLanguage()) {
			Log.d(DEBUG, "Word:" + word.nombre + ", trans:" + word.getTranslationString());
			word = word.getInverse(true);
		}
		database.updateWord(word);
	}

	public static void deleteWordTemporarily(Word word) {
		database.deleteWordTemporarily(currentLanguage.getID(), word.id);
		currentLanguage.removeWord(word);
	}

	public static void restoreWord(int position) {
		database.restoreWord(currentLanguage.getRemovedWords().get(position).id);
		currentLanguage.restoreWord(position);
	}

	public static void deleteLanguage() {
		database.deleteLanguage(currentLanguage);
		if (!currentLanguage.isNativeLanguage()) {
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
		database.updateStatistics(currentLanguage.getStatistics());
	}

	public static void clearStatistics() {
		currentLanguage.getStatistics().clear();
		saveStatistics();
	}

	public static void deleteAllRemovedWords() {
		for (Word word : currentLanguage.getRemovedWords()) {
			database.deleteVerbTenses(word.id);
		}
		currentLanguage.deleteAllRemovedWords();
		database.deleteAllRemovedWords(currentLanguage.getID());
	}

	public static void addToStore(String note) {
		int type = currentLanguage.isNativeLanguage() ? 1 : 0;

		currentLanguage.addWord(database.insertStoreWord(note, currentLanguage.getID(), type));
	}

	public static void addWordFromStore(Nota sword, String name, Translations translations, String pronuntiation) {
		addWord(name, translations, pronuntiation);
		removeFromStore(sword);
	}

	public static void removeFromStore(Nota sword) {
		currentLanguage.deleteDrawerWord(sword);
		database.deleteStoredWord(sword.id);
	}

	public static void renameLanguage(String newname) {
		currentLanguage.setName(newname);
		database.updateLanguage(currentLanguage);
	}

	public static void setLanguageSettings(boolean status, int mask) {
		currentLanguage.setSettings(status, mask);
		database.updateLanguage(currentLanguage);
	}

	public static Words integrateLanguage(int destinyLanguagePosition) {
		integrateWords = new Words();
		integrateLanguage = languages.get(destinyLanguagePosition);
		IDDelved destino = integrateLanguage;
		loadLanguage(destino);
		while (!destino.isDictionaryCreated()) {
		}

		// Copiando el Diccionario
		Words fuentes = currentLanguage.getWords(); // Necesario
		for (int i = 0; i < fuentes.size(); ++i) {
			Word pinsert = fuentes.get(i);
			Word porig = destino.getPalabra(pinsert.getName());// Necesario
			if (porig == null) {
				destino.addWord(pinsert);
				database.updateWordLanguage(pinsert, destino.getID());
			} else {
				integrateWords.add(porig);
				integrateWords.add(pinsert);
			}
		}

		// Copiando la warehouse
		Notas notafuente = currentLanguage.getDrawerWords();
		for (int i = 0; i < notafuente.size(); ++i) {
			destino.addWord(notafuente.get(i));
		}
		database.updateDrawerWordsLanguage(currentLanguage.getID(), destino.getID());
		return integrateWords;
	}

	public static void exercise(DReference ref, int intento) {
		currentLanguage.getStatistics().nuevoIntento(intento);
		if (intento == 1) {
			ref.priority += -1;
		}
		for (Link link : ref.links) {
			link.owner.updatePriority(ref.priority);
			database.updateWordPriority(link.owner);
		}
	}

}
