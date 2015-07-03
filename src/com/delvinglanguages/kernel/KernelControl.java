package com.delvinglanguages.kernel;

import android.content.Context;

import com.delvinglanguages.data.ControlDB;
import com.delvinglanguages.data.ControlDisco;
import com.delvinglanguages.kernel.set.DrawerWords;
import com.delvinglanguages.kernel.set.Languages;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.kernel.svenska.SwedishTranslation;
import com.delvinglanguages.net.internal.BackgroundTaskMessenger;
import com.delvinglanguages.net.internal.ProgressHandler;
import com.delvinglanguages.settings.Settings;

public class KernelControl {

	private static Context context;
	protected static ControlDB database;
	private static ControlDisco sdcard;
	private static Languages languages;
	protected static Language currentLanguage;

	//
	public static Words integrateWords;
	public static Language integrateLanguage;

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

	public static Language getCurrentLanguage() {
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

	public static void setCurrentLanguage(Language language) {
		setCurrentLanguage(languages.indexOf(language));
	}

	public static int getNumLanguages() {
		return languages.size();
	}

	public static Languages getLanguages() {
		return languages;
	}

	public static void loadLanguage(Language language) {
		if (!language.isLoaded()) {
			database.readLanguage(language, new ProgressHandler(null));
		}
	}

	public static void loadLanguage(final Language language, final ProgressHandler progress, final BackgroundTaskMessenger comm) {
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

	public static int addLanguage(int code, String name, int settings) {
		Language newlanguage = database.insertLanguage(code, name, settings);
		languages.add(newlanguage);
		return languages.size() - 1;
	}

	public static void addWord(String name, Translations translations, String pronunciation, int priority) {
		name = Word.format(name);
		if (currentLanguage.isNativeLanguage()) {
			Word temp = new Word(-1, name, translations, pronunciation, 0).getInverse(false);
			name = temp.nombre;
			translations = temp.traducciones;
			pronunciation = "";
		}
		Word W = database.insertWord(name, translations, currentLanguage.getID(), pronunciation, priority);
		if (currentLanguage.CODE == Language.SV) {
			for (int i = 0; i < W.traducciones.size(); i++) {
				SwedishTranslation SVT = (SwedishTranslation) translations.get(i);
				database.insertSwedishForm(W.traducciones.get(i), SVT.forms);
			}
		}
		currentLanguage.addWord(W);
	}

	public static void updateWord(Word word, String name, Translations translation, String pronunciation) {
		currentLanguage.updateWord(word, name, translation, pronunciation);
		if (currentLanguage.isNativeLanguage()) {
			debug("Word:" + word.nombre + ", trans:" + word.getTranslationString());
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
		currentLanguage.deleteAllRemovedWords();
		database.deleteAllRemovedWords(currentLanguage.getID());
	}

	public static void addToStore(String note) {
		int type = currentLanguage.isNativeLanguage() ? 1 : 0;
		currentLanguage.addWord(database.insertStoreWord(note, currentLanguage.getID(), type));
	}

	public static void addWord(DrawerWord sword, String name, Translations translations, String pronuntiation) {
		addWord(name, translations, pronuntiation, Word.INITIAL_PRIORITY);
		removeFromStore(sword);
	}

	public static void removeFromStore(DrawerWord sword) {
		currentLanguage.deleteDrawerWord(sword);
		database.deleteStoredWord(sword.id);
	}

	public static void updateLanguage(int code, String newname) {
		currentLanguage.CODE = code;
		currentLanguage.setName(newname);
		database.updateLanguage(currentLanguage);
	}

	public static void updateLanguageSettings(boolean status, int mask) {
		currentLanguage.setSettings(status, mask);
		database.updateLanguage(currentLanguage);
	}

	public static Words integrateLanguage(int destinyLanguagePosition) {
		integrateWords = new Words();
		integrateLanguage = languages.get(destinyLanguagePosition);
		Language destino = integrateLanguage;
		loadLanguage(destino);
		while (!destino.isDictionaryCreated()) {
		}

		// Copiando el Diccionario
		Words fuentes = currentLanguage.getWords();
		for (Word pinsert : fuentes) {
			Word porig = destino.getPalabra(pinsert.getName());
			if (porig == null) {
				destino.addWord(pinsert);
				database.updateWordLanguage(pinsert, destino.getID());
			} else {
				integrateWords.add(porig);
				integrateWords.add(pinsert);
			}
		}

		// Copiando la warehouse
		DrawerWords notafuente = currentLanguage.getDrawerWords();
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
		for (Word word : ref.words) {
			word.updatePriority(ref.priority);
			database.updateWordPriority(word);
		}
	}

	public static SwedishTranslation getSwedishForm(Translation t) {
		return database.readSwedishForm(t);
	}

	private static void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##KernelControl##", text);
	}

}
