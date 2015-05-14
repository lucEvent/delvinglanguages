package com.delvinglanguages.kernel;

import com.delvinglanguages.kernel.set.Themes;
import com.delvinglanguages.kernel.set.Tests;
import com.delvinglanguages.kernel.set.Words;

public class Datos {

	private String DEBUG = "##Datos##";

	public final int id;
	public String language_delved_name;
	public String language_native_name;

	public IDDelved delved;
	public IDNativo nativo;

	public Words words;
	public Words removed_words;
	public Estadisticas statistics;
	public Tests tests;
	public Themes themes;

	public int settings;

	public Dictionary dictionary;

	public Datos(int id, String delved_name, String native_name, String settings) {
		this.id = id;
		this.language_delved_name = delved_name;
		this.language_native_name = native_name;
		this.settings = Integer.valueOf(settings);
	}

	public void createDictionary(Character[] indexsD, Character[] indexsN) {
		dictionary = new Dictionary(indexsD, indexsN);
		dictionary.addEntries(words);
	}

	public void indexWord(Word enDelv) {
		words.add(enDelv);
		dictionary.addEntry(enDelv);
	}

	public void unindexWord(Word enDelv) {
		words.remove(enDelv);
		dictionary.removeEntry(enDelv);
	}

}
