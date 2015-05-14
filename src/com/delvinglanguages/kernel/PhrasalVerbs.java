package com.delvinglanguages.kernel;

import java.util.TreeMap;

import android.util.Log;

import com.delvinglanguages.kernel.set.Words;

public class PhrasalVerbs {

	private String[] prepositions;
	public TreeMap<String, Boolean[]> phrasals;

	public PhrasalVerbs(Words words, String[] preps) {
		phrasals = new TreeMap<String, Boolean[]>();
		prepositions = new String[preps.length];
		for (int i = 0; i < preps.length; i++) {
			prepositions[i] = preps[i].toLowerCase();
		}
		int typePH = 1 << 4;
		for (Word p : words) {
			if ((typePH & p.getType()) != 0) {
				Log.d("##Phrasals##", "Phrasal: " + p.getName());
				decodePhrasal(p.getName());
			}
		}
	}

	private void decodePhrasal(String p) {
		String[] parts = p.split(" ");
		int prepPart;
		int prep = 0;
		boolean found = false;
		out: for (prepPart = 1; prepPart < parts.length; ++prepPart) {
			for (prep = 0; prep < prepositions.length; prep++) {
				if (prepositions[prep].equals(parts[prepPart])) {
					found = true;
					break out;
				}
			}
		}
		if (!found) {
			Log.d("##Phrasals##", "Phrasal no encontrado. Error? p = " + p);
			return;
		}
		Boolean[] delvs = phrasals.get(parts[0]);
		if (delvs == null) {
			delvs = new Boolean[prepositions.length];
			for (int i = 0; i < delvs.length; i++) {
				delvs[i] = false;
			}
			int pos = 0;
			if (parts[pos].toLowerCase().equals("to")) {
				pos++;
			}
			phrasals.put(parts[pos], delvs);
		}
		delvs[prep] = true;
	}

}
