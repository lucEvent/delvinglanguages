package com.delvinglanguages.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

public class IDNativo extends IDDelved {

	private static final String DEBUG = "##IDNativo##";

	/** **************** CREADORAS **************** **/
	public IDNativo(Datos data) {
		super(data);
		// Aqui tengo que modificar CODE
	}

	/** *************** CONSULTORAS **************** **/
	public Word getPalabra(String name) {
		return getPalabra(name, datos.dictionary.dictionary_N_to_D.get(name.charAt(0)));
	}

	public Word getPalabra(int id) {
		return super.getPalabra(id).cloneReverse();
	}

	public DReference getReference(String name) {
		TreeSet<DReference> sub = datos.dictionary.dictionary_N_to_D.get(name.charAt(0));
		if (sub != null) {
			Iterator<DReference> it = sub.iterator();
			while (it.hasNext()) {
				DReference temp = it.next();
				if (temp.item.equals(name)) {
					return temp;
				}
			}
		}
		return null;
	}

	public ArrayList<Word> getPapelera() {
		ArrayList<Word> papelera = new ArrayList<Word>();
		for (int i = 0; i < datos.papelera.size(); i++) {
			papelera.add(datos.papelera.get(i).cloneReverse());
		}
		return datos.papelera;
	}

	public boolean contains(Word p) {
		return datos.palabrasNativo.contains(p);
	}

	public ArrayList<Word> getPalabras() {
		return datos.palabrasNativo;
	}

	public HashMap<Character, TreeSet<DReference>> getDiccionario() {
		return datos.dictionary.dictionary_N_to_D;
	}

	public ArrayList<DReference> getVerbs() {
		ArrayList<DReference> verbs = new ArrayList<DReference>();
		TreeSet<Character> keys = new TreeSet<Character>(
				datos.dictionary.dictionary_N_to_D.keySet());
		for (Character key : keys) {
			TreeSet<DReference> sub = datos.dictionary.dictionary_N_to_D.get(key);
			for (DReference ref : sub) {
				if (ref.isVerb()) {
					verbs.add(ref);
				}
			}
		}
		return verbs;
	}

	public boolean isLoaded() {
		return !(datos.palabrasNativo == null || almacen == null);
	}

	/** *************** MODIFICADORAS **************** **/
	// Totales

	// No llamar nunca a esta funcion, se nutre a traves de IDDelved
	public void setPalabras(ArrayList<Word> words) {
		datos.palabrasNativo = new ArrayList<Word>(words.size());
		for (Word temp : words) {
			datos.palabrasNativo.add(temp.cloneReverse());
		}
	}

	// No llamar nunca a esta funcion, se nutre a traves de IDDelved
	public void setStore(ArrayList<Nota> store) {
		almacen = store;
	}

	// Parciales add
	public void addPalabra(Word p) {
		datos.indexa(p.cloneReverse());
	}

	public void tirarPalabra(Word thrown) {
		Word reverse = thrown.cloneReverse();
		datos.desindexa(reverse);
		datos.papelera.add(reverse);
	}

	// Restores
	public void restorePalabra(int binposition) {
		Word p = datos.papelera.remove(binposition);
		datos.indexa(p.cloneReverse());
	}

	public void reindexar(Word p, String name, String trad, String spell,
			int type) {
		datos.desindexa(p.cloneReverse());
		p.setChanges(name, trad, spell, type);
		datos.indexa(p.cloneReverse());
	}

	// De clase
	public boolean isIdiomaNativo() {
		return true;
	}

}
