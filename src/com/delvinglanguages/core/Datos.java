package com.delvinglanguages.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;

import android.util.Log;

public class Datos {

	private String DEBUG = "##Datos##";

	public final int id;
	public String nombre;

	public IDDelved delved;
	public IDNativo nativo;

	public ArrayList<Palabra> palabrasDelved, palabrasNativo;
	public ArrayList<Palabra> papelera;
	public Estadisticas estadistics;

	public Hashtable<Character, TreeSet<DReference>> diccionarioDelved,
			diccionarioNativo;

	public ArrayList<Test> tests;

	protected int[] numtypes;

	// Settings
	public int settings;

	public Datos(int id, String name, String settings) {
		this.id = id;
		nombre = name;
		this.settings = Integer.valueOf(settings);
		dictionariesCreated = false;
	}

	public boolean dictionariesCreated;

	protected void createDictionaries() {
		dictionariesCreated = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				diccionarioDelved = new Hashtable<Character, TreeSet<DReference>>(
						27);
				diccionarioNativo = new Hashtable<Character, TreeSet<DReference>>(
						27);

				for (Palabra word : palabrasDelved) {
					indexarPalabra(word);
				}
				dictionariesCreated = true;
			}
		}).start();
	}

	private void indexarPalabra(Palabra word) {
		TreeSet<DReference> sub;

		ArrayList<String> refsD = Palabra.formatArray(null, word.getName());
		ArrayList<String> refsN = Palabra.formatArray(null,
				word.getTranslation());
		ArrayList<DReference> tempD = new ArrayList<DReference>();
		ArrayList<DReference> tempN = new ArrayList<DReference>();
		for (String refD : refsD) {
			if (refD.length() == 0)
				continue;
			DReference temp = null;
			Character cap = refD.charAt(0);
			sub = diccionarioDelved.get(cap);
			if (sub == null) {
				sub = new TreeSet<DReference>();
				diccionarioDelved.put(cap, sub);
			} else {
				temp = sub.ceiling(new DReference(refD));
			}

			if (temp == null || !temp.item.equals(refD)) {
				temp = new DReference(refD);
				sub.add(temp);
			}
			tempD.add(temp);
		}
		for (String refN : refsN) {
			if (refN.length() == 0)
				continue;
			DReference temp = null;
			Character cap = refN.charAt(0);
			sub = diccionarioNativo.get(cap);
			if (sub == null) {
				sub = new TreeSet<DReference>();
				diccionarioNativo.put(cap, sub);
			} else {
				temp = sub.ceiling(new DReference(refN));
			}

			if (temp == null || !temp.item.equals(refN)) {
				temp = new DReference(refN);
				sub.add(temp);
			}
			tempN.add(temp);
		}
		for (DReference refD : tempD) {
			for (DReference refN : tempN) {
				refD.addReference(refN, word);
			}
		}
		for (DReference refN : tempN) {
			for (DReference refD : tempD) {
				refN.addReference(refD, word);
			}
		}
	}

	public void indexa(Palabra enDelv) {
		// Add to palabras
		palabrasDelved.add(enDelv);
		if (palabrasNativo != null)
			palabrasNativo.add(enDelv.cloneReverse());

		// Add to diccionarios
		indexarPalabra(enDelv);
	}

	public void desindexa(Palabra enDelv) {
		// Remove into palabras
		palabrasDelved.remove(enDelv);
		palabrasNativo.remove(enDelv.cloneReverse());

		// Remove in the diccionaries
		TreeSet<DReference> sub;

		ArrayList<String> refsD = Palabra.formatArray(null, enDelv.getName());
		ArrayList<String> refsN = Palabra.formatArray(null,
				enDelv.getTranslation());

		for (String refD : refsD) {
			Character car = refD.charAt(0);
			sub = diccionarioDelved.get(car);
			DReference temp = sub.ceiling(new DReference(refD));

			temp.removeReferencesto(enDelv);
			if (temp.owners.isEmpty()) {
				Log.d(DEBUG,"##removed1?##->"+sub.remove(temp));
				if (sub.isEmpty()) {
					diccionarioDelved.remove(car);
				}
			}
		}
		for (String refN : refsN) {
			Character car = refN.charAt(0);
			sub = diccionarioNativo.get(car);
			DReference temp = sub.ceiling(new DReference(refN));

			temp.removeReferencesto(enDelv);
			if (temp.owners.isEmpty()) {
				Log.d(DEBUG,"##removed2?##->"+sub.remove(temp));
				if (sub.isEmpty()) {
					diccionarioNativo.remove(car);
				}
			}
		}
	}

}
