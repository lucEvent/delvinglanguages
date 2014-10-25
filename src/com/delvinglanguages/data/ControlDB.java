package com.delvinglanguages.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.delvinglanguages.core.Datos;
import com.delvinglanguages.core.Estadisticas;
import com.delvinglanguages.core.IDDelved;
import com.delvinglanguages.core.Nota;
import com.delvinglanguages.core.Palabra;
import com.delvinglanguages.core.Test;
import com.delvinglanguages.core.Tense;

public class ControlDB {

	private static final String DEBUG = "##ControlDB##";
	private SQLiteDatabase database;
	private DataBase gateway;

	public ControlDB(Context context) {
		gateway = new DataBase(context);
	}

	/** ********* Lecturas en la BD ************* **/
	public ArrayList<IDDelved> readLanguages() {
		database = gateway.getReadableDatabase();
		ArrayList<IDDelved> result = new ArrayList<IDDelved>();
		Cursor cursorID = database.query(DataBase.idioma, DataBase.col_idioma,
				null, null, null, null, null);

		Cursor cursorES = database.query(DataBase.estadisticas,
				DataBase.col_estadisticas, null, null, null, null, null);

		cursorID.moveToFirst();
		cursorES.moveToFirst();
		while (!cursorID.isAfterLast()) {
			IDDelved language = cursorToIdioma(cursorID);
			language.setEstadisticas(cursorToEstadisticas(cursorES));
			result.add(language);
			cursorID.moveToNext();
			cursorES.moveToNext();
		}
		cursorID.close();
		cursorES.close();
		database.close();
		return result;
	}

	public ArrayList<Palabra> readWords(int langID) {
		database = gateway.getReadableDatabase();
		ArrayList<Palabra> result = new ArrayList<Palabra>();
		Cursor cursor = database.query(DataBase.palabra, DataBase.col_palabra,
				DataBase.col_palabra[3] + "=" + langID, null, null, null,
				DataBase.col_palabra[1] + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Palabra pal = cursorToPalabra(cursor);
			result.add(pal);
			cursor.moveToNext();
		}
		cursor.close();
		Log.d(DEBUG, "Palabras leidas: " + result.size());
		database.close();
		return result;
	}

