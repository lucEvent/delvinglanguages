package com.delvinglanguages.core;

import java.util.ArrayList;
import java.util.Random;

import android.R.integer;

public class FoneticTree {

	public class PalabraFonetica {
		public Palabra word;
		public ArrayList< ArrayList<Palabra> > matches;
		
		public PalabraFonetica(Palabra word) {
			this.word = word;
		}
	}
	
	private class Affinity {
		int mejorPosAfin;
		int afinidadIZQ;
		int afininadDER;
	}
	
	private ArrayList<Palabra> palabras;
	
	public FoneticTree(ArrayList<Palabra> words) {
		palabras = words;
	}
	
	public PalabraFonetica makeTree(int position) {
		return treeMaker(palabras.get(position));
	}
	
	public PalabraFonetica makeTree(Palabra p) {
		return treeMaker(p);
	}
	
	public PalabraFonetica makeTree() {
		int pos = new Random().nextInt(palabras.size());
		return treeMaker(palabras.get(pos));
	}
	
	private PalabraFonetica treeMaker(Palabra p) {
		// ------- Save state -----
		int tmppospal = palabras.indexOf(p);
		palabras.remove(p);
		// --------------
		PalabraFonetica res = new PalabraFonetica(p);
		String fonetica = p.getPronunciation();
		String puntuacion = "¡'!\"·$%&/()=?¿^*¨_:;,.-´`+";
		int lenght = fonetica.length();
		for (int i = 0; i < fonetica.length(); i++) {
			if (puntuacion.contains(fonetica.charAt(i)+"")) {
				lenght--;
			}
		}
		res.matches = new ArrayList< ArrayList<Palabra> >(lenght);
		
		for (int i = 0; i < palabras.size(); i++) {
			Palabra q = palabras.get(i);
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
