package com.delvinglanguages.core;

import java.util.ArrayList;
import java.util.Random;

import android.R.integer;

public class FoneticTree {

	public class FoneticWord {
		public Word word;
		public ArrayList< ArrayList<Word> > matches;
		
		public FoneticWord(Word word) {
			this.word = word;
		}
	}
	
	private class Affinity {
		int mejorPosAfin;
		int afinidadIZQ;
		int afininadDER;
	}
	
	private ArrayList<Word> palabras;
	
	public FoneticTree(ArrayList<Word> words) {
		palabras = words;
	}
	
	public FoneticWord makeTree(int position) {
		return treeMaker(palabras.get(position));
	}
	
	public FoneticWord makeTree(Word p) {
		return treeMaker(p);
	}
	
	public FoneticWord makeTree() {
		int pos = new Random().nextInt(palabras.size());
		return treeMaker(palabras.get(pos));
	}
	
	private FoneticWord treeMaker(Word p) {
		// ------- Save state -----
		int tmppospal = palabras.indexOf(p);
		palabras.remove(p);
		// --------------
		FoneticWord res = new FoneticWord(p);
		String fonetica = p.getPronunciation();
		String puntuacion = "¡'!\"·$%&/()=?¿^*¨_:;,.-´`+";
		int lenght = fonetica.length();
		for (int i = 0; i < fonetica.length(); i++) {
			if (puntuacion.contains(fonetica.charAt(i)+"")) {
				lenght--;
			}
		}
		res.matches = new ArrayList< ArrayList<Word> >(lenght);
		
		for (int i = 0; i < palabras.size(); i++) {
			Word q = palabras.get(i);
			String qfon = q.getPronunciation();
			for (int j = 0; j < fonetica.length(); ++j) {
				Affinity af = affinityBetween(fonetica, qfon, i);
				
			}
		}
		
		// ------ Restore state ------
		if (tmppospal != -1) {
			palabras.add(p);
		}
		// --------------
		return res;
	}

	private Affinity affinityBetween(String fonetica, String qfon, int i) {
	
		return null;
	}
	
	
	
}
