package com.delvinglanguages.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;

import android.util.Log;

import com.delvinglanguages.core.Nota;
import com.delvinglanguages.core.Palabra;

public class IDNativo extends IDDelved {

	private static final String DEBUG = "##IDNativo##";

	/** **************** CREADORAS **************** **/
	public IDNativo(Datos data) {
		super(data);
		// Aqui tengo que modificar CODE
	}

	/** *************** CONSULTORAS **************** **/
	public Palabra getPalabra(String name) {
		return getPalabra(name, datos.diccionarioNativo.get(name.charAt(0)));
	}

	public Palabra getPalabra(int id) {
		return super.getPalabra(id).cloneReverse();
	}

	public DReference getReference(String name) {
		TreeSet<DReference> sub = datos.diccionarioNativo.get(name.charAt(0));
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

	public ArrayList<Palabra> getPapelera() {
		ArrayList<Palabra> papelera = new ArrayList<Palabra>();
		for (int i = 0; i < datos.papelera.size(); i++) {
			papelera.add(datos.papelera.get(i).cloneReverse());
		}
		return datos.papelera;
	}

	public boolean contains(Palabra p) {
		return datos.palabrasNativo.contains(p);
	}

	public ArrayList<Palabra> getPalabras() {
		return datos.palabrasNativo;
	}

	public Hashtable<Character, TreeSet<DReference>> getDiccionario() {
		return datos.diccionarioNativo;
	}

	public ArrayList<DReference> getVerbs() {
		ArrayList<DReference> verbs = new ArrayList<DReference>();
		TreeSet<Character> keys = new TreeSet<Character>(
				datos.diccionarioNativo.keySet());
		for (Character key : keys) {
			TreeSet<DReference> sub = datos.diccionarioNativo.get(key);
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
	public void setPalabras(ArrayList<Palabra> words) {
		datos.palabrasNativo = new ArrayList<Palabra>(words.size());
		for (Palabra temp : words) {
			datos.palabrasNativo.add(temp.cloneReverse());
		}
	}

	// No llamar nunca a esta funcion, se nutre a traves de IDDelved
	public void setStore(ArrayList<Nota> store) {
		almacen = store;
	}

	// Parciales add
	public void addPalabra(Palabra p) {
		datos.indexa(p.cloneReverse());
	}

	public void tirarPalabra(Palabra thrown) {
		Palabra reverse = thrown.cloneReverse();
		datos.desindexa(reverse);
		datos.papelera.add(reverse);
	}

	// Restores
	public void restorePalabra(int binposition) {
		Palabra p = datos.papelera.remove(binposition);
		datos.indexa(p.cloneReverse());
	}

	public void reindexar(Palabra p, String name, String trad, String spell,
			int type) {
		datos.desindexa(p.cloneReverse());
		p.setChanges(name, trad, spell, type);
		datos.indexa(p.cloneReverse());
	}

	public void createDictionaries() {
		if (datos.diccionarioNativo == null) {
			datos.dictionariesCreated = false;
			datos.createDictionaries();
		}
	}

	// De clase
	public boolean isIdiomaNativo() {
		return true;
	}

}
