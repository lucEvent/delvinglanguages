package com.delvinglanguages.kernel;

import java.util.ArrayList;
import java.util.TreeSet;

import android.util.Log;

import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.settings.Settings;

public class Dictionary {

	private static final String DEBUG = "##Dictionary##";

	private int[] type_counter;
	private TreeSet<DReference> dictionary, dictionary_inverse;
	public boolean dictionaryCreated;

	public Dictionary(Character[] indexsD, Character[] indexsN) {
		type_counter = new int[Settings.NUM_TYPES];
		dictionary = new TreeSet<DReference>();
		dictionary_inverse = new TreeSet<DReference>();
		for (int i = 0; i < type_counter.length; i++) {
			type_counter[i] = 0;
		}
		dictionaryCreated = false;
	}

	public void addEntries(final Words entries) {
		dictionaryCreated = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (Word word : entries) {
					addEntry(word);
				}
				dictionaryCreated = true;
			}
		}).start();
	}

	public void addEntry(Word entry) {
		int type = entry.getType();
		for (int i = 0; i < type_counter.length; i++) {
			if ((type & (1 << i)) != 0) {
				type_counter[i]++;
			}
		}

		ArrayList<String> refsD = entry.getNameArray();
		Translations refsN = entry.getTranslations();
		DReferences tempD = new DReferences();
		for (String refD : refsD) {
			if (refD.length() == 0) {
				continue;
			}
			DReference ref = new DReference(refD);
			DReference temp = dictionary.ceiling(ref);
			if (temp == null || !temp.getName().equals(refD)) {
				temp = ref;
				dictionary.add(temp);
			}
			tempD.add(temp);
		}
		DReferences tempN = new DReferences();
		for (Translation transN : refsN) {
			for (String refN : transN.getItems()) {
				if (refN.length() == 0) {
					continue;
				}
				DReference ref = new DReference(refN, transN.type);
				DReference temp = dictionary_inverse.ceiling(ref);
				if (temp == null || !temp.getName().equals(refN)) {
					temp = ref;
					dictionary_inverse.add(temp);
				}
				tempN.add(temp);
			}
		}
		for (DReference refD : tempD) {
			for (DReference refN : tempN) {
				refD.addReference(refN, entry, refN.getType());
				refN.addReference(refD, entry);
			}
		}
	}

	public void removeEntry(Word entry) {
		int type = entry.getType();
		for (int i = 0; i < type_counter.length; i++) {
			if ((type & (1 << i)) != 0) {
				type_counter[i]--;
			}
		}

		{ // debug
			Log.d(DEBUG, "DESINDEXANDO: " + entry.nombre);
			Log.d(DEBUG, " el contenido del diccionario es:");
			for (DReference R : dictionary) {
				Log.d(DEBUG, " --->" + R.getName());
			}
			Log.d(DEBUG, "el contenido del diccionario INVERSO es:");
			for (DReference R : dictionary_inverse) {
				Log.d(DEBUG, " --->" + R.getName());
			}
		}// end debug

		ArrayList<String> refsD = entry.getNameArray();
		ArrayList<String> refsN = entry.getTranslationArray();
		for (String refD : refsD) {
			DReference temp = dictionary.ceiling(new DReference(refD));
			temp.removeReferencesto(entry);
			if (temp.links.isEmpty()) {
				dictionary.remove(temp);
			}
		}
		for (String refN : refsN) {
			DReference temp = dictionary_inverse.ceiling(new DReference(refN));
			if (temp == null) {
				Log.d(DEBUG, "refN es " + refN + " y word es: " + entry.nombre);
			}
			temp.removeReferencesto(entry);
			if (temp.links.isEmpty()) {
				dictionary_inverse.remove(temp);
			}
		}
	}

	public int[] getTypeCounter() {
		return type_counter;
	}

	public DReferences getReferences(boolean inverse) {
		TreeSet<DReference> dictionary = this.dictionary;
		if (inverse) {
			dictionary = this.dictionary_inverse;
		}
		return new DReferences(dictionary);
	}

	public DReferences getPhrasalVerbs(boolean inverse) {
		TreeSet<DReference> dictionary = this.dictionary;
		if (inverse) {
			dictionary = this.dictionary_inverse;
		}
		DReferences res = new DReferences();
		for (DReference ref : dictionary) {
			if (ref.isPhrasalVerb()) {
				res.add(ref);
			}
		}
		return res;
	}

	public DReferences getVerbs(boolean inverse) {
		TreeSet<DReference> dictionary = this.dictionary;
		if (inverse) {
			dictionary = this.dictionary_inverse;
		}
		DReferences res = new DReferences();
		for (DReference ref : dictionary) {
			if (ref.isVerb()) {
				res.add(ref);
			}
		}
		return res;
	}

	public TreeSet<DReference> getDictionary(boolean inverse) {
		if (inverse) {
			return dictionary_inverse;
		}
		return dictionary;
	}

	public TreeSet<DReference> getDictionaryAt(boolean inverse, char at) {
		TreeSet<DReference> dictionary = this.dictionary;
		if (inverse) {
			dictionary = this.dictionary_inverse;
		}
		DReference ceiling = dictionary.ceiling(new DReference("" + at));
		if (ceiling == null || IDDelved.collator.compare(at + "", ceiling.getName().charAt(0) + "") != 0) {
			return new TreeSet<DReference>();
		}
		String next = "" + (char) (at + 1);
		if (at >= 'Z') {
			switch (at) {
			case 'Z':
				next = "Z÷÷";
				break;
			case 'ƒ':
				next = "ƒ÷÷";
				break;
			case '≈':
				next = "≈÷÷";
				break;
			case '—':
				next = "—÷÷";
				break;
			case '÷':
				next = "÷÷÷";
				break;
			}
		}
		DReference floor = dictionary.floor(new DReference(next));
		if (floor == null) {
			return new TreeSet<DReference>();
		}
		return (TreeSet<DReference>) dictionary.subSet(ceiling, true, floor, true);
	}

	public DReference getReference(boolean inverse, String name) {
		TreeSet<DReference> dictionary = this.dictionary;
		if (inverse) {
			dictionary = this.dictionary_inverse;
		}
		DReference candidate = dictionary.ceiling(new DReference(name));
		if (candidate != null && IDDelved.collator.compare(candidate.getName(), name) == 0) {
			return candidate;
		}
		return null;
	}
}
