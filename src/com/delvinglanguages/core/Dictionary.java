package com.delvinglanguages.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import com.delvinglanguages.settings.Configuraciones;

public class Dictionary {

	private static final String DEBUG = null;
	public int[] types;
	public HashMap<Character, TreeSet<DReference>> dictionary_D_to_N,
			dictionary_N_to_D;

	public Dictionary(Character[] indexsD, Character[] indexsN) {
		dictionary_D_to_N = new HashMap<Character, TreeSet<DReference>>(
				indexsD.length);
		for (int i = 0; i < indexsD.length; i++) {
			dictionary_D_to_N.put(indexsD[i], new TreeSet<DReference>());
		}
		dictionary_N_to_D = new HashMap<Character, TreeSet<DReference>>(
				indexsN.length);
		for (int i = 0; i < indexsN.length; i++) {
			dictionary_D_to_N.put(indexsN[i], new TreeSet<DReference>());
		}
		types = new int[Configuraciones.NUM_TYPES];
		for (int i = 0; i < types.length; i++) {
			types[i] = 0;
		}
	}

	public boolean dictionariesCreated;

	public void addEntries(final ArrayList<Word> entries) {
		dictionariesCreated = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (Word word : entries) {
					addEntry(word);
				}
				dictionariesCreated = true;
			}
		}).start();
	}

	public void addEntry(Word entry) {
		int etype = entry.getType();
		for (int i = 0; i < types.length; i++) {
			if (((1 << i) & etype) != 0) {
				types[i]++;
			}
		}

		TreeSet<DReference> sub;

		ArrayList<String> refsD = Word.formatArray(null, entry.getName());
		ArrayList<String> refsN = Word
				.formatArray(null, entry.getTranslation());
		ArrayList<DReference> tempD = new ArrayList<DReference>();
		for (String refD : refsD) {
			if (refD.length() == 0) {
				continue;
			}
			DReference temp = null;
			Character cap = refD.charAt(0);
			sub = dictionary_D_to_N.get(cap);
			if (sub == null) {
				sub = new TreeSet<DReference>();
				dictionary_D_to_N.put(cap, sub);
			} else {
				temp = sub.ceiling(new DReference(refD));
			}

			if (temp == null || !temp.name.equals(refD)) {
				temp = new DReference(refD);
				sub.add(temp);
			}
			tempD.add(temp);
		}
		ArrayList<DReference> tempN = new ArrayList<DReference>();
		for (String refN : refsN) {
			if (refN.length() == 0) {
				continue;
			}
			DReference temp = null;
			Character cap = refN.charAt(0);
			sub = dictionary_N_to_D.get(cap);
			if (sub == null) {
				sub = new TreeSet<DReference>();
				dictionary_N_to_D.put(cap, sub);
			} else {
				temp = sub.ceiling(new DReference(refN));
			}

			if (temp == null || !temp.name.equals(refN)) {
				temp = new DReference(refN);
				sub.add(temp);
			}
			tempN.add(temp);
		}
		for (DReference refD : tempD) {
			for (DReference refN : tempN) {
				refD.addReference(refN, entry);
				refN.addReference(refD, entry);
			}
		}
	}

	public void removeEntry(Word entry) {
		int etype = entry.getType();
		for (int i = 0; i < types.length; i++) {
			if (((1 << i) & etype) != 0) {
				types[i]--;
			}
		}

		TreeSet<DReference> sub;

		ArrayList<String> refsD = Word.formatArray(null, entry.getName());
		ArrayList<String> refsN = Word
				.formatArray(null, entry.getTranslation());

		for (String refD : refsD) {
			Character car = refD.charAt(0);
			sub = dictionary_D_to_N.get(car);
			DReference temp = sub.ceiling(new DReference(refD));

			temp.removeReferencesto(entry);
			if (temp.links.isEmpty()) {
				sub.remove(temp);
			}
		}
		for (String refN : refsN) {
			Character car = refN.charAt(0);
			sub = dictionary_N_to_D.get(car);
			DReference temp = sub.ceiling(new DReference(refN));

			temp.removeReferencesto(entry);
			if (temp.links.isEmpty()) {
				sub.remove(temp);
			}
		}
	}

	public int[] getTypesCount() {
		return types;
	}

}
