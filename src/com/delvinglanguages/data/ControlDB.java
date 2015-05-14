package com.delvinglanguages.data;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

import com.delvinglanguages.data.DataBase.DBDrawerWord;
import com.delvinglanguages.data.DataBase.DBLanguage;
import com.delvinglanguages.data.DataBase.DBRemovedWord;
import com.delvinglanguages.data.DataBase.DBStatistics;
import com.delvinglanguages.data.DataBase.DBTense;
import com.delvinglanguages.data.DataBase.DBTest;
import com.delvinglanguages.data.DataBase.DBTheme;
import com.delvinglanguages.data.DataBase.DBThemePair;
import com.delvinglanguages.data.DataBase.DBTranslation;
import com.delvinglanguages.data.DataBase.DBWord;
import com.delvinglanguages.kernel.Datos;
import com.delvinglanguages.kernel.Estadisticas;
import com.delvinglanguages.kernel.IDDelved;
import com.delvinglanguages.kernel.Nota;
import com.delvinglanguages.kernel.Tense;
import com.delvinglanguages.kernel.Translation;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.Languages;
import com.delvinglanguages.kernel.set.Notas;
import com.delvinglanguages.kernel.set.Tests;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.kernel.set.Themes;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemePair;
import com.delvinglanguages.net.internal.ProgressHandler;
import com.delvinglanguages.settings.Settings;

public class ControlDB {

	private static final String DEBUG = "##ControlDB##";
	private DataBase gateway;

	public ControlDB(Context context) {
		gateway = new DataBase(context);
	}

	/** ********* READS ************* **/
	public Languages readLanguages() {
		SQLiteDatabase database = gateway.getReadableDatabase();
		Languages result = new Languages();
		Cursor cursorID = database.query(DBLanguage.db, DBLanguage.cols, null, null, null, null, null);

		Cursor cursorES = database.query(DBStatistics.db, DBStatistics.cols, null, null, null, null, null);

		cursorID.moveToFirst();
		cursorES.moveToFirst();
		while (!cursorID.isAfterLast()) {
			IDDelved language = cursorToLanguage(cursorID);
			language.setStatistics(cursorToStatitics(cursorES));
			result.add(language);
			cursorID.moveToNext();
			cursorES.moveToNext();
		}
		cursorID.close();
		cursorES.close();
		database.close();
		Log.d(DEBUG, result.size() + " Idiomas leidos");
		return result;
	}

	public void readLanguage(IDDelved language, ProgressHandler progress) {
		language.setWords(readWords(language.getID(), progress));
		language.setDrawerWords(readDrawerWords(language.getID()));
		language.setRemovedWords(readRemovedWords(language.getID()));
	}

	private Words readWords(int langID, ProgressHandler progress) {
		TreeSet<Integer> remIds = readRemovedWordsIds(langID);
		SQLiteDatabase database = gateway.getReadableDatabase();
		Words result = new Words();
		Cursor cursor = database.query(DBWord.db, DBWord.cols, DBWord.lang_id + "=" + langID, null, null, null, DBWord.name + " ASC");

		progress.start(cursor.getCount());

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			if (!remIds.contains(cursor.getInt(0))) {
				Word word = cursorToWord(cursor);
				word.setTranslations(readTranslations(database, word.id));
				result.add(word);
			}
			progress.stepForward();
			cursor.moveToNext();
		}
		cursor.close();

		progress.finish();