	public ArrayList<Nota> readStore(int langID) {
		database = gateway.getReadableDatabase();
		ArrayList<Nota> result = new ArrayList<Nota>();
		Cursor cursor = database.query(DataBase.almacen, DataBase.col_almacen,
				DataBase.col_almacen[2] + "=" + langID, null, null, null,
				DataBase.col_almacen[1] + " ASC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			result.add(cursorToNota(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		Log.d(DEBUG, "Almacen leidas: " + result.size());
		database.close();
		return result;
	}

	public ArrayList<Test> readTests(int langID) {
		database = gateway.getReadableDatabase();
		ArrayList<Test> result = new ArrayList<Test>();
		Cursor cursor = database.query(DataBase.test, DataBase.col_test,
				DataBase.col_test[2] + "=" + langID, null, null, null,
				DataBase.col_test[1] + " ASC");

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

	/** ********* Modificaciones en la BD ************* **/
	public void updateLanguage(IDDelved lang) {
		ContentValues values = new ContentValues();
		values.put(DataBase.col_idioma[1], lang.getName());
		values.put(DataBase.col_idioma[3], lang.getSettings());
		database = gateway.getWritableDatabase();
		database.update(DataBase.idioma, values, DataBase.col_idioma[0] + " = "
				+ lang.getID(), null);
		database.close();
	}

	public void updateWord(Palabra p) {
		ContentValues values = new ContentValues();
		values.put(DataBase.col_palabra[1], p.getName());
		values.put(DataBase.col_palabra[2], p.getTranslationFormated());
		values.put(DataBase.col_palabra[4], p.getPronunciation());
		values.put(DataBase.col_palabra[5], p.getType());
		database = gateway.getWritableDatabase();
		database.update(DataBase.palabra, values, DataBase.col_palabra[0]
				+ " = " + p.id, null);
		database.close();
	}

	public void integrateWord(Palabra p, int newlangid) {
		ContentValues values = new ContentValues();
		values.put(DataBase.col_palabra[3], newlangid);
		database = gateway.getWritableDatabase();
		database.update(DataBase.palabra, values, DataBase.col_palabra[0]
				+ " = " + p.id, null);
		database.close();
	}

	public void integrateStore(int oldlangid, int newlangid) {
		ContentValues values = new ContentValues();
		values.put(DataBase.col_almacen[2], newlangid);
		database = gateway.getWritableDatabase();
		database.update(DataBase.almacen, values, DataBase.col_almacen[2]
				+ " = " + oldlangid, null);
		database.close();
	}

	public void updatePriority(Palabra p) {
		ContentValues values = new ContentValues();
		values.put(DataBase.col_palabra[7], p.getPriority());
		database = gateway.getWritableDatabase();
		database.update(DataBase.palabra, values, DataBase.col_palabra[0]
				+ " = " + p.id, null);
		database.close();
	}

	public void updateTest(Test t) {
		database = gateway.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DataBase.col_test[3], t.encapsulate());
		database.update(DataBase.test, values, DataBase.col_test[0] + " = "
				+ t.id, null);
		database.close();
	}

	public void throworrestoreWord(Palabra p) {
		int place = p.isThrown() ? 0 : 1;
		ContentValues values = new ContentValues();
		values.put(DataBase.col_palabra[6], place);
		database = gateway.getWritableDatabase();
		database.update(DataBase.palabra, values, DataBase.col_palabra[0]
				+ " = " + p.id, null);
		database.close();
	}

	public void updateStatistics(Estadisticas e) {
		ContentValues values = new ContentValues();
		values.put(DataBase.col_estadisticas[1], e.intentos);
		values.put(DataBase.col_estadisticas[2], e.aciertos1);
		values.put(DataBase.col_estadisticas[3], e.aciertos2);
		values.put(DataBase.col_estadisticas[4], e.aciertos3);
		values.put(DataBase.col_estadisticas[5], e.fallos);
		database = gateway.getWritableDatabase();
		database.update(DataBase.estadisticas, values,
				DataBase.col_estadisticas[0] + " = " + e.id, null);
		database.close();
	}

	/** ********* Escrituras en la BD ************* **/
	public IDDelved insertLanguage(String name) {
		// Inserting stadistics
		ContentValues values = new ContentValues();
		values.put(DataBase.col_estadisticas[1], 0);
		values.put(DataBase.col_estadisticas[2], 0);
		values.put(DataBase.col_estadisticas[3], 0);
		values.put(DataBase.col_estadisticas[4], 0);
		values.put(DataBase.col_estadisticas[5], 0);
		database = gateway.getWritableDatabase();
		long estadid = database.insert(DataBase.estadisticas, null, values);

		// Inserting language
		String settings = Integer.toString(0);

		values = new ContentValues();
		values.put(DataBase.col_idioma[1], name);
		values.put(DataBase.col_idioma[2], estadid);
		values.put(DataBase.col_idioma[3], settings);
		long idid = database.insert(DataBase.idioma, null, values);
		IDDelved language = new IDDelved(new Datos((int) idid, name, settings));
		language.setEstadisticas(new Estadisticas((int) estadid));
		database.close();
		return language;
	}

	public Palabra insertWord(String name, String trad, int langID,
			String pron, int type) {
		// Inserting word
		ContentValues values = new ContentValues();
		values.put(DataBase.col_palabra[1], name);
		values.put(DataBase.col_palabra[2], trad);
		values.put(DataBase.col_palabra[3], langID);
		values.put(DataBase.col_palabra[4], pron);
		values.put(DataBase.col_palabra[5], type);
		values.put(DataBase.col_palabra[6], 0);
		values.put(DataBase.col_palabra[7], Palabra.INITIAL_PRIORITY);
		database = gateway.getWritableDatabase();
		long palid = database.insert(DataBase.palabra, null, values);
		database.close();
		return new Palabra((int) palid, name, trad, pron, type, false,
				Palabra.INITIAL_PRIORITY);
	}

	public Nota insertStoreWord(String pal, int langID, int type) {
		// Inserting almacen
		ContentValues values = new ContentValues();
		values.put(DataBase.col_almacen[1], pal);
		values.put(DataBase.col_almacen[2], langID);
		values.put(DataBase.col_almacen[3], type);
		database = gateway.getWritableDatabase();
		long notid = database.insert(DataBase.almacen, null, values);
		database.close();
		return new Nota((int) notid, pal, type);
	}

	public int insertTest(String name, int langID, String content) {
		// Inserting test
		ContentValues values = new ContentValues();
		values.put(DataBase.col_test[1], name);
		values.put(DataBase.col_test[2], langID);
		values.put(DataBase.col_test[3], content);
		database = gateway.getWritableDatabase();
		long tid = database.insert(DataBase.test, null, values);
		database.close();
		return (int) tid;
	}

	/** ********* Borrados en la BD ************* **/
	public void removeLanguage(int id) {
		database = gateway.getWritableDatabase();
		// Removing words
		database.delete(DataBase.palabra, DataBase.col_palabra[3] + " = " + id,
				null);
		// Removing store
		database.delete(DataBase.almacen, DataBase.col_almacen[2] + " = " + id,
				null);
		// Removing statistics
		database.delete(DataBase.estadisticas, DataBase.col_estadisticas[0]
				+ " = " + id, null);
		// Removing tests
		database.delete(DataBase.test, DataBase.col_test[2] + " = " + id, null);
		// Removing tiempos verbales
		database.delete(DataBase.tiempo_verbal, DataBase.col_tiempo_verbal[1]
				+ " = " + id, null);
		// Removing language
		database.delete(DataBase.idioma, DataBase.col_idioma[0] + " = " + id,
				null);
		database.close();
	}

	public void removeWord(int id) {
		database = gateway.getWritableDatabase();
		database.delete(DataBase.palabra, DataBase.col_palabra[0] + " = " + id,
				null);
		database.close();
	}

	public void clearTrash(int idid) {
		database = gateway.getWritableDatabase();
		database.delete(DataBase.palabra, DataBase.col_palabra[3] + " = "
				+ idid + " AND " + DataBase.col_palabra[6] + " = 1", null);
		database.close();
	}

	public void removeStoredWord(int id) {
		database = gateway.getWritableDatabase();
		database.delete(DataBase.almacen, DataBase.col_almacen[0] + " = " + id,
				null);
		database.close();
	}

	public void removeTest(int id) {
		database = gateway.getWritableDatabase();
		database.delete(DataBase.test, DataBase.col_test[0] + " = " + id, null);
		database.close();
	}

	public Tense getTense(int verbId, int tense) {
		database = gateway.getReadableDatabase();
		Cursor cursor = database.query(DataBase.tiempo_verbal,
				DataBase.col_tiempo_verbal, DataBase.col_tiempo_verbal[2]
						+ " = " + verbId + " and "
						+ DataBase.col_tiempo_verbal[5] + " = " + tense, null,
				null, null, null);

		Tense result = null;
		cursor.moveToFirst();
		Log.d(DEBUG, "Encontrados:" + cursor.getCount() + " buscando tense:"
				+ tense + " en verbid:" + verbId);
		while (!cursor.isAfterLast()) {
			result = new Tense(cursor.getInt(0), cursor.getString(3),
					cursor.getString(4), cursor.getInt(5));
			break;
		}
		cursor.close();
		database.close();
		return result;
	}

	public Tense insertTense(int langId, int verbId, String forms, String pron,
			int opts) {
		ContentValues values = new ContentValues();
		values.put(DataBase.col_tiempo_verbal[1], langId);
		values.put(DataBase.col_tiempo_verbal[2], verbId);
		values.put(DataBase.col_tiempo_verbal[3], forms);
		values.put(DataBase.col_tiempo_verbal[4], pron);
		values.put(DataBase.col_tiempo_verbal[5], opts);
		database = gateway.getWritableDatabase();
		int cid = (int) database.insert(DataBase.tiempo_verbal, null, values);
		database.close();
		Log.d(DEBUG, "Result:" + cid + " insertando tense:" + opts
				+ " en verbid:" + verbId);
		return new Tense(cid, forms, pron, opts);
	}

	public void updateTense(Tense tns) {
		ContentValues values = new ContentValues();
		values.put(DataBase.col_tiempo_verbal[3], tns.getForms());
		values.put(DataBase.col_tiempo_verbal[4], tns.getPronuntiations());
		database = gateway.getWritableDatabase();
		database.update(DataBase.tiempo_verbal, values,
				DataBase.col_tiempo_verbal[0] + " = " + tns.id, null);
		database.close();
	}

	public void removeTenses(int verbId) {
		database = gateway.getWritableDatabase();
		database.delete(DataBase.tiempo_verbal, DataBase.col_tiempo_verbal[2]
				+ " = " + verbId, null);
		database.close();
	}

	/**
	 * Funciones privadas
	 */
	private IDDelved cursorToIdioma(Cursor c) {
		return new IDDelved(new Datos(c.getInt(0), c.getString(1),
				c.getString(3)));
	}

	private Palabra cursorToPalabra(Cursor c) {
		boolean b = c.getInt(6) == 1 ? true : false;

		return new Palabra(c.getInt(0), c.getString(1), c.getString(2),
				c.getString(4), c.getInt(5), b, c.getInt(7));
	}

	private Estadisticas cursorToEstadisticas(Cursor c) {
		return new Estadisticas(c.getInt(0), c.getInt(1), c.getInt(2),
				c.getInt(3), c.getInt(4), c.getInt(5));
	}

	private Nota cursorToNota(Cursor c) {
		return new Nota(c.getInt(0), c.getString(1), c.getInt(3));
	}

	private Test cursorToTest(Cursor c) {
		return new Test(c.getInt(0), c.getString(1), c.getString(3));
	}

}
