package com.delvinglanguages.kernel;

import com.delvinglanguages.kernel.set.Tests;
import com.delvinglanguages.kernel.set.Themes;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.settings.Settings;

public class Datos {

	public final int id;
	public String language_delved_name;
	public String language_native_name;

	public Language delved;
	public LanguageR nativo;

	public Words words;
	public Words removed_words;
	public Estadisticas statistics;
	public Tests tests;
	public Themes themes;

	public int settings;

	public Dictionary dictionary;

	public Datos(int id, String delved_name, String native_name, int settings) {
		this.id = id;
		this.language_delved_name = delved_name;
		this.language_native_name = native_name;
		this.settings = settings;
	}

	public void createDictionary() {
		dictionary = new Dictionary();
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

	private void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##Datos##", text);
	}

}