		Log.d(DEBUG, "Palabras leidas: " + result.size());
		database.close();
		return result;
	}

	private Notas readDrawerWords(int langID) {
		SQLiteDatabase database = gateway.getReadableDatabase();
		Notas result = new Notas();
		Cursor cursor = database.query(DBDrawerWord.db, DBDrawerWord.cols, DBDrawerWord.lang_id + "=" + langID, null, null, null, DBDrawerWord.name
				+ " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			result.add(cursorToDrawerWord(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		Log.d(DEBUG, "Almacen leidas: " + result.size());
		database.close();
		return result;
	}

	private TreeSet<Integer> readRemovedWordsIds(int lang_id) {
		SQLiteDatabase database = gateway.getReadableDatabase();
		Cursor cursor = database.query(DBRemovedWord.db, DBRemovedWord.cols, DBRemovedWord.lang_id + " = " + lang_id, null, null, null, null);

		TreeSet<Integer> result = new TreeSet<Integer>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			result.add(cursor.getInt(2));
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return result;
	}

	private Words readRemovedWords(int langID) {
		TreeSet<Integer> wordIds = readRemovedWordsIds(langID);

		SQLiteDatabase database = gateway.getReadableDatabase();

		Words result = new Words();
		for (Integer word_id : wordIds) {
			Cursor cursor = database.query(DBWord.db, DBWord.cols, DBWord.id + " = " + word_id, null, null, null, null);
			cursor.moveToFirst();
			Word word = cursorToWord(cursor);
			cursor.close();
			word.setTranslations(readTranslations(database, word.id));
			result.add(word);
		}

		Log.d(DEBUG, "Palabras eliminadas leidas: " + result.size());
		database.close();
		return result;
	}

	public Translations readTranslations(SQLiteDatabase database, int word_id) {
		Cursor cursor = database.query(DBTranslation.db, DBTranslation.cols, DBTranslation.word_id + "=" + word_id, null, null, null, null);
		Translations translations = new Translations();

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			translations.add(cursorToTranslation(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return translations;
	}

	public Tests readTests(int langID) {
		SQLiteDatabase database = gateway.getReadableDatabase();
		Tests result = new Tests();
		Cursor cursor = database.query(DBTest.db, DBTest.cols, DBTest.lang_id + "=" + langID, null, null, null, DBTest.name + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Test test = cursorToTest(cursor);
			result.add(test);
			cursor.moveToNext();
		}
		cursor.close();
		Log.d(DEBUG, "Test leidos: " + result.size());
		database.close();
		return result;
	}

	public Tense readTense(int verbId, int tense, String verbName) {
		SQLiteDatabase database = gateway.getReadableDatabase();
		Cursor cursor = database.query(DBTense.db, DBTense.cols, DBTense.verb_id + " = " + verbId + " and " + DBTense.tense + " = " + tense, null,
				null, null, null);
		Tense result = null;
		cursor.moveToFirst();
		Log.d(DEBUG, "Encontrados:" + cursor.getCount() + " buscando tense:" + tense + " en verbid:" + verbId);
		if (!cursor.isAfterLast()) {
			result = new Tense(cursor.getInt(0), tense, verbName, cursor.getString(4), cursor.getString(5));
		}
		cursor.close();
		database.close();
		return result;
	}

	public Themes readThemes(int langID) {
		SQLiteDatabase database = gateway.getReadableDatabase();
		Cursor cursor = database.query(DBTheme.db, DBTheme.cols, DBTheme.lang_id + "=" + langID, null, null, null, DBTheme.name + " ASC");

		Themes result = new Themes();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Theme theme = cursorToTheme(cursor);
			theme.setPairs(readThemePairs(theme.id));
			result.add(theme);
			cursor.moveToNext();
		}
		cursor.close();
		Log.d(DEBUG, "Themes leidos: " + result.size());
		database.close();
		return result;
	}

	public ThemePairs readThemePairs(int themeID) {
		SQLiteDatabase database = gateway.getReadableDatabase();
		Cursor cursor = database.query(DBThemePair.db, DBThemePair.cols, DBThemePair.theme_id + "=" + themeID, null, null, null, null);

		ThemePairs result = new ThemePairs();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			result.add(cursorToThemePair(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return result;
	}

	public ArrayList<Pair<Integer, Tense>> readTenses(int langId) {
		SQLiteDatabase database = gateway.getReadableDatabase();
		Cursor cursor = database.query(DBTense.db, DBTense.cols, DBTense.lang_id + " = " + langId, null, null, null, null);

		ArrayList<Pair<Integer, Tense>> result = new ArrayList<Pair<Integer, Tense>>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			result.add(new Pair<Integer, Tense>(cursor.getInt(2), new Tense(-1, cursor.getInt(3), "", cursor.getString(4), cursor.getString(5))));
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return result;
	}

	/** ********* UPDATES ************* **/
	public void updateLanguage(IDDelved lang) {
		ContentValues values = new ContentValues();
		values.put(DBLanguage.name, lang.getName());
		values.put(DBLanguage.settings, lang.getSettings());
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.update(DBLanguage.db, values, DBLanguage.id + " = " + lang.getID(), null);
		database.close();
	}

	public void updateWord(Word word) {
		ContentValues values = new ContentValues();
		values.put(DBWord.name, word.getName());
		values.put(DBWord.pronunciation, word.getPronunciation());
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.update(DBWord.db, values, DBWord.id + " = " + word.id, null);
		database.delete(DBTranslation.db, DBTranslation.word_id + " = " + word.id, null);
		database.close();

		for (Translation translation : word.getTranslations()) {
			insertTranslation(translation, word.id);
		}
	}

	public void updateWordLanguage(Word word, int newlang_id) {
		ContentValues values = new ContentValues();
		values.put(DBWord.lang_id, newlang_id);
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.update(DBWord.db, values, DBWord.id + " = " + word.id, null);
		database.close();
	}

	public void updateWordPriority(Word word) {
		ContentValues values = new ContentValues();
		values.put(DBWord.priority, word.getPriority());
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.update(DBWord.db, values, DBWord.id + " = " + word.id, null);
		database.close();
	}

	public void updateDrawerWordsLanguage(int oldlang_id, int newlang_id) {
		ContentValues values = new ContentValues();
		values.put(DBDrawerWord.lang_id, newlang_id);
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.update(DBDrawerWord.db, values, DBDrawerWord.lang_id + " = " + oldlang_id, null);
		database.close();
	}

	public void updateTest(Test t) {
		SQLiteDatabase database = gateway.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBTest.content, t.encapsulate());
		database.update(DBTest.db, values, DBTest.id + " = " + t.id, null);
		database.close();
	}

	public void updateTense(Tense tense) {
		ContentValues values = new ContentValues();
		values.put(DBTense.forms, tense.getFormsString());
		values.put(DBTense.pronunciations, tense.getPronuntiationsString());
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.update(DBTense.db, values, DBTense.id + " = " + tense.id, null);
		database.close();
	}

	public void updateStatistics(Estadisticas e) {
		ContentValues values = new ContentValues();
		values.put(DBStatistics.tries, e.intentos);
		values.put(DBStatistics.hits1, e.aciertos1);
		values.put(DBStatistics.hits2, e.aciertos2);
		values.put(DBStatistics.hits3, e.aciertos3);
		values.put(DBStatistics.misses, e.fallos);
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.update(DBStatistics.db, values, DBStatistics.id + " = " + e.id, null);
		database.close();
	}

	public void updateTheme(Theme theme) {
		ContentValues values = new ContentValues();
		values.put(DBTheme.name, theme.getName());
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.update(DBTheme.db, values, DBTheme.id + " = " + theme.id, null);
		database.delete(DBThemePair.db, DBThemePair.theme_id + " = " + theme.id, null);
		database.close();

		for (ThemePair pair : theme.getPairs()) {
			insertThemePair(theme.id, pair);
		}
	}

	/** ************************ INSERTS ************************ **/
	public IDDelved insertLanguage(String name, int isettings) {
		// Inserting stadistics
		ContentValues values = new ContentValues();
		values.put(DBStatistics.tries, 0);
		values.put(DBStatistics.hits1, 0);
		values.put(DBStatistics.hits2, 0);
		values.put(DBStatistics.hits3, 0);
		values.put(DBStatistics.misses, 0);
		SQLiteDatabase database = gateway.getWritableDatabase();
		long stats_id = database.insert(DBStatistics.db, null, values);

		// Inserting language
		String settings = Integer.toString(isettings);

		values = new ContentValues();
		values.put(DBLanguage.name, name);
		values.put(DBLanguage.statistics, stats_id);
		values.put(DBLanguage.settings, settings);
		int lang_id = (int) database.insert(DBLanguage.db, null, values);
		IDDelved language = new IDDelved(new Datos(lang_id, name, Settings.IdiomaNativo, settings));
		language.setStatistics(new Estadisticas((int) stats_id));
		database.close();
		return language;
	}

	public Word insertWord(String name, Translations translations, int langID, String pronunciation) {
		ContentValues values = new ContentValues();
		values.put(DBWord.name, name);
		values.put(DBWord.lang_id, langID);
		values.put(DBWord.pronunciation, pronunciation);
		values.put(DBWord.priority, Word.INITIAL_PRIORITY);
		SQLiteDatabase database = gateway.getWritableDatabase();
		int word_id = (int) database.insert(DBWord.db, null, values);
		database.close();

		for (Translation translation : translations) {
			insertTranslation(translation, word_id);
		}
		return new Word(word_id, name, translations, pronunciation, Word.INITIAL_PRIORITY);
	}

	public void insertRemovedWord(int langID, int word_id) {
		ContentValues values = new ContentValues();
		values.put(DBRemovedWord.lang_id, langID);
		values.put(DBRemovedWord.word_id, word_id);
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.insert(DBRemovedWord.db, null, values);
		database.close();
	}

	public void insertTranslation(Translation translation, int word_id) {
		ContentValues values = new ContentValues();
		values.put(DBTranslation.word_id, word_id);
		values.put(DBTranslation.type, translation.type);
		values.put(DBTranslation.translation, translation.name);
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.insert(DBTranslation.db, null, values);
		database.close();
	}

	public Nota insertStoreWord(String note, int langID, int type) {
		ContentValues values = new ContentValues();
		values.put(DBDrawerWord.name, note);
		values.put(DBDrawerWord.lang_id, langID);
		values.put(DBDrawerWord.type, type);
		SQLiteDatabase database = gateway.getWritableDatabase();
		long note_id = database.insert(DBDrawerWord.db, null, values);
		database.close();
		return new Nota((int) note_id, note, type);
	}

	public int insertTest(String name, int langID, String content) {
		ContentValues values = new ContentValues();
		values.put(DBTest.name, name);
		values.put(DBTest.lang_id, langID);
		values.put(DBTest.content, content);
		SQLiteDatabase database = gateway.getWritableDatabase();
		long tid = database.insert(DBTest.db, null, values);
		database.close();
		return (int) tid;
	}

	public void insertTense(int langId, int verbId, String verbName, int tense, String forms, String pronunciations) {
		ContentValues values = new ContentValues();
		values.put(DBTense.lang_id, langId);
		values.put(DBTense.verb_id, verbId);
		values.put(DBTense.tense, tense);
		values.put(DBTense.forms, forms);
		values.put(DBTense.pronunciations, pronunciations);
		SQLiteDatabase database = gateway.getWritableDatabase();
		int cid = (int) database.insert(DBTense.db, null, values);
		database.close();
		Log.d(DEBUG, "Result:" + cid + " insertando tense:" + tense + " en verbid:" + verbId);
	}

	public Theme insertTheme(int langID, String thname, ThemePairs pairs) {
		ContentValues values = new ContentValues();
		values.put(DBTheme.lang_id, langID);
		values.put(DBTheme.name, thname);
		SQLiteDatabase database = gateway.getWritableDatabase();
		int id = (int) database.insert(DBTheme.db, null, values);
		database.close();

		for (ThemePair pair : pairs) {
			insertThemePair(id, pair);
		}
		return new Theme(id, thname, pairs);
	}

	public void insertThemePair(int theme_id, ThemePair pair) {
		ContentValues values = new ContentValues();
		values.put(DBThemePair.theme_id, theme_id);
		values.put(DBThemePair.in_delv, pair.inDelved);
		values.put(DBThemePair.in_nativ, pair.inNative);
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.insert(DBThemePair.db, null, values);
		database.close();
	}

	/** ********************* DELETES ********************* **/
	public void deleteLanguage(IDDelved lang) {
		SQLiteDatabase database = gateway.getWritableDatabase();
		// Removing translations
		for (Word word : lang.getWords()) {
			database.delete(DBTranslation.db, DBTranslation.word_id + " = " + word.id, null);
		}
		// Removing words
		database.delete(DBWord.db, DBWord.lang_id + " = " + lang.getID(), null);
		// Removing trash
		database.delete(DBRemovedWord.db, DBRemovedWord.lang_id + " = " + lang.getID(), null);
		// Removing store
		database.delete(DBDrawerWord.db, DBDrawerWord.lang_id + " = " + lang.getID(), null);
		// Removing tests
		database.delete(DBTest.db, DBTest.lang_id + " = " + lang.getID(), null);
		// Removing tiempos verbales
		database.delete(DBTense.db, DBTense.lang_id + " = " + lang.getID(), null);
		// Removing themes
		database.close();
		Themes themes = readThemes(lang.getID());
		database = gateway.getWritableDatabase();
		for (Theme theme : themes) {
			// Removing theme pairs
			database.delete(DBThemePair.db, DBThemePair.theme_id + " = " + theme.id, null);
		}
		database.delete(DBTheme.db, DBTheme.lang_id + " = " + lang.getID(), null);
		// Removing language
		database.delete(DBLanguage.db, DBLanguage.id + " = " + lang.getID(), null);
		// Removing statistics
		database.delete(DBStatistics.db, DBStatistics.id + " = " + lang.getID(), null);
		database.close();
	}

	public void deleteWordTemporarily(int lang_id, int word_id) {
		insertRemovedWord(lang_id, word_id);
	}

	public void deleteAllRemovedWords(int lang_id) {
		TreeSet<Integer> ids = readRemovedWordsIds(lang_id);
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.delete(DBRemovedWord.db, DBRemovedWord.lang_id + " = " + lang_id, null);
		for (Integer word_id : ids) {
			database.delete(DBTranslation.db, DBTranslation.word_id + " = " + word_id, null);
			database.delete(DBTense.db, DBTense.verb_id + " = " + word_id, null);
			database.delete(DBWord.db, DBWord.id + " = " + word_id, null);
		}
		database.close();
	}

	public void deleteStoredWord(int id) {
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.delete(DBDrawerWord.db, DBDrawerWord.id + " = " + id, null);
		database.close();
	}

	public void deleteTest(int id) {
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.delete(DBTest.db, DBTest.id + " = " + id, null);
		database.close();
	}

	public void deleteTheme(int theme_id) {
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.delete(DBThemePair.db, DBThemePair.theme_id + " = " + theme_id, null);
		database.delete(DBTheme.db, DBTheme.id + " = " + theme_id, null);
		database.close();
	}

	public void deleteVerbTenses(int verb_id) {
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.delete(DBTense.db, DBTense.verb_id + " = " + verb_id, null);
		database.close();
	}

	/** ********************* Restore ********************* **/
	public void restoreWord(int word_id) {
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.delete(DBRemovedWord.db, DBRemovedWord.word_id + " = " + word_id, null);
		database.close();
	}

	/** ********************* Funciones privadas ********************* **/
	private IDDelved cursorToLanguage(Cursor c) {
		return new IDDelved(new Datos(c.getInt(0), c.getString(1), Settings.IdiomaNativo, c.getString(3)));
	}

	private Word cursorToWord(Cursor c) {
		return new Word(c.getInt(0), c.getString(1), null, c.getString(3), c.getInt(4));
	}

	private Translation cursorToTranslation(Cursor c) {
		return new Translation(c.getString(3), c.getInt(2));
	}

	private Estadisticas cursorToStatitics(Cursor c) {
		return new Estadisticas(c.getInt(0), c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));
	}

	private Nota cursorToDrawerWord(Cursor c) {
		return new Nota(c.getInt(0), c.getString(1), c.getInt(3));
	}

	private Test cursorToTest(Cursor c) {
		return new Test(c.getInt(0), c.getString(1), c.getString(3));
	}

	private Theme cursorToTheme(Cursor c) {
		return new Theme(c.getInt(0), c.getString(2));
	}

	private ThemePair cursorToThemePair(Cursor c) {
		return new ThemePair(c.getString(2), c.getString(3));
	}

}
