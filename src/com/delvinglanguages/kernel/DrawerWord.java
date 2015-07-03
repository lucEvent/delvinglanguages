package com.delvinglanguages.kernel;

public class DrawerWord {

	public final int id;
	private String nota;
	private boolean forIdioma;

	public DrawerWord(int id, String name, int type) {
		this.id = id;
		nota = name;
		forIdioma = type == 0;
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
