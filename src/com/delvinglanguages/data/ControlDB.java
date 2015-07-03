package com.delvinglanguages.data;

import java.util.TreeSet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.delvinglanguages.data.DataBase.DBDrawerWord;
import com.delvinglanguages.data.DataBase.DBLanguage;
import com.delvinglanguages.data.DataBase.DBRemovedWord;
import com.delvinglanguages.data.DataBase.DBStatistics;
import com.delvinglanguages.data.DataBase.DBSwedishForm;
import com.delvinglanguages.data.DataBase.DBTest;
import com.delvinglanguages.data.DataBase.DBTheme;
import com.delvinglanguages.data.DataBase.DBThemePair;
import com.delvinglanguages.data.DataBase.DBTranslation;
import com.delvinglanguages.data.DataBase.DBWord;
import com.delvinglanguages.kernel.Datos;
import com.delvinglanguages.kernel.DrawerWord;
import com.delvinglanguages.kernel.Estadisticas;
import com.delvinglanguages.kernel.Language;
import com.delvinglanguages.kernel.Translation;
import com.delvinglanguages.kernel.Word;
import com.delvinglanguages.kernel.set.DrawerWords;
import com.delvinglanguages.kernel.set.Languages;
import com.delvinglanguages.kernel.set.Tests;
import com.delvinglanguages.kernel.set.ThemePairs;
import com.delvinglanguages.kernel.set.Themes;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.kernel.svenska.SwedishTranslation;
import com.delvinglanguages.kernel.test.Test;
import com.delvinglanguages.kernel.theme.Theme;
import com.delvinglanguages.kernel.theme.ThemePair;
import com.delvinglanguages.net.internal.ProgressHandler;
import com.delvinglanguages.settings.Settings;

