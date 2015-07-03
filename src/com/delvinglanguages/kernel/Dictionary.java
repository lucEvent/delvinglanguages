package com.delvinglanguages.kernel;

import java.util.ArrayList;
import java.util.TreeSet;

import com.delvinglanguages.kernel.set.DReferences;
import com.delvinglanguages.kernel.set.Translations;
import com.delvinglanguages.kernel.set.Words;
import com.delvinglanguages.settings.Settings;

public class Dictionary {

	private int[] type_counter;
	private TreeSet<DReference> dictionary, dictionary_inverse;
	public boolean dictionaryCreated;

	public Dictionary() {
		type_counter = new int[7];
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
		for (String refDS : refsD) {
			if (refDS.length() == 0) {
				continue;
			}
			DReference refD = new DReference(refDS, entry.pronunciacion, entry.prioridad);
			if (!dictionary.add(refD)) {
				refD = dictionary.ceiling(refD);
			}

			for (Translation transN : refsN) {
				for (String refNS : transN.getItems()) {
					if (refNS.length() == 0) {
						continue;
					}
					DReference refN = new DReference(transN.id, refNS, "", entry.prioridad, transN.type);
					if (!dictionary_inverse.add(refN)) {
						refN = dictionary_inverse.ceiling(refN);
					}
					refD.linkTo(entry, refN);
					refN.linkTo(entry, refD);
				}
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

		ArrayList<String> refsD = entry.getNameArray();
		Translations refsN = entry.getTranslations();
		for (String refDS : refsD) {
			DReference refD = dictionary.ceiling(new DReference(refDS, -1));

			for (Translation transN : refsN) {
				for (String refNS : transN.getItems()) {
					DReference refN = dictionary_inverse.ceiling(new DReference(refNS, transN.type));

					refD.unlink(entry, refN);
					refN.unlink(entry, refD);

					if (refN.links.isEmpty()) {
						dictionary_inverse.remove(refN);
					}

				}
			}

			if (refD.links.isEmpty()) {
				dictionary.remove(refD);
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
		DReference ceiling = dictionary.ceiling(new DReference("" + at, -1));
		if (ceiling == null || Language.collator.compare(at + "", ceiling.name.charAt(0) + "") != 0) {
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
		DReference floor = dictionary.floor(new DReference(next, -1));
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
		DReference candidate = dictionary.ceiling(new DReference(name, -1));
		if (candidate != null && Language.collator.compare(candidate.name, name) == 0) {
			return candidate;
		}
		return null;
	}

	private void debug(String text) {
		if (Settings.DEBUG)
			android.util.Log.d("##Dictionary##", text);
	}

}
