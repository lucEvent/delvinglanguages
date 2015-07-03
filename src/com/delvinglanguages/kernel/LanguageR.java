package com.delvinglanguages.kernel;

import java.util.TreeSet;

import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.DrawerWords;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.settings.Settings;

public class LanguageR extends Language {

	public LanguageR(int code, Datos data) {
		super(code, data);

		setLocale();
	}

	/** *********************** Getters *********************** **/
	public String getName() {
		return datos.language_native_name;
	}

	@Override
	public Word getPalabra(String name) {
		return getPalabra(name, datos.dictionary.getDictionaryAt(true, name.charAt(0)));
	}

	@Override
	public Word getPalabra(int id) {
		return super.getPalabra(id).getInverse(false);
	}

	@Override
	public DReference getReference(String name) {
		return datos.dictionary.getReference(true, name);
	}

	@Override
	public DReferences getReferences() {
		return datos.dictionary.getReferences(true);
	}

	@Override
	public boolean contains(Word word) {
		return super.contains(word.getInverse(false));
	}

	@Override
	public Words getRemovedWords() {
		Words removedWords = new Words();
		for (Word word : datos.removed_words) {
			removedWords.add(word.getInverse(false));
		}
		return removedWords;
	}

	@Override
	public TreeSet<DReference> getDiccionary() {
		return datos.dictionary.getDictionary(true);
	}

	@Override
	public TreeSet<DReference> getDiccionaryAt(char charAt) {
		return datos.dictionary.getDictionaryAt(true, charAt);
	}

	@Override
	public DReferences getVerbs() {
		return datos.dictionary.getVerbs(true);
	}

	/** *********************** Setters *********************** **/
	@Override
	public void setWords(Words words) {
		debug("The method 'setWords'of IDNativo shouldn't be called");
	}

	@Override
	public void setDrawerWords(DrawerWords drawer_words) {
		debug("The method 'setDrawerWords' of IDNativo shouldn't be called");
	}

	public void setName(String newname) {
		datos.language_native_name = newname;
	}

	/** *********************** Adders *********************** **/

	@Override
	public void addWord(Word word) {
		datos.indexWord(word.getInverse(true));
	}

	/** *********************** Restores *********************** **/
	@Override
	public void updateWord(Word word, String name, Translations translations, String pronunciation) {
		datos.unindexWord(word.getInverse(true));
		word.setChanges(name, translations, pronunciation);
		datos.indexWord(word.getInverse(false));
	}

	@Override
	public boolean isNativeLanguage() {
		return true;
	}

	private void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##LanguageR##", text);
	}

}