public class ControlDB {

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
			Language language = cursorToLanguage(cursorID);
			language.setStatistics(cursorToStatitics(cursorES));
			result.add(language);
			cursorID.moveToNext();
			cursorES.moveToNext();
		}
		cursorID.close();
		cursorES.close();
		database.close();
		debug(result.size() + " Idiomas leidos");
		return result;
	}

	public void readLanguage(Language language, ProgressHandler progress) {
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

		debug("Palabras leidas: " + result.size());
		database.close();
		return result;
	}

	private DrawerWords readDrawerWords(int langID) {
		SQLiteDatabase database = gateway.getReadableDatabase();
		DrawerWords result = new DrawerWords();
		Cursor cursor = database.query(DBDrawerWord.db, DBDrawerWord.cols, DBDrawerWord.lang_id + "=" + langID, null, null, null, DBDrawerWord.name
				+ " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			result.add(cursorToDrawerWord(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		debug("Almacen leidas: " + result.size());
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

		debug("Palabras eliminadas leidas: " + result.size());
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
		debug("Test leidos: " + result.size());
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
		debug("Themes leidos: " + result.size());
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

	public SwedishTranslation readSwedishForm(Translation T) {
		debug("Buscando SVT con id:" + T.id);
		SQLiteDatabase database = gateway.getReadableDatabase();
		Cursor cursor = database.query(DBSwedishForm.db, DBSwedishForm.cols, DBSwedishForm.translation_id + "=" + T.id, null, null, null, null);
		String[] forms = null;

		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			forms = cursorToSwedishForm(cursor);
		}
		cursor.close();
		database.close();
		return new SwedishTranslation(T, forms);
	}

	/** ********* UPDATES ************* **/
	public void updateLanguage(Language lang) {
		ContentValues values = new ContentValues();
		values.put(DBLanguage.code, lang.CODE);
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

	public void updateSwedishForm(Translation T, String[] forms) {
		ContentValues values = new ContentValues();
		values.put(DBSwedishForm.form1, forms[0]);
		values.put(DBSwedishForm.form2, forms[1]);
		values.put(DBSwedishForm.form3, forms[2]);
		values.put(DBSwedishForm.form4, forms[3]);
		values.put(DBSwedishForm.form5, forms[4]);
		values.put(DBSwedishForm.form6, forms[5]);

		SQLiteDatabase database = gateway.getWritableDatabase();
		database.update(DBSwedishForm.db, values, DBSwedishForm.translation_id + " = " + T.id, null);
		database.close();
	}

	/** ************************ INSERTS ************************ **/
	public Language insertLanguage(int code, String name, int settings) {
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

		values = new ContentValues();
		values.put(DBLanguage.code, code);
		values.put(DBLanguage.name, name);
		values.put(DBLanguage.statistics, stats_id);
		values.put(DBLanguage.settings, settings);
		int lang_id = (int) database.insert(DBLanguage.db, null, values);
		Language language = new Language(code, new Datos(lang_id, name, Settings.NativeLanguage, settings));
		language.setStatistics(new Estadisticas((int) stats_id));
		database.close();
		return language;
	}

	public Word insertWord(String name, Translations translations, int langID, String pronunciation, int priority) {
		ContentValues values = new ContentValues();
		values.put(DBWord.name, name);
		values.put(DBWord.lang_id, langID);
		values.put(DBWord.pronunciation, pronunciation);
		values.put(DBWord.priority, priority);
		SQLiteDatabase database = gateway.getWritableDatabase();
		int word_id = (int) database.insert(DBWord.db, null, values);
		database.close();

		Translations finalTrans = new Translations();
		for (Translation translation : translations) {
			finalTrans.add(insertTranslation(translation, word_id));
		}
		return new Word(word_id, name, finalTrans, pronunciation, Word.INITIAL_PRIORITY);
	}

	public void insertRemovedWord(int langID, int word_id) {
		ContentValues values = new ContentValues();
		values.put(DBRemovedWord.lang_id, langID);
		values.put(DBRemovedWord.word_id, word_id);
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.insert(DBRemovedWord.db, null, values);
		database.close();
	}

	public Translation insertTranslation(Translation translation, int word_id) {
		ContentValues values = new ContentValues();
		values.put(DBTranslation.word_id, word_id);
		values.put(DBTranslation.type, translation.type);
		values.put(DBTranslation.translation, translation.name);
		SQLiteDatabase database = gateway.getWritableDatabase();
		int id = (int) database.insert(DBTranslation.db, null, values);
		database.close();
		return new Translation(id, translation.name, translation.type);
	}

	public DrawerWord insertStoreWord(String note, int langID, int type) {
		ContentValues values = new ContentValues();
		values.put(DBDrawerWord.name, note);
		values.put(DBDrawerWord.lang_id, langID);
		values.put(DBDrawerWord.type, type);
		SQLiteDatabase database = gateway.getWritableDatabase();
		long note_id = database.insert(DBDrawerWord.db, null, values);
		database.close();
		return new DrawerWord((int) note_id, note, type);
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

	public SwedishTranslation insertSwedishForm(Translation T, String[] forms) {
		ContentValues values = new ContentValues();
		values.put(DBSwedishForm.translation_id, T.id);
		values.put(DBSwedishForm.form1, forms[0]);
		values.put(DBSwedishForm.form2, forms[1]);
		values.put(DBSwedishForm.form3, forms[2]);
		values.put(DBSwedishForm.form4, forms[3]);
		values.put(DBSwedishForm.form5, forms[4]);
		values.put(DBSwedishForm.form6, forms[5]);

		SQLiteDatabase database = gateway.getWritableDatabase();
		database.insert(DBSwedishForm.db, null, values);
		database.close();
		return new SwedishTranslation(T, forms);
	}

	/** ********************* DELETES ********************* **/
	public void deleteLanguage(Language lang) {
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

	public void deleteSwedishForm(Translation T) {
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.delete(DBSwedishForm.db, DBSwedishForm.translation_id + " = " + T.id, null);
		database.close();
	}

	/** ********************* Restore ********************* **/
	public void restoreWord(int word_id) {
		SQLiteDatabase database = gateway.getWritableDatabase();
		database.delete(DBRemovedWord.db, DBRemovedWord.word_id + " = " + word_id, null);
		database.close();
	}

	/** ********************* Funciones privadas ********************* **/
	private Language cursorToLanguage(Cursor c) {
		return new Language(c.getInt(1), new Datos(c.getInt(0), c.getString(2), Settings.NativeLanguage, c.getInt(4)));
	}

	private Word cursorToWord(Cursor c) {
		return new Word(c.getInt(0), c.getString(1), null, c.getString(3), c.getInt(4));
	}

	private Translation cursorToTranslation(Cursor c) {
		return new Translation(c.getInt(0), c.getString(3), c.getInt(2));
	}

	private Estadisticas cursorToStatitics(Cursor c) {
		return new Estadisticas(c.getInt(0), c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5));
	}

	private DrawerWord cursorToDrawerWord(Cursor c) {
		return new DrawerWord(c.getInt(0), c.getString(1), c.getInt(3));
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

	private String[] cursorToSwedishForm(Cursor c) {
		String[] forms = new String[6];
		for (int i = 0; i < forms.length; i++) {
			forms[i] = c.getString(i + 2);
		}
		return forms;
	}

	private void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##ControlDB##", text);
	}

}
