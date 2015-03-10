package com.delvinglanguages.core;

import java.util.ArrayList;

import com.delvinglanguages.kernel.set.Themes;
import com.delvinglanguages.kernel.set.Tests;

public class Datos {

	private String DEBUG = "##Datos##";

	public final int id;
	public String nombre;

	public IDDelved delved;
	public IDNativo nativo;

	public ArrayList<Word> palabrasDelved, palabrasNativo;
	public ArrayList<Word> papelera;
	public Estadisticas estadistics;

	public Tests tests;

	public Themes themes;

	// Settings
	public int settings;

	public Dictionary dictionary;

	public Datos(int id, String name, String settings) {
		this.id = id;
		nombre = name;
		this.settings = Integer.valueOf(settings);

	}

	public void createDictionary(Character[] indexsD, Character[] indexsN) {
		dictionary = new Dictionary(indexsD, indexsN);
		dictionary.addEntries(palabrasDelved);
	}

	public boolean isDictionaryCreated() {
		return dictionary.dictionariesCreated;
	}

	public void indexa(Word enDelv) {
		// Add to palabras
		palabrasDelved.add(enDelv);
		if (palabrasNativo != null)
			palabrasNativo.add(enDelv.cloneReverse());

		// Add to diccionarios
		dictionary.addEntry(enDelv);
	}

	public void desindexa(Word enDelv) {
		// Remove into palabras
		palabrasDelved.remove(enDelv);
		palabrasNativo.remove(enDelv.cloneReverse());

		dictionary.removeEntry(enDelv);
	}

}
