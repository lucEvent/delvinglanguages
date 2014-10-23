package com.delvinglanguages.core;


//No operativo
public class Nota {

	public final int id;
	private String nota;
	private boolean forIdioma; // 0 for idioma, 1 for antiidioma

	public Nota(int ident, String name, int type) {
		id = ident;
		nota = name;
		forIdioma = type == 0 ? true : false;
	}

	public String get() {
		return nota;
	}

	public boolean forIdioma() {
		return forIdioma;
	}

	public boolean forAntiIdioma() {
		return !forIdioma;
	}

}
